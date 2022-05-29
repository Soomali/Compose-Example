class Todo {
  final String todo;
  final int id;
  final String title;
  static const String TABLE_NAME = 'Todo';
  Map<String, String> get asMap => {'todo': todo, 'title': title};
  const Todo({required this.todo, required this.id, required this.title});
  factory Todo.fromMap(Map<String, dynamic> data) {
    return Todo(id: data['id'], todo: data['todo'], title: data['title']);
  }
}
