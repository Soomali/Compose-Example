import 'package:flutter/material.dart';

class LoaderPage extends PopupRoute {
  LoaderPage();
  @override
  Color? get barrierColor => Colors.black45;

  @override
  bool get barrierDismissible => false;

  @override
  String? get barrierLabel => null;

  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) {
    return ColoredBox(
      color: Colors.transparent,
      child: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }

  @override
  Duration get transitionDuration => Duration(milliseconds: 250);
}
