import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_carrotquest/flutter_carrotquest.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    await Carrot.setup(apiKey: 'your apiKey', appId: 'your appId')
        .catchError((onError) => print(onError));
    await Carrot.setDebug();
    await Carrot.auth(userId: 'your userId', userAuthKey: 'your userAuthKey');
    await Carrot.openChat();
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(''),
        ),
      ),
    );
  }
}
