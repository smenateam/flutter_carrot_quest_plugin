import 'dart:async';

import 'package:flutter/services.dart';

class Carrot {
  static const MethodChannel _channel =
      const MethodChannel('flutter_carrotquest');

  // Plugin initialization.
  /// This must be done before using any other methods of this plugin.
  /// You can use this in main.dart before launch runApp(...)
  static Future<void> setup(
      {required String apiKey, required String appId}) async {
    return _channel
        .invokeMethod<void>('setup', {'api_key': apiKey, 'app_id': appId});
  }

  // Note: Only for Android
  /// Launching the debug mode of native SDK
  static Future<void> setDebug({bool isDebug = true}) async {
    return _channel.invokeMethod<void>('set_debug', {'is_debug': isDebug});
  }

  // User authorization
  static Future<bool?> auth(
      {required String userId, required String userAuthKey}) async {
    return _channel.invokeMethod<bool?>(
        'auth', {'user_id': userId, 'user_auth_key': userAuthKey});
  }

  // Note: Only for Android
  // Unauthorized user.
  /// After executing this method, you must initialize the plugin again.
  static Future<void> deinit() async {
    return _channel.invokeMethod<void>('de_init');
  }

  // Opening a native chat window
  static Future<void> openChat() async {
    return _channel.invokeMethod('open_chat');
  }
}
