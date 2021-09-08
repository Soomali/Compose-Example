import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'entities.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';
import 'storageManager.dart';
import 'Searchbar.dart';
import 'CustomDrawer.dart';
import 'Basket.dart';

const TextStyle _menuItemStyle = TextStyle(color: Colors.blue, fontSize: 16);

class MenuPage extends StatelessWidget {
  const MenuPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        body: MenuDisplayList(),
        drawer: CustomDrawer(),
        floatingActionButton: Basket(),
      ),
    );
  }
}

class MenuDisplayList extends StatefulWidget {
  const MenuDisplayList({Key? key}) : super(key: key);

  @override
  _MenuDisplayListState createState() => _MenuDisplayListState();
}

class _MenuDisplayListState extends State<MenuDisplayList> {
  static const int _pageSize = 30;
  final manager = StorageManager();
  int lastEdibleId = 0;
  final PagingController<int, Menu> controller =
      PagingController(firstPageKey: 0);
  String? _search;
  @override
  void initState() {
    controller.addPageRequestListener((pageKey) {
      _fetchPage(pageKey);
    });
    super.initState();
  }

  Future<void> _fetchPage(int pageKey) async {
    try {
      final newItems = await manager.getMenus(
          lastMenuID: lastEdibleId, searchParam: _search);
      final isLastPage = newItems.length < _pageSize;
      if (isLastPage) {
        controller.appendLastPage(newItems);
      } else {
        final nextPageKey = pageKey + newItems.length;
        controller.appendPage(newItems, nextPageKey);
      }
      lastEdibleId = newItems.last.id;
    } catch (error) {
      controller.error = error;
    }
  }

  void refresh(String newName) {
    _search = newName;
    lastEdibleId = 0;
    controller.refresh();
  }

  @override
  Widget build(BuildContext context) => Column(
        children: [
          SearchArea(
            onChanged: (String name) => refresh(name),
            controller: TextEditingController(),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: PagedListView<int, Menu>(
                pagingController: controller,
                builderDelegate: PagedChildBuilderDelegate<Menu>(
                  itemBuilder: (context, item, index) => MenuDisplayWidget(
                    menu: item,
                  ),
                ),
              ),
            ),
          ),
        ],
      );

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }
}

class MenuDisplayWidget extends StatefulWidget {
  final Menu menu;
  const MenuDisplayWidget({Key? key, required this.menu}) : super(key: key);

  @override
  _MenuDisplayWidgetState createState() => _MenuDisplayWidgetState();
}

class _MenuDisplayWidgetState extends State<MenuDisplayWidget> {
  int limit = 4;
  List<Widget> get _widgets {
    List<Widget> widgets = [
      Padding(
        padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 50),
        child: Align(
          alignment: Alignment.bottomCenter,
          child: Text(
            widget.menu.name,
            style: TextStyle(color: Colors.blue, fontSize: 25),
          ),
        ),
      )
    ];
    int count = 0;
    for (MapEntry<Edible, int> i in widget.menu.edibles.entries) {
      if (count > limit) {
        break;
      }
      count++;
      var value = i.value;
      var edible = i.key;
      widgets.add(Padding(
        padding: const EdgeInsets.all(12.0),
        child: MenuItemDisplay(
          name:
              '$value adet ${edible.name.length > 40 ? edible.name.substring(0, 40) + '...' : edible.name}',
          quantity: value,
          calorie: edible.calorie,
        ),
      ));
    }
    if (count > limit) {
      widgets.add(GestureDetector(
        onTap: () {
          setState(() {
            limit += 4;
          });
        },
        child: Padding(
          padding: EdgeInsets.all(12),
          child: Align(
            alignment: Alignment.center,
            child: Container(child: Text('...', style: _menuItemStyle)),
          ),
        ),
      ));
    }
    widgets.add(Padding(
      padding: const EdgeInsets.all(12.0),
      child: MenuItemDisplay(
          name: "Toplam Kalori:", calorie: widget.menu.calories, quantity: 1),
    ));
    return widgets;
  }

  bool shown = true;
  @override
  Widget build(BuildContext context) {
    return !shown
        ? Text('')
        : Padding(
            padding: const EdgeInsets.symmetric(vertical: 16.0),
            child: Stack(
              children: [
                Container(
                  decoration: BoxDecoration(
                      border: Border.all(color: Colors.blue),
                      borderRadius: BorderRadius.circular(25),
                      color: Colors.black),
                  child: Column(
                    children: _widgets,
                  ),
                ),
                Positioned(
                    top: -2,
                    right: 10,
                    child: IconButton(
                        onPressed: () {
                          setState(() {
                            shown = false;
                          });
                          StorageManager().deleteMenuFromDB(widget.menu);
                        },
                        icon: Icon(
                          Icons.delete,
                          color: Colors.blue,
                          size: 35,
                        ))),
                AddSubstractMenu(menu: widget.menu),
              ],
            ),
          );
  }
}

class AddSubstractMenu extends StatefulWidget {
  final Menu menu;
  const AddSubstractMenu({
    required this.menu,
    Key? key,
  }) : super(key: key);

  @override
  _AddSubstractMenuState createState() => _AddSubstractMenuState();
}

class _AddSubstractMenuState extends State<AddSubstractMenu> {
  Timer? t;
  StorageManager manager = StorageManager();
  int count = 0;
  void _add() {
    manager.addMenu(widget.menu);

    setState(() {
      count++;
    });
  }

  void _substract() {
    manager.removeMenu(widget.menu);
    setState(() {
      count--;
    });
  }

  @override
  void dispose() {
    super.dispose();
    if (t != null) {
      t!.cancel();
    }
  }

  @override
  void initState() {
    count = manager.getCountOnTodays(widget.menu) ?? 0;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Positioned.fill(
        bottom: -18,
        child: Align(
          alignment: Alignment.bottomCenter,
          child: AnimatedContainer(
            duration: Duration(milliseconds: 250),
            width: count != 0
                ? MediaQuery.of(context).size.width * 0.265
                : MediaQuery.of(context).size.width * 0.1325,
            height: 60,
            decoration: BoxDecoration(
              color: Colors.black,
              border: Border.all(color: Colors.blue),
              borderRadius: BorderRadius.circular(30),
            ),
            child: ListView(
              scrollDirection: Axis.horizontal,
              physics: NeverScrollableScrollPhysics(),
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: count != 0
                      ? [
                          Padding(padding: EdgeInsets.only(left: 4)),
                          GestureDetector(
                            onTap: _substract,
                            onTapDown: (TapDownDetails d) {
                              t = Timer.periodic(Duration(milliseconds: 150),
                                  (timer) {
                                if (count > 0) {
                                  _substract();
                                } else {
                                  t!.cancel();
                                }
                              });
                            },
                            onTapUp: (TapUpDetails d) {
                              t!.cancel();
                            },
                            child: Padding(
                              padding: EdgeInsets.all(
                                  8.0 - (count.toString().length * 2 % 9)),
                              child: Icon(
                                Icons.remove_circle_outline,
                                color: Colors.blue,
                                size: 30,
                              ),
                            ),
                          ),
                          Text(
                            '$count',
                            style: TextStyle(
                                color: Colors.blue,
                                fontSize: 30 - (count.toString().length * 4)),
                          ),
                          GestureDetector(
                            onTap: _add,
                            onTapDown: (TapDownDetails d) {
                              t = Timer.periodic(Duration(milliseconds: 150),
                                  (timer) {
                                _add();
                              });
                            },
                            onTapUp: (TapUpDetails d) {
                              t!.cancel();
                            },
                            child: Padding(
                              padding: EdgeInsets.all(
                                  8.0 - (count.toString().length * 2 % 9)),
                              child: Icon(Icons.add_circle_outline,
                                  color: Colors.blue, size: 30),
                            ),
                          ),
                        ]
                      : [
                          IconButton(
                              alignment: Alignment.center,
                              padding: EdgeInsets.only(
                                  left: MediaQuery.of(context).size.width *
                                      0.015),
                              onPressed: _add,
                              icon: Icon(
                                Icons.add,
                                color: Colors.blue,
                                size: 40,
                              ))
                        ],
                ),
              ],
            ),
          ),
        ));
  }
}

// class AddMenuFirstTime extends StatelessWidget {
//   final void Function() onPressed;

//   const AddMenuFirstTime({
//     Key? key,
//     required this.onPressed,
//   }) : super(key: key);

//   @override
//   Widget build(BuildContext context) {
//     return Positioned.fill(
//         bottom: -18,
//         child: Align(
//           alignment: Alignment.bottomCenter,
//           child: Container(
//             width: 60,
//             height: 60,
//             decoration: BoxDecoration(
//               color: Colors.black,
//               border: Border.all(color: Colors.blue),
//               borderRadius: BorderRadius.circular(30),
//             ),
//             child: ,
//           ),
//         ));
//   }
// }

class MenuItemDisplay extends StatelessWidget {
  final String name;
  final num calorie;
  final int quantity;
  const MenuItemDisplay(
      {Key? key,
      required this.name,
      required this.calorie,
      required this.quantity})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Flexible(
          child: Text(name, style: _menuItemStyle),
        ),
        Align(
          alignment: Alignment.centerRight,
          child: Text(
            '${(calorie * quantity).toStringAsFixed(2)}',
            style: _menuItemStyle,
          ),
        )
      ],
    );
  }
}
