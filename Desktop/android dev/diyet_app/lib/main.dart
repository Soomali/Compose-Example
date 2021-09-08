import 'dart:async';

import 'package:diyet_app/entities.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';
import 'storageManager.dart';
import 'EdibleDisplayItem.dart';
import 'Searchbar.dart';
import 'CustomDrawer.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await PreferencesManager().initialize();
  await StorageManager().initialize();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        backgroundColor: Colors.black,
        scaffoldBackgroundColor: Colors.black,
        primarySwatch: Colors.blue,
      ),
      home: EdiblePage(),
    );
  }
}
