import 'package:flutter/material.dart';
import 'storageManager.dart';
import 'MenuAdd.dart';
import 'TodaysMenu.dart';

class Basket extends StatefulWidget {
  const Basket({
    Key? key,
  }) : super(key: key);

  @override
  _BasketState createState() => _BasketState();
}

class _BasketState extends State<Basket> {
  var manager = StorageManager();
  late StorageManagerListener _edibleListener;
  late StorageManagerListener _menuCountListener;
  @override
  void initState() {
    _menuCountListener =
        StorageManagerListener(onChanged: () => setState(() {}), mod: 1);
    _edibleListener = StorageManagerListener(onChanged: () {
      setState(() {});
    });
    manager.addListener(_menuCountListener);
    manager.addListener(_edibleListener);
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
    _edibleListener.dispose();
    _menuCountListener.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Padding(
          padding: const EdgeInsets.only(left: 26),
          child: BasketHolder(
              count: _menuCountListener.count!,
              icon: Icons.menu_book_outlined,
              onPressed: () {
                Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => TodaysMenuAddPage()));
              }),
        ),
        BasketHolder(
          icon: Icons.shopping_bag_rounded,
          count: _edibleListener.current!.edibles.keys.length,
          onPressed: () => Navigator.of(context).pushReplacement(
              MaterialPageRoute(builder: (context) => MenuAddPage())),
        ),
      ],
    );
  }
}

class BasketHolder extends StatelessWidget {
  final int count;
  final IconData icon;
  final void Function() onPressed;
  const BasketHolder({
    Key? key,
    required this.count,
    required this.icon,
    required this.onPressed,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          width: 80,
          height: 80,
          decoration: BoxDecoration(
              color: Colors.black,
              border: Border.all(color: Colors.blue[800]!),
              borderRadius: BorderRadius.circular(40)),
          child: IconButton(
              padding: EdgeInsets.zero,
              onPressed: onPressed,
              icon: Icon(
                icon,
                color: Colors.blue,
                size: 50,
              )),
        ),
        Positioned(
            right: -3.0,
            bottom: 0.0,
            child: Container(
              decoration: BoxDecoration(
                  border: Border.all(color: Colors.black),
                  borderRadius: BorderRadius.circular(80)),
              child: Text(
                '$count',
                style: TextStyle(fontSize: 35, color: Colors.blue),
              ),
            ))
      ],
    );
  }
}
