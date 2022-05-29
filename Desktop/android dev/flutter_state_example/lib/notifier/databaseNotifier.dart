import 'package:flutter/material.dart';
import 'package:flutter_state_example/db/database.dart';

import '../db/todo.dart';

enum DatabaseState {
  error,
  busy,
  idle,
}

class DatabaseNotifier extends ChangeNotifier {
  final DatabaseModel model;
  DatabaseState state = DatabaseState.idle;
  String? error;
  final List<Todo> todos = [];
  DatabaseNotifier(this.model);
  void addTodo(String title, String todo) async {
    _makeReadyForOperation();
    final result = await model.addTodo(Todo(id: -1, todo: todo, title: title));
    if (result != null) {
      todos.add(Todo(id: result, todo: todo, title: title));
    }
    _handleResult(result == null ? null : 'Insert işlemi başarısız.');
  }

  void _makeReadyForOperation() {
    error = null;
    state = DatabaseState.busy;
    notifyListeners();
  }

  void updateTodo(Todo todo) async {
    _makeReadyForOperation();
    final result = await model.updateTodo(todo);
    if (result == null) {
      todos[todos.indexOf(
          todos.firstWhere((element) => element.id == todo.id))] = todo;
    }
    _handleResult(result);
  }

  void deleteTodo(Todo todo) async {
    _makeReadyForOperation();
    final result = await model.removeTodo(todo);
    if (result == null) {
      todos.remove(todo);
    }
    _handleResult(result);
  }

  void getTodos() async {
    _makeReadyForOperation();
    try {
      final newTodos =
          await model.getTodos(lastId: todos.isNotEmpty ? todos.last.id : null);
      todos.addAll(newTodos ?? []);
      _changeToIdleState();
    } catch (e) {
      _changeToErrorState(e.toString());
    }
  }

  void _handleResult(String? result) {
    if (result != null) {
      _changeToErrorState(result);
    } else {
      _changeToIdleState();
    }
  }

  void _changeToIdleState() {
    state = DatabaseState.idle;
    notifyListeners();
  }

  void _changeToErrorState(String error) {
    this.error = error;
    state = DatabaseState.error;
    notifyListeners();
  }
}
