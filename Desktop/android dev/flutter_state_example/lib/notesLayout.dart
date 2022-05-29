import 'package:flutter/material.dart';
import 'package:flutter_state_example/addNotePage.dart';
import 'package:flutter_state_example/db/todo.dart';
import 'package:flutter_state_example/loaderPage.dart';
import 'package:flutter_state_example/notifier/databaseNotifier.dart';
import 'package:provider/provider.dart';

class NotesLayout extends StatelessWidget {
  const NotesLayout({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(32.0),
      child: Consumer<DatabaseNotifier>(
        builder: ((context, value, child) {
          final todos = value.todos;
          if (value.state == DatabaseState.idle &&
              ModalRoute.of(context)?.isCurrent == false) {
            WidgetsBinding.instance
                ?.addPostFrameCallback((_) => Navigator.of(context).pop());
          }
          if (value.state == DatabaseState.busy) {
            WidgetsBinding.instance?.addPostFrameCallback((_) {
              Navigator.of(context).push(LoaderPage());
            });
          } else if (value.state == DatabaseState.error) {
            WidgetsBinding.instance?.addPostFrameCallback((_) {
              showDialog(
                  context: context,
                  builder: (context) {
                    return AlertDialog(
                      title: Text('HATA'),
                      content: Text(value.error!),
                    );
                  });
            });
          }
          if (todos.isEmpty) {
            return Text('Henüz not yok.');
          }

          return GridView.builder(
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              mainAxisSpacing: 125,
              crossAxisSpacing: 25,
            ),
            itemBuilder: (context, index) => TodoItem(todo: todos[index]),
            itemCount: todos.length,
          );
          ;
        }),
      ),
    );
  }
}

class TodoItem extends StatelessWidget {
  const TodoItem({
    Key? key,
    required this.todo,
  }) : super(key: key);

  final Todo todo;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onLongPress: () {
        Provider.of<DatabaseNotifier>(context, listen: false).deleteTodo(todo);
      },
      onTap: () {
        Navigator.of(context).push(MaterialPageRoute(
            //by giving the todo argument we told the addNotesPage that we will be
            //updating the todo instead of creating a new one
            builder: (context) => AddNotesPage(
                  todo: this.todo,
                )));
      },
      child: Container(
        decoration: BoxDecoration(
            border: Border.all(), borderRadius: BorderRadius.circular(12)),
        child: Column(
          children: [
            Text(
              todo.title,
              style: TextStyle(overflow: TextOverflow.ellipsis),
            ),
            Divider(
              thickness: 2,
              color: Colors.black45,
            ),
            Flexible(
              child: Text(
                todo.todo,
              ),
            )
          ],
        ),
      ),
    );
  }
}
