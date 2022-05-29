import 'package:flutter_state_example/db/todo.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseModel {
  Database? _db;

  //returns the error if any. returns null otherwise.
  Future<int?> addTodo(Todo todo) async {
    await initDb();
    try {
      int? id = await _db?.insert(Todo.TABLE_NAME, todo.asMap);
      return id;
    } catch (e) {
      return null;
    }
  }

  Future<void> initDb() async {
    await Future.delayed(Duration(milliseconds: 450));
    if (_db == null) {
      await _open();
    }
  }

  Future<String?> removeTodo(Todo todo) async {
    await initDb();
    try {
      _db?.delete(Todo.TABLE_NAME, where: 'id=?', whereArgs: [todo.id]);
    } catch (e) {
      return e.toString();
    }
  }

  Future<String?> updateTodo(Todo todo) async {
    await initDb();
    try {
      _db?.update(Todo.TABLE_NAME, todo.asMap,
          where: 'id=?', whereArgs: [todo.id]);
    } catch (e) {
      return e.toString();
    }
  }

  //any error encountered here should be handled within the caller.
  Future<List<Todo>?> getTodos({int? lastId}) async {
    await initDb();
    final todos = await _db?.query(Todo.TABLE_NAME,
        limit: 30,
        where: lastId == null ? null : 'id>?',
        whereArgs: lastId == null ? null : [lastId]);
    return todos?.map((e) => Todo.fromMap(e)).toList();
  }

  Future<void> _open() async {
    _db = await openDatabase('todo_db.db', version: 1,
        onCreate: ((db, version) async {
      await db.execute(
          'CREATE TABLE Todo(id INTEGER PRIMARY KEY,title TEXT,todo TEXT)');
    }));
  }
}
