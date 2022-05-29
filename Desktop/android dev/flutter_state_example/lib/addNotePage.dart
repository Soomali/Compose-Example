import 'package:flutter/material.dart';
import 'package:flutter_state_example/notifier/databaseNotifier.dart';
import 'package:provider/provider.dart';

import 'db/todo.dart';

class AddNotesPage extends StatelessWidget {
  final Todo? todo;
  const AddNotesPage({Key? key, this.todo}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final TextEditingController titleController =
        TextEditingController(text: todo?.title);
    final TextEditingController todoController =
        TextEditingController(text: todo?.todo);
    return Scaffold(
      appBar: AppBar(),
      body: Column(
        children: [
          TextField(
            maxLines: null,
            maxLength: 45,
            controller: titleController,
            decoration: InputDecoration(hintText: 'Başlık'),
          ),
          Divider(
            thickness: 2,
          ),
          Expanded(
            child: SizedBox(
              width: MediaQuery.of(context).size.width,
              child: TextField(
                maxLines: null,
                controller: todoController,
                decoration:
                    InputDecoration(hintText: 'Not', border: InputBorder.none),
              ),
            ),
          ),
          Align(
            alignment: Alignment.centerRight,
            child: IconButton(
                onPressed: () {
                  if (todoController.text.isNotEmpty &&
                      titleController.text.isNotEmpty) {
                    final notifier =
                        Provider.of<DatabaseNotifier>(context, listen: false);
                    if (todo == null) {
                      notifier.addTodo(
                          titleController.text, todoController.text);
                    } else {
                      notifier.updateTodo(Todo(
                          todo: todoController.text,
                          id: todo!.id,
                          title: titleController.text));
                    }
                    Navigator.of(context).pop();
                  }
                },
                icon: Icon(Icons.note_add)),
          )
        ],
      ),
    );
  }
}
