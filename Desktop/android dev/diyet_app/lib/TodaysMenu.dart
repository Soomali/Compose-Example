import 'package:diyet_app/CustomDrawer.dart';
import 'package:diyet_app/MenuDisplayItem.dart';
import 'package:diyet_app/Searchbar.dart';
import 'package:diyet_app/storageManager.dart';
import 'package:flutter/material.dart';

class TodaysMenuAddPage extends StatelessWidget {
  const TodaysMenuAddPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        drawer: CustomDrawer(),
        bottomNavigationBar: BottomBar(),
        body: Column(
          children: [AppBarWithNoSearch(), ExpandableMenuList()],
        ),
      ),
    );
  }
}

class BottomBar extends StatefulWidget {
  const BottomBar({
    Key? key,
  }) : super(key: key);

  @override
  _BottomBarState createState() => _BottomBarState();
}

class _BottomBarState extends State<BottomBar> {
  late StorageManagerListener _listener;
  @override
  void initState() {
    super.initState();
    _listener = StorageManagerListener(
        onChanged: () {
          setState(() {});
        },
        mod: 2);
    StorageManager().addListener(_listener);
  }

  @override
  void dispose() {
    _listener.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration:
          BoxDecoration(border: Border(top: BorderSide(color: Colors.blue))),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          Text(
            'Toplam kalori: \n${_listener.todays!.sumCalories}',
            style: TextStyle(color: Colors.blue, fontSize: 20),
          ),
          TextButton(
              onPressed: () {
                print('ok');
              },
              child: Text(
                'Bugün \nyediklerime ekle',
                style: TextStyle(color: Colors.blue, fontSize: 20),
              ))
        ],
      ),
    );
  }
}

class ExpandableMenuList extends StatefulWidget {
  const ExpandableMenuList({Key? key}) : super(key: key);

  @override
  _ExpandableMenuListState createState() => _ExpandableMenuListState();
}

class _ExpandableMenuListState extends State<ExpandableMenuList> {
  var todaysMenus = StorageManager().todaysMenusMap;
  late List<bool> _isOpen;
  @override
  void initState() {
    _isOpen = List.generate(todaysMenus.length, (index) => false);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    print(todaysMenus.length);
    return Expanded(
      child: ListView(
        children: [
          ExpansionPanelList(
            dividerColor: Colors.blue,
            expansionCallback: (i, isOpen) => setState(() {
              _isOpen[i] = !isOpen;
            }),
            children: List.generate(
                todaysMenus.length,
                (index) => ExpansionPanel(
                    backgroundColor: Colors.black,
                    canTapOnHeader: true,
                    headerBuilder: (context, isOpen) {
                      return Text(
                        todaysMenus.keys.elementAt(index).name,
                        style: TextStyle(color: Colors.blue),
                      );
                    },
                    body: MenuDisplayWidget(
                        menu: todaysMenus.keys.elementAt(index)),
                    isExpanded: _isOpen[index])),
          ),
        ],
      ),
    );
  }
}
