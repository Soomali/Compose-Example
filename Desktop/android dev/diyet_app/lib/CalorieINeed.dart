import 'package:diyet_app/storageManager.dart';
import 'package:flutter/material.dart';
import 'CustomDrawer.dart';
import 'Searchbar.dart';

//Kalori Egzersizleri:
//az: 100
//biraz: 150
//orta:200
//yoğun:250
//çok yoğun:300

//kalori_alt_limit: %15
enum Exercise {
  little,
  smedium,
  medium,
  intense,
  soIntense,
}
int exerciseToKcalNeed(Exercise e) {
  switch (e) {
    case Exercise.little:
      return 100;
    case Exercise.smedium:
      return 450;
    case Exercise.medium:
      return 800;
    case Exercise.intense:
      return 1150;
    case Exercise.soIntense:
      return 1500;
  }
}

String exerciseToString(Exercise e) {
  switch (e) {
    case Exercise.little:
      return 'Az';
    case Exercise.smedium:
      return 'Biraz';
    case Exercise.medium:
      return 'Orta';
    case Exercise.intense:
      return 'Fazla';
    case Exercise.soIntense:
      return 'Çok Fazla';
  }
}

double calculateCalorieNeed(
    double weight, double height, double age, bool isWoman, Exercise exercise) {
  int exerciseCal;
  switch (exercise) {
    case Exercise.little:
      exerciseCal = 100;
      break;
    case Exercise.smedium:
      exerciseCal = 150;
      break;
    case Exercise.medium:
      exerciseCal = 200;
      break;
    case Exercise.intense:
      exerciseCal = 250;
      break;
    case Exercise.soIntense:
      exerciseCal = 300;
      break;
  }
  return exerciseCal +
      (isWoman
          ? 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age)
          : 66 + (13.7 * weight) + (5 * height) - (6.8 * age));
}

class IndexHolder {
  int index;
  IndexHolder(this.index);
}

class CalorieNeedCalculatePage extends StatelessWidget {
  const CalorieNeedCalculatePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        drawer: CustomDrawer(),
        body: CalorieParameterHolder(),
      ),
    );
  }
}

class CalorieParameterHolder extends StatefulWidget {
  const CalorieParameterHolder({Key? key}) : super(key: key);

  @override
  _CalorieParameterHolderState createState() => _CalorieParameterHolderState();
}

class _CalorieParameterHolderState extends State<CalorieParameterHolder>
    with WidgetsBindingObserver {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance!.addObserver(this);
  }

  @override
  void dispose() {
    WidgetsBinding.instance!.removeObserver(this);
    super.dispose();
  }

  @override
  void didChangeMetrics() {
    final bottomInset = WidgetsBinding.instance!.window.viewInsets.bottom;
    final newValue = bottomInset > 0.0;

    if (newValue != _isKeyboardVisible) {
      setState(() {
        _isKeyboardVisible = newValue;
      });
    }
  }

  final TextEditingController weightcontrol = TextEditingController();
  final indexHolder = IndexHolder(0);
  final TextEditingController agecontrol = TextEditingController();
  final TextEditingController heighcontrol = TextEditingController();
  var _isKeyboardVisible = false;
  var _stayOpen = false;
  GlobalKey<PopupMenuButtonState> _menukey = GlobalKey();

  final TextStyle _dropdownStyle = TextStyle(color: Colors.blue);
  Exercise? _selected;
  @override
  Widget build(BuildContext context) {
    var condt = _isKeyboardVisible || _stayOpen;
    return Column(
      children: [
        AppBarWithNoSearch(),
        condt ? Text('') : CurrentNeedWidget(),
        Flexible(
          child: Container(
            margin: EdgeInsets.symmetric(horizontal: 18),
            padding: EdgeInsets.all(8),
            decoration: BoxDecoration(
                color: Colors.black,
                boxShadow: [
                  BoxShadow(
                      color: Colors.blue.shade900,
                      offset: Offset(0, 1),
                      blurRadius: 9.0)
                ],
                borderRadius: BorderRadius.vertical(top: Radius.circular(60))),
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    DataWidget(hint: 'Yaş', controller: agecontrol),
                    DataWidget(hint: 'kilo (Kg)', controller: weightcontrol),
                  ],
                ),
                Align(
                  alignment: Alignment.center,
                  child: DataWidget(hint: 'boy (cm)', controller: heighcontrol),
                ),
                Padding(
                  padding: EdgeInsets.only(
                      left: MediaQuery.of(context).size.width * 0.17),
                  child: PopupMenuButton<Exercise>(
                      key: _menukey,
                      child: Row(
                        children: [
                          Text(
                            'Egzersiz \n Düzeyi',
                            style: TextStyle(color: Colors.blue, fontSize: 12),
                          ),
                          OutlinedButton.icon(
                            style: OutlinedButton.styleFrom(
                                fixedSize: Size(
                                    MediaQuery.of(context).size.width * 0.32,
                                    40),
                                shape: RoundedRectangleBorder(),
                                side: BorderSide(color: Colors.blue)),
                            icon: Icon(Icons.arrow_drop_down),
                            label: Text(
                              _selected == null
                                  ? 'Az'
                                  : exerciseToString(_selected!),
                              style:
                                  TextStyle(color: Colors.blue, fontSize: 16),
                            ),
                            onPressed: () {
                              setState(() {
                                if (_isKeyboardVisible) {
                                  _stayOpen = true;
                                }
                              });
                              _menukey.currentState!.showButtonMenu();
                            },
                          ),
                        ],
                      ),
                      color: Colors.black,
                      initialValue: _selected,
                      onSelected: (Exercise? e) {
                        setState(() {
                          _stayOpen = false;
                          _selected = e;
                        });
                      },
                      onCanceled: () {
                        setState(() {
                          _stayOpen = false;
                        });
                      },
                      iconSize: 5,
                      itemBuilder: (context) => <PopupMenuItem<Exercise>>[
                            PopupMenuItem(
                                value: Exercise.little,
                                child: Text(
                                  'Az',
                                  style: _dropdownStyle,
                                )),
                            PopupMenuItem(
                                value: Exercise.smedium,
                                child: Text(
                                  'Biraz',
                                  style: _dropdownStyle,
                                )),
                            PopupMenuItem(
                                value: Exercise.medium,
                                child: Text(
                                  'Orta',
                                  style: _dropdownStyle,
                                )),
                            PopupMenuItem(
                                value: Exercise.intense,
                                child: Text(
                                  'Fazla',
                                  style: _dropdownStyle,
                                )),
                            PopupMenuItem(
                                value: Exercise.soIntense,
                                child: Text(
                                  'Çok Fazla',
                                  style: _dropdownStyle,
                                )),
                          ]),
                ),
                Expanded(
                  child: RadioButton(holder: indexHolder, widgetData: [
                    RadioButtonElementData(
                        text: 'Kadın',
                        isOn: true,
                        padding: EdgeInsets.only(
                            left: MediaQuery.of(context).size.width * 0.13)),
                    RadioButtonElementData(
                        text: 'Erkek',
                        isOn: false,
                        padding: EdgeInsets.only(
                            left: MediaQuery.of(context).size.width * 0.15))
                  ]),
                ),
                TextButton.icon(
                    onPressed: () {
                      double calorieNeed = 0;
                      try {
                        var weight = double.parse(weightcontrol.text);
                        var height = double.parse(heighcontrol.text);
                        var age = double.parse(agecontrol.text);
                        //Kadınlarda: 655 + 9,6 X (kg cinsinden ağırlık) + 1,8 X (santim cinsinden boy) - 4,7 X (yaş)
                        //Erkeklerde: 66 + 13,7 X (kg cinsinden ağırlık) + 5 X (santim cinsinden boy) - 6,8 X (yaş)

                        switch (indexHolder.index) {
                          case 0:
                            calorieNeed = 655 +
                                (9.6 * weight) +
                                (1.8 * height) -
                                (4.7 * age);
                            break;
                          case 1:
                            calorieNeed = 66 +
                                (13.7 * weight) +
                                (5 * height) -
                                (6.8 * age);
                            break;
                        }
                        setState(() {
                          PreferencesManager().userCalorieNeed = calorieNeed +
                              exerciseToKcalNeed(_selected ?? Exercise.little);
                          FocusScope.of(context).unfocus();
                        });
                      } catch (e) {
                        print(e);
                      }
                    },
                    icon: Icon(Icons.calculate_outlined,
                        color: Colors.blue, size: 50),
                    label: Text(
                      'Hesapla',
                      style: TextStyle(color: Colors.blue, fontSize: 35),
                    ))
              ],
            ),
          ),
        )
      ],
    );
  }
}

class CurrentNeedWidget extends StatelessWidget {
  const CurrentNeedWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var manager = PreferencesManager();
    return SizedBox(
        height: MediaQuery.of(context).size.height * 0.45,
        child: Align(
          alignment: Alignment.center,
          child: Text(
            '${manager.userCalorieNeed} kcal',
            style: TextStyle(fontSize: 40, color: Colors.blue),
          ),
        ));
  }
}

class DataWidget extends StatelessWidget {
  final TextEditingController controller;
  final String hint;
  const DataWidget({Key? key, required this.hint, required this.controller})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width * 0.2,
      child: TextField(
        keyboardType: TextInputType.number,
        controller: controller,
        maxLines: 1,
        maxLength: 3,
        decoration: InputDecoration(
            hintText: hint,
            hintStyle: TextStyle(color: Colors.blue[200]),
            enabledBorder:
                OutlineInputBorder(borderSide: BorderSide(color: Colors.blue))),
        style: TextStyle(fontSize: 16, color: Colors.blue),
      ),
    );
  }
}

class RadioButton extends StatefulWidget {
  final IndexHolder holder;
  final List<RadioButtonElementData> widgetData;
  const RadioButton({Key? key, required this.widgetData, required this.holder})
      : super(key: key);

  @override
  _RadioButtonState createState() => _RadioButtonState();
}

class _RadioButtonState extends State<RadioButton> {
  int activeIndex = 0;
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
        scrollDirection: Axis.horizontal,
        physics: NeverScrollableScrollPhysics(),
        itemCount: widget.widgetData.length,
        itemBuilder: (context, index) {
          return GestureDetector(
              onTap: () {
                setState(() {
                  widget.widgetData[activeIndex].isOn = false;
                  widget.widgetData[index].isOn = true;
                  activeIndex = index;
                  widget.holder.index = index;
                });
              },
              child: widget.widgetData[index].element);
        });
  }
}

class RadioButtonElementData {
  final String text;
  bool isOn;
  final TextStyle? style;
  final double? height;
  final EdgeInsetsGeometry? padding;
  RadioButtonElementData(
      {this.height,
      this.padding,
      required this.text,
      required this.isOn,
      this.style});
  Widget get element {
    Widget widget = RadioButtonElement(
      text: text,
      isOn: isOn,
      height: height,
      style: style,
    );
    if (padding != null) {
      widget = Padding(
        padding: padding!,
        child: widget,
      );
    }
    return widget;
  }
}

class RadioButtonElement extends StatelessWidget {
  final String text;
  final bool isOn;
  final TextStyle? style;
  final double? height;
  const RadioButtonElement(
      {Key? key,
      this.height,
      required this.text,
      required this.isOn,
      this.style})
      : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Container(
          margin: const EdgeInsets.all(8),
          padding: const EdgeInsets.all(3),
          width: MediaQuery.of(context).size.width * 0.05,
          height: height ?? MediaQuery.of(context).size.height * 0.027,
          decoration: BoxDecoration(
            border: Border.all(color: Colors.blue),
            borderRadius: BorderRadius.circular(40),
          ),
          child: isOn
              ? Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(40),
                    border: Border.all(color: Colors.black),
                    color: Colors.blue,
                  ),
                )
              : null,
        ),
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(
            text,
            style: style ?? TextStyle(fontSize: 16, color: Colors.blue),
          ),
        )
      ],
    );
  }
}
