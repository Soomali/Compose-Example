import 'dart:io';
import 'package:flutter/services.dart';
import 'entities.dart';
import 'package:sqflite/sqflite.dart';
import 'package:shared_preferences/shared_preferences.dart';

const int VERSION = 1;
const Map<int, MapEntry<String, List<Object?>?>> versionScripts = {};

class PreferencesManager {
  Map<String, dynamic> happenings = {};
  final String _calorieNeed = 'user_calorieNeed';
  late SharedPreferences _preferences;

  static final PreferencesManager _instance = PreferencesManager._();
  PreferencesManager._();
  factory PreferencesManager() {
    return PreferencesManager._instance;
  }

  Future<void> initialize() async {
    _preferences = await SharedPreferences.getInstance();
  }

  double get userCalorieNeed {
    if (happenings.containsKey(_calorieNeed)) return happenings[_calorieNeed];
    return _preferences.getDouble(_calorieNeed) ?? 0;
  }

  set userCalorieNeed(double newCalorie) {
    happenings[_calorieNeed] = newCalorie;
    _preferences
        .setDouble(_calorieNeed, newCalorie)
        .then((value) => happenings.remove(_calorieNeed));
  }
}

class StorageManagerListener {
  final int mod;
  final void Function() onChanged;
  StorageManagerListener({required this.onChanged, this.mod = 0}) {
    update();
  }
  void update() {
    switch (mod) {
      case 0:
        current = StorageManager().current;
        break;
      case 1:
        count = StorageManager()._todaysMenus.menuCount;

        break;
      case 2:
        todays = StorageManager()._todaysMenus;
        break;
    }
  }

  MenuList? todays;
  Menu? current;
  int? count;
  void notify() {
    update();
    onChanged();
  }

  void dispose() {
    StorageManager().removeListener(this);
  }
}

class StorageManager {
  DateTime today = DateTime.now();
  MenuList _todaysMenus = MenuList.empty;
  List<StorageManagerListener> listeners = [];
  Menu current = Menu.empty;
  Database? _db;
  static final StorageManager _instance = StorageManager._();
  StorageManager._();
  factory StorageManager() {
    return StorageManager._instance;
  }
  void notifyListeners() {
    listeners.forEach((element) {
      element.notify();
    });
  }

  void addListener(StorageManagerListener listener) {
    this.listeners.add(listener);
  }

  void removeListener(StorageManagerListener listener) {
    this.listeners.remove(listener);
  }

  void addMenu(Menu menu, {int count = 1}) {
    _todaysMenus.add(menu, count);
    notifyListeners();
  }

  double get todaysCalories => _todaysMenus.sumCalories;

  int? getCountOnTodays(Menu menu) {
    return _todaysMenus[menu];
  }

  void removeMenu(Menu menu, {int count = 1}) {
    _todaysMenus.substract(menu, count);
    notifyListeners();
  }

  void addToCurrent(Edible edible, {int count = 1}) {
    current.add(edible, count);
    notifyListeners();
  }

  void substractFromCurrent(Edible edible, {int count = 1}) {
    current.substract(edible, count);
    notifyListeners();
  }

  Future<bool> initialize() async {
    var databasesPath = await getDatabasesPath();
    var path = databasesPath + "/Diyet.db";
// Check if the database exists
    var exists = await databaseExists(path);

    if (!exists) {
      // Copy from asset
      ByteData data = await rootBundle.load("assets/Diyet.db");
      List<int> bytes =
          data.buffer.asUint8List(data.offsetInBytes, data.lengthInBytes);

      // Write and flush the bytes written
      await File(path).writeAsBytes(bytes, flush: true);
    } else {
      print("Opening existing database");
    }
// open the database
    _db = await openDatabase(path, version: VERSION,
        onUpgrade: (db, oldVersion, newVersion) async {
      var batch = db.batch();
      if (oldVersion > newVersion) oldVersion = 0;
      for (int key = oldVersion + 1; key <= newVersion; key++) {
        if (versionScripts.containsKey(key)) {
          batch.execute(versionScripts[key]!.key, versionScripts[key]!.value);
        }
      }
      await batch.commit();
    });
    await printTables();
    return true;
  }

  Future<List<Edible>> getEdibles(
      {int limit = 30, int lastEdibleID = 0, String? searchParam}) async {
    List<dynamic> whereArgs = [lastEdibleID];
    String? where = 'id > ?';
    if (searchParam != null) {
      whereArgs.add('%$searchParam%');
      where += ' and name LIKE ?';
    }

    var ediblesQuery = await _db!
        .query("Edible", where: where, whereArgs: whereArgs, limit: limit);
    return createEdibles(ediblesQuery);
  }

  Future<List<Edible>> createEdibles(List<Map<String, dynamic>> ediblesQuery,
      {int? activeId}) async {
    List<Edible> edibles = [];
    for (Map<String, dynamic> edibleRaw in ediblesQuery) {
      int id = edibleRaw['id'];
      if (current.edibles.keys.any((element) => element.id == id)) {
        edibles.add(
            current.edibles.keys.firstWhere((element) => element.id == id));
      } else {
        String name = edibleRaw['name'];
        var options = await _db!
            .query("EdibleOptions", where: 'edibleID = ?', whereArgs: [id]);
        OptionHolder holder =
            OptionHolder.fromQuery(options, activeId: activeId);
        edibles.add(Edible(name, id, holder));
      }
    }
    return edibles;
  }

  Future<List<Menu>> getMenus(
      {int lastMenuID = 0, String? searchParam, bool shown = true}) async {
    var where = 'id > ?';
    var whereArgs = <dynamic>[lastMenuID];
    if (shown) {
      where += ' and shown = ?';
      whereArgs.add(1);
    }
    if (searchParam != null) {
      where += ' and LIKE ?';
      whereArgs.add('%$searchParam%');
    }
    var menusQuery =
        await _db!.query("Menu", where: where, whereArgs: whereArgs, limit: 30);

    return createMenus(menusQuery);
  }

  Future<List<Menu>> createMenus(List<Map<String, Object?>> menusQuery) async {
    List<Menu> menus = [];

    for (Map<String, dynamic> rawMenu in menusQuery) {
      int id = rawMenu['id'];
      var menuItems =
          await _db!.query("MenuItems", where: 'menuID = ?', whereArgs: [id]);

      Menu menu = Menu({}, rawMenu['name'], rawMenu['id']);

      await initMenu(menuItems, menu);
      menus.add(menu);
    }
    return menus;
  }

  Future<void> initMenu(List<Map<String, Object?>> menuItems, Menu menu) async {
    for (Map<String, dynamic> rawMenuItem in menuItems) {
      int activeId = rawMenuItem['optionId'];
      int edibleId = rawMenuItem['edibleID'];
      List<Map<String, dynamic>> ediblesQuery =
          await _db!.query('Edible', where: 'id = ?', whereArgs: [edibleId]);
      List<Edible> edible =
          await createEdibles(ediblesQuery, activeId: activeId);
      menu.add(edible.first, rawMenuItem['quantity']);
    }
  }

  Future<void> insertEdible(Edible edible) async {
    int id = await _db!
        .rawInsert("INSERT INTO Edible(name) VALUES(?)", edible.getAsValues);
    Batch b = _db!.batch();
    for (Option i in edible.optionHolder.options) {
      b.insert('EdibleOptions', {
        "edibleID": id,
        "name": i.name,
        "Calorie": i.calorie,
      });
    }
    await b.commit();
  }

  Future<void> deleteMenuFromDB(Menu menu) async {
    await _db!
        .rawUpdate('UPDATE Menu SET shown = ? WHERE id = ?', [0, menu.id]);
  }

  @deprecated
  Future<void> deleteTableContent(String table) async {
    await _db!.delete(table);
  }

  Future<void> insertCurrentMenu() async {
    print(current);
    _insertMenu(current);
    this.current = Menu.empty;
    print(current);
    notifyListeners();
  }

  Future<Map<Menu, int>> getDayMenus({String? timeString}) async {
    timeString ??= converToDateString(DateTime.now());
    var dayEntries = await _db!
        .query('DayEntry', where: 'date = ?', whereArgs: [timeString]);
    Map<int, int> menusMap = {};

    for (Map map in dayEntries) {
      var id = map['MenuID'];
      menusMap[id] = map['quantity'];
    }
    var menusQuery = await _db!
        .rawQuery('SELECT * FROM Menus WHERE id IN ?', menusMap.keys.toList());
    List<Menu> menus = await this.createMenus(menusQuery);

    return menusMap.map<Menu, int>((key, value) =>
        MapEntry(menus.firstWhere((element) => element.id == key), value));
  }

  Future<List<Map<Menu, int>>> getHistoricDayMenus(
      {int limit = 30, DateTime? startDate}) async {
    startDate ??= DateTime.now().subtract(Duration(days: 1));
    List<Map<Menu, int>> dayData = [];
    for (int i = 0; i < limit; i++) {
      Map<Menu, int> menusDate =
          await getDayMenus(timeString: converToDateString(startDate!));
      startDate = startDate.subtract(Duration(days: 1));
      dayData.add(menusDate);
    }
    return dayData;
  }

  Map<Menu, int> get todaysMenusMap => _todaysMenus.map;

  Future<void> insertDayEntry() async {
    var batch = _db!.batch();
    var menus = todaysMenusMap;
    var date = DateTime.now();
    var dateString = converToDateString(date);
    for (MapEntry<Menu, int> menuEntry in menus.entries) {
      batch.rawInsert(
          'INSERT INTO DayEntry(Date,quantity,MenuID) VALUES(?,?,?)',
          [dateString, menuEntry.value, menuEntry.key.id]);
    }
    await batch.commit();
  }

  String converToDateString(DateTime date) {
    return '${date.year}:${date.month}:${date.day}';
  }

  Future<void> _insertMenu(Menu menu) async {
    int id =
        await _db!.rawInsert('INSERT INTO Menu(name) VALUES(?)', [menu.name]);
    var batch = _db!.batch();
    for (Edible edible in menu.edibles.keys) {
      batch.insert('MenuItems', {
        "edibleID": edible.id,
        "menuID": id,
        "quantity": menu.edibles[edible],
        "optionId": edible.optionHolder.active.id,
      });
    }
    await batch.commit();
  }

  @deprecated
  Future<void> printTables() async {
    (await _db!.query('sqlite_master', columns: ['type', 'name']))
        .forEach((row) {
      print(row.values);
    });
  }

  /*
    optionData should look like this
    edibleid:1,
    name:bir çay kaşığı,
    calorie:75
   */
  Future<void> insertOption(Map<String, dynamic> optionData) async {
    await _db!.insert('EdibleOptions', optionData);
  }
}
