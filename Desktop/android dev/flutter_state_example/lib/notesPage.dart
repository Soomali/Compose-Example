import 'package:flutter/material.dart';
import 'package:flutter_state_example/addNotePage.dart';
import 'package:flutter_state_example/loaderPage.dart';
import 'package:flutter_state_example/notesLayout.dart';
import 'package:flutter_state_example/notifier/databaseNotifier.dart';
import 'package:provider/provider.dart';

class NotesPage extends StatelessWidget {
  const NotesPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    makeGetRequest(context);
    return Scaffold(
      appBar: AppBar(
        title: Text('Notes app'),
        actions: [
          IconButton(
              onPressed: () {
                Navigator.of(context).push(
                    MaterialPageRoute(builder: (context) => AddNotesPage()));
              },
              icon: Icon(Icons.note_add_rounded))
        ],
      ),
      body: NotesLayout(),
    );
  }

  void makeGetRequest(BuildContext context) {
    WidgetsBinding.instance?.addPostFrameCallback((_) {
      Provider.of<DatabaseNotifier>(context, listen: false).getTodos();
    });
  }
}
