import 'package:flutter/material.dart';

class AppBarWithNoSearch extends StatelessWidget {
  const AppBarWithNoSearch({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      color: Colors.blue,
      child: IconButton(
          alignment: Alignment.centerLeft,
          onPressed: () {
            Scaffold.of(context).openDrawer();
          },
          icon: Icon(
            Icons.workspaces_outline,
            size: 16,
          )),
    );
  }
}

class SearchArea extends StatefulWidget {
  final TextEditingController controller;
  final void Function(String) onChanged;
  SearchArea({Key? key, required this.onChanged, required this.controller})
      : super(key: key);

  @override
  _SearchAreaState createState() => _SearchAreaState();
}

class _SearchAreaState extends State<SearchArea> {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.blue,
      child: Row(
        children: [
          IconButton(
            onPressed: () {
              Scaffold.of(context).openDrawer();
            },
            icon: Icon(Icons.workspaces_outline),
            iconSize: 16,
          ),
          Expanded(
            child: Padding(
                padding: EdgeInsets.only(left: 15),
                child: TextField(
                  controller: widget.controller,
                  onChanged: widget.onChanged,
                )),
          ),
        ],
      ),
    );
  }
}

/*
 (String text) => setState(() {
              widget.holder.name = widget.controller.text;
            })
 */

