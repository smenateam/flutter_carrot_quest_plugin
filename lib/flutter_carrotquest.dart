import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class Carrot {
  static const MethodChannel _channel =
      const MethodChannel('flutter_carrotquest');

  static Future<void> setup(
      {@required String apiKey, @required String appId}) async {
    return _channel
        .invokeMethod<void>('setup', {'api_key': apiKey, 'app_id': appId});
  }

  static Future<void> setDebug({bool isDebug = true}) async {
    return _channel.invokeMethod<void>('set_debug', {'is_debug': isDebug});
  }

  static Future<bool> auth(
      {@required String userId, @required String userAuthKey}) async {
    return _channel.invokeMethod<bool>(
        'auth', {'user_id': userId, 'user_auth_key': userAuthKey});
  }

  static Future<void> deinit() async {
    return _channel.invokeMethod<void>('de_init');
  }

  static Future<void> openChat() async {
    return _channel.invokeMethod('open_chat');
  }
}
