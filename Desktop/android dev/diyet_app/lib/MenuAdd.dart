import 'package:diyet_app/MenuDisplayItem.dart';
import 'package:flutter/material.dart';
import 'storageManager.dart';
import 'CustomDrawer.dart';
import 'Searchbar.dart';
// import 'entities.dart';
import 'EdibleDisplayItem.dart';

class MenuAddPage extends StatelessWidget {
  const MenuAddPage({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    var manager = StorageManager();

    return SafeArea(
        child: Scaffold(
      drawer: CustomDrawer(),
      body: MenuAddContainer(),
      floatingActionButton: Container(
        decoration: BoxDecoration(
            border: Border.all(color: Colors.blue, width: 2.5),
            color: Colors.black,
            borderRadius: BorderRadius.circular(12)),
        child: TextButton.icon(
            label: Text(
              'Menüyü ekle',
              style: TextStyle(color: Colors.blue),
            ),
            onPressed: () {
              var controller = TextEditingController();
              showDialog(
                  context: context,
                  builder: (context) {
                    return MenuNameGetter(controller: controller);
                  }).then((value) {
                if (value != null && value) {
                  manager.current.name = controller.text;
                  manager.insertCurrentMenu().then((value) =>
                      Navigator.of(context).pushReplacement(MaterialPageRoute(
                          builder: (BuildContext cont) => MenuPage())));
                }
              });
            },
            icon: Icon(
              Icons.add,
              color: Colors.blue,
              size: 52,
            )),
      ),
    ));
  }
}

class MenuNameGetter extends StatefulWidget {
  const MenuNameGetter({
    Key? key,
    required this.controller,
  }) : super(key: key);

  final TextEditingController controller;

  @override
  _MenuNameGetterState createState() => _MenuNameGetterState();
}

class _MenuNameGetterState extends State<MenuNameGetter> {
  bool hasError = false;
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      backgroundColor: Colors.black,
      title: Text(
        'Menü adı',
        style: TextStyle(color: Colors.blue),
      ),
      content: TextField(
        style: TextStyle(color: Colors.blue),
        decoration: InputDecoration(
            enabledBorder:
                OutlineInputBorder(borderSide: BorderSide(color: Colors.grey)),
            errorBorder:
                OutlineInputBorder(borderSide: BorderSide(color: Colors.red)),
            errorText: hasError ? 'Menü ismi boş olamaz!' : null,
            border:
                OutlineInputBorder(borderSide: BorderSide(color: Colors.blue))),
        controller: widget.controller,
      ),
      actions: [
        TextButton(
            onPressed: () {
              if (widget.controller.text.length != 0) {
                Navigator.of(context).pop(true);
              }
              setState(() {
                hasError = true;
              });
            },
            child: Text(
              'Tamam !',
            ))
      ],
    );
  }
}

class MenuAddContainer extends StatefulWidget {
  MenuAddContainer({Key? key}) : super(key: key);

  @override
  _MenuAddContainerState createState() => _MenuAddContainerState();
}

class _MenuAddContainerState extends State<MenuAddContainer> {
  final StorageManager manager = StorageManager();
  late StorageManagerListener _listener;
  @override
  void initState() {
    _listener = StorageManagerListener(onChanged: () {
      setState(() {});
    });
    manager.addListener(_listener);
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
    _listener.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        AppBarWithNoSearch(),
        Expanded(
          child: ListView.builder(
              itemCount: _listener.current!.edibles.keys.length,
              itemBuilder: (context, index) => EdibleItemDisplay(
                  edible: _listener.current!.edibles.keys.elementAt(index))),
        ),
      ],
    );
  }
}
