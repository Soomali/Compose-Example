import 'dart:math';

T sum<T extends num>(Iterable<T> nums) {
  num sum = 0;
  for (T i in nums) {
    sum += i;
  }
  return sum as T;
}

class _MapHolder<T> {
  Map<T, int> _map;
  _MapHolder(this._map);
  void add(T variable, int count) {
    if (_map.containsKey(variable)) {
      _map[variable] = count + _map[variable]!;
    } else {
      _map[variable] = count;
    }
  }

  void substract(T variable, int count) {
    if (_map.containsKey(variable)) {
      _map[variable] = _map[variable]! - count;
      if (_map[variable]! <= 0) {
        _map.remove(variable);
      }
    }
  }
}

class Edible {
  late int id;
  late String name;
  OptionHolder optionHolder;
  Edible(this.name, this.id, this.optionHolder);
  Edible.fromMap(Map<String, dynamic> _map, this.optionHolder) {
    this.name = _map["name"];
    this.id = _map["id"];
  }
  double get calorie => optionHolder.calorie;
  @override
  String toString() {
    return "(id:$id,name:$name,options:$optionHolder)";
  }

  List<String> get getAsValues {
    return [name];
  }

  Option get activeOption {
    return optionHolder.active;
  }
}

class OptionHolder {
  late List<Option> options;
  int activeOption = 0;
  OptionHolder(
    this.options,
  );
  OptionHolder.fromQuery(List<Map<String, dynamic>> optionsRaw,
      {int? activeId}) {
    this.options = optionsRaw.map((e) => Option.fromMap(e)).toList();
    if (activeId != null) {
      activeOption = options.indexWhere((element) => element.id == activeId);
    }
  }
  void setActiveOption(Option opt) {
    activeOption = options.indexOf(opt);
  }

  double get calorie {
    return active.calorie;
  }

  Option get active {
    return options[activeOption];
  }

  @override
  String toString() {
    return '{active:$activeOption,$options}';
  }
}

class Option {
  late String name;
  late double calorie;
  late int id;
  Option(this.name, this.calorie, this.id);
  Option.fromMap(Map<String, dynamic> _map) {
    this.name = _map['name'];
    this.calorie = _map['Calorie'];
    this.id = _map['id'];
  }
  List<dynamic> get values {
    return [calorie, name];
  }

  @override
  String toString() {
    return 'Option(name:$name,id:$id,calorie:$calorie)';
  }

  @override
  bool operator ==(Object other) {
    return other is Option && other.id == id;
  }

  @override
  int get hashCode => id.hashCode;
}

class Menu extends _MapHolder<Edible> {
  static Menu get empty {
    return Menu({}, '', -1);
  }

  String name;
  int id;
  Map<Edible, int> get edibles => _map;

  Menu(Map<Edible, int> edibles, this.name, this.id) : super(edibles);

  double get calories {
    double cals = 0;
    edibles.forEach((key, value) {
      cals += key.calorie * value;
    });
    return cals;
  }

  @override
  String toString() {
    return 'name:$name,content:$edibles';
  }
}

class MenuList extends _MapHolder<Menu> {
  static MenuList get empty {
    return MenuList(menus: {});
  }

  int get menuCount => sum(_map.values);

  double get sumCalories {
    double sum = 0;
    for (MapEntry entry in _map.entries) {
      Menu menu = entry.key;
      sum += menu.calories * entry.value;
    }
    return sum;
  }

  MenuList({required Map<Menu, int> menus}) : super(menus);

  int? operator [](Menu key) {
    return _map[_map.keys.firstWhere((element) => element.id == key.id,
        orElse: () => Menu.empty)];
  }

  Map<Menu, int> get map => _map;
}
