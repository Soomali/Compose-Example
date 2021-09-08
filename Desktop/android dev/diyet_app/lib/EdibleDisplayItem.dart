import 'dart:async';

import 'entities.dart';
import 'package:flutter/material.dart';
import 'package:infinite_scroll_pagination/infinite_scroll_pagination.dart';
import 'storageManager.dart';
import 'Searchbar.dart';
import 'CustomDrawer.dart';
import 'Basket.dart';

class EdiblePage extends StatelessWidget {
  const EdiblePage({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: Scaffold(
      body: EdibleItemSearch(),
      drawer: CustomDrawer(),
      floatingActionButton: Basket(),
    ));
  }
}

class EdibleItemSearch extends StatefulWidget {
  EdibleItemSearch({Key? key}) : super(key: key);
  final StorageManager manager = StorageManager();

  @override
  _EdibleItemSearchState createState() => _EdibleItemSearchState();
}

class _EdibleItemSearchState extends State<EdibleItemSearch> {
  @override
  Widget build(BuildContext context) {
    return EdibleItemList();
    //  FutureBuilder(
    //     future: widget.manager.initialize(),
    //     builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
    //       switch (snapshot.connectionState) {
    //         case ConnectionState.none:
    //           return Text("Veritabanı bekleniyor...");

    //         case ConnectionState.waiting:
    //           return Text("Veritabanı bekleniyor...");
    //         case ConnectionState.active:
    //           return CircularProgressIndicator();
    //         case ConnectionState.done:
    //           if (snapshot.hasData && snapshot.data!) {
    //             return EdibleItemList();
    //           }
    //           return Text('Hata oluştu, malişkoya bildir....');
    //       }
    //     });
  }
}

class EdibleItemList extends StatefulWidget {
  const EdibleItemList({Key? key}) : super(key: key);

  @override
  _EdibleItemListState createState() => _EdibleItemListState();
}

class _EdibleItemListState extends State<EdibleItemList> {
  static const int _pageSize = 30;
  final manager = StorageManager();
  int lastEdibleId = 0;
  final PagingController<int, Edible> controller =
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
      final newItems = await manager.getEdibles(
          lastEdibleID: lastEdibleId, searchParam: _search);
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
            child: PagedListView<int, Edible>(
              pagingController: controller,
              builderDelegate: PagedChildBuilderDelegate<Edible>(
                itemBuilder: (context, item, index) => EdibleItemDisplay(
                  edible: item,
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

class EdibleItemDisplay extends StatefulWidget {
  final Edible edible;
  const EdibleItemDisplay({Key? key, required this.edible}) : super(key: key);

  @override
  _EdibleItemDisplayState createState() => _EdibleItemDisplayState();
}

class _EdibleItemDisplayState extends State<EdibleItemDisplay> {
  int count = 0;
  @override
  void initState() {
    var manager = StorageManager();
    if (manager.current.edibles.containsKey(widget.edible)) {
      count = manager.current.edibles[widget.edible]!;
    }
    super.initState();
  }

  void addSelf() => setState(() {
        count++;
        StorageManager().addToCurrent(widget.edible);
      });

  void substractSelf() => setState(() {
        count--;
        StorageManager().substractFromCurrent(widget.edible);
      });
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 7.5, horizontal: 15),
      decoration: BoxDecoration(
          border: Border.all(color: Colors.blue, width: 1.5),
          borderRadius: BorderRadius.circular(35),
          color: Colors.black),
      width: double.infinity,
      child: Container(
        padding: EdgeInsets.fromLTRB(13, 15, 13, 15),
        child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Flexible(
                child: EdibleItemText(data: widget.edible.name),
                fit: FlexFit.loose,
              ),
              Expanded(
                child: widget.edible.optionHolder.options.length > 1
                    ? DropDown(edible: widget.edible)
                    : OptionDisplay(option: widget.edible.activeOption),
              ),
              count > 0
                  ? Align(
                      alignment: Alignment.centerRight,
                      child: AddQuantityDiscard(
                          onAddPressed: addSelf,
                          onDiscardPressed: substractSelf,
                          quantity: count),
                    )
                  : Align(
                      alignment: Alignment.centerRight,
                      child: IconButton(
                          onPressed: addSelf,
                          icon: Icon(
                            Icons.add_circle_outline,
                            size: 36,
                            color: Colors.blue,
                          )),
                    ),
            ]),
      ),
    );
  }
}

class DropDown extends StatefulWidget {
  const DropDown({
    Key? key,
    required this.edible,
  }) : super(key: key);
  final Edible edible;
  @override
  _DropDownState createState() => _DropDownState();
}

class _DropDownState extends State<DropDown> {
  @override
  Widget build(BuildContext context) {
    var items = widget.edible.optionHolder.options
        .map((e) => DropdownMenuItem<Option>(
              value: e,
              child: OptionDisplay(
                option: e,
              ),
              // onTap: () => setState(() {
              //   widget.edible.optionHolder.setActiveOption(e);
              // }),
            ))
        .toList();
    return Theme(
      data: Theme.of(context).copyWith(canvasColor: Colors.black),
      child: DropdownButton<Option>(
        underline: Text(''),
        isExpanded: true,
        items: items,
        value: widget.edible.activeOption,
        onChanged: (Option? opt) {
          setState(() {
            widget.edible.optionHolder.setActiveOption(opt!);
          });
        },
      ),
    );
  }
}

class OptionDisplay extends StatelessWidget {
  final Option option;
  const OptionDisplay({
    required this.option,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return EdibleItemText(data: '${option.name}/${option.calorie} kcal');
  }
}

class AddQuantityDiscard extends StatefulWidget {
  final void Function() onAddPressed;
  final void Function() onDiscardPressed;
  int quantity;
  AddQuantityDiscard(
      {Key? key,
      required this.onAddPressed,
      required this.onDiscardPressed,
      required this.quantity})
      : super(key: key);

  @override
  _AddQuantityDiscardState createState() => _AddQuantityDiscardState();
}

class _AddQuantityDiscardState extends State<AddQuantityDiscard> {
  Timer? t;
  @override
  void dispose() {
    if (t != null) {
      t!.cancel();
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        GestureDetector(
            onTapDown: (TapDownDetails details) {
              t = Timer.periodic(Duration(milliseconds: 150), (timer) {
                if (widget.quantity > 0) {
                  widget.onDiscardPressed();
                } else {
                  t!.cancel();
                }
              });
            },
            onTapUp: (TapUpDetails details) {
              t!.cancel();
            },
            onTap: widget.onDiscardPressed,
            child: Icon(Icons.remove_circle_outline,
                color: Colors.blue, size: 32)),
        Text(
          '${widget.quantity}',
          style: TextStyle(fontSize: 24, color: Colors.blue),
        ),
        GestureDetector(
            onTapDown: (TapDownDetails details) {
              t = Timer.periodic(Duration(milliseconds: 150), (timer) {
                widget.onAddPressed();
              });
            },
            onTapUp: (TapUpDetails details) {
              t!.cancel();
            },
            onTap: widget.onAddPressed,
            child: Icon(
              Icons.add_circle_outline,
              color: Colors.blue,
              size: 32,
            ))
      ],
    );
  }
}

class EdibleItemText extends StatelessWidget {
  const EdibleItemText({
    Key? key,
    required this.data,
  }) : super(key: key);

  final String data;

  @override
  Widget build(BuildContext context) {
    return Text('$data',
        style: TextStyle(
          color: Colors.blue,
          fontSize: 16,
          locale: Locale.fromSubtags(languageCode: 'tr'),
        ));
  }
}
