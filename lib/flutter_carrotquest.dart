import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class Carrot {
  static const MethodChannel _channel = const MethodChannel('flutter_carrotquest');

  // Plugin initialization.
  /// This must be done before using any other methods of this plugin.
  /// You can use this in main.dart before launch runApp(...)
  static Future<void> setup({required String apiKey, required String appId}) async {
    return await _channel.invokeMethod<void>('setup', {'api_key': apiKey, 'app_id': appId});
  }

  // Note: Only for Android
  /// Launching the debug mode of native SDK
  static Future<void> setDebug({bool isDebug = true}) async {
    if (kIsWeb || !Platform.isAndroid) throw Exception('Only for Android');
    return _channel.invokeMethod<void>('set_debug', {'is_debug': isDebug});
  }

  // User authorization
  static Future<bool?> auth({required String userId, required String userAuthKey}) async {
    return _channel.invokeMethod<bool?>('auth', {'user_id': userId, 'user_auth_key': userAuthKey});
  }

  // Note: Only for Android
  // Unauthorized user.
  /// After executing this method, you must initialize the plugin again.
  static Future<void> deinit() async {
    return await _channel.invokeMethod<void>('de_init');
  }

  // Opening a native chat window
  static Future<void> openChat() async {
    return _channel.invokeMethod('open_chat');
  }

  // Note: Only for Android
  // Send Firebase Messages to Carrot
  static Future<void> sendFirebaseNotification(dynamic remoteMessage) async {
    if (!kIsWeb || !Platform.isAndroid) throw Exception('Only for Android');
    return _channel.invokeMethod('send_firebase_notification', {'remote_message': remoteMessage});
  }

  // Note: Only for IOS
  // Send Firebase Cloud Messages Token to Carrot
  static Future<void> setToken(String fcmToken) async {
    return await _channel.invokeMethod('set_token', {'fcm_token': fcmToken});
  }

  /// удаление токена для ios и только
  static Future<void> deleteToken() async {
    if (Platform.isIOS) {
      return await _channel.invokeMethod('delete_token');
    }
    return;
  }

  // Set user properties
  static Future<void> setUserProperty(Map<String, String> userProperty) async {
    return _channel.invokeMethod('set_user_property', {'user_property': userProperty});
  }

  // Track user event
  static Future<void> trackEvent(String eventName, {String? eventParams}) async {
    return _channel.invokeMethod('track_event', {'event_name': eventName, 'event_params': eventParams});
  }

  // Track user event
  static Future<void> openChatById(String id) async {
    return await _channel.invokeMethod('open_chat_by_id', {'id': id});
  }

  // Track user event
  static Future<bool> isInit() async {
    return await _channel.invokeMethod('is_init');
  }
}
