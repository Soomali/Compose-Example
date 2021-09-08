import 'package:diyet_app/CalorieINeed.dart';
import 'package:diyet_app/MenuDisplayItem.dart';
import 'EdibleDisplayItem.dart';
import 'package:flutter/material.dart';

const TextStyle _style = TextStyle(fontSize: 20, color: Colors.blue);

void toPage(Widget page, BuildContext context) {
  Navigator.of(context).pushReplacement(
      MaterialPageRoute(builder: (BuildContext context) => page));
}

class CustomDrawer extends StatelessWidget {
  const CustomDrawer({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Drawer(
        child: Container(
      color: Colors.black,
      child: ListView(
        physics: NeverScrollableScrollPhysics(),
        children: [
          Container(
            height: MediaQuery.of(context).size.height * 0.4,
            child: Container(
              margin: EdgeInsets.fromLTRB(20, 20, 20, 20),
              alignment: Alignment.center,
              decoration: BoxDecoration(
                border: Border.all(color: Colors.blue),
                borderRadius: BorderRadius.circular(550),
                image: DecorationImage(
                    image: AssetImage('assets/poytik.jpeg'),
                    fit: BoxFit.fitWidth),
              ),
            ),
          ),
          DrawerItem(
            child: CustomListTile(
                onTap: () => print('ToTodaaay!'),
                text: 'Günlük Kalori',
                icon: Icons.show_chart),
          ),
          DrawerItem(
            child: CustomListTile(
                onTap: () => toPage(MenuPage(), context),
                text: 'Menüler',
                icon: Icons.menu_book_sharp),
          ),
          DrawerItem(
            child: CustomListTile(
              onTap: () => toPage(EdiblePage(), context),
              text: 'Yiyecekler',
              icon: Icons.food_bank_outlined,
            ),
          ),
          DrawerItem(
            child: CustomListTile(
                onTap: () => toPage(CalorieNeedCalculatePage(), context),
                text: 'Günlük Kalori İhtiyacı Hesapla',
                icon: Icons.calculate_outlined),
          )
        ],
      ),
    ));
  }
}

class DrawerItem extends StatelessWidget {
  final CustomListTile child;
  const DrawerItem({
    required this.child,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          border: Border(
              bottom: BorderSide(color: Colors.blue),
              top: BorderSide(color: Colors.blue))),
      child: child,
    );
  }
}

class CustomListTile extends StatelessWidget {
  final void Function() onTap;
  final String text;
  final IconData icon;
  const CustomListTile(
      {Key? key, required this.onTap, required this.text, required this.icon})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.black,
      child: InkWell(
        onTap: onTap,
        splashColor: Colors.red,
        child: ListTile(
          leading: Icon(
            icon,
            color: Colors.blue,
            size: 38,
          ),
          title: Text(
            text,
            style: _style,
          ),
          shape: Border(bottom: BorderSide(color: Colors.blue)),
        ),
      ),
    );
  }
}
