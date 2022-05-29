import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_state_example/db/database.dart';
import 'package:flutter_state_example/notifier/databaseNotifier.dart';
import 'package:provider/provider.dart';

import 'notesPage.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final model = DatabaseModel();
    return ChangeNotifierProvider.value(
        value: DatabaseNotifier(model),
        child: MaterialApp(
          home: SafeArea(
            child: NotesPage(),
          ),
        ));
  }
}
