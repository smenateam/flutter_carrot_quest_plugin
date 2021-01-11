import 'dart:async';

import 'package:flutter/services.dart';

class Carrot {
  static const MethodChannel _channel =
      const MethodChannel('flutter_carrotquest');

  static Future<void> setup(
      {required String apiKey, required String appId}) async {
    return _channel.invokeMethod('setup', {'api_key': apiKey, 'app_id': appId});
  }

  static Future<void> openChat() async {
    return _channel.invokeMethod('open_chat');
  }
}
