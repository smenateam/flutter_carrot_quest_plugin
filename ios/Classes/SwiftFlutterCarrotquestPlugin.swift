import Flutter
import UIKit
import CarrotSDK

public class SwiftFlutterCarrotquestPlugin: NSObject, FlutterPlugin {

    let pluginInited = false;
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_carrotquest", binaryMessenger: registrar.messenger())
        let instance = SwiftFlutterCarrotquestPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        let arguments = call.arguments as? NSDictionary
        if(call.method.elementsEqual("setup")) {
            if (pluginInited) {
                result(
                    FlutterError(code: "Plugin is already initialized.",
                        message: nil, details: nil))
                return
            }
            guard let apiKey = arguments?["api_key"] as? String else {
                result(
                    FlutterError(code: "An error has occurred, the apiKey or appId is null.",
                        message: nil,
                        details: nil))
                return
            }
            Carrot.shared.setup(
                withApiKey: apiKey,
                successHandler: {
                    result(nil)
                },
                errorHandler: { error in
                    result(
                        FlutterError(code: error,
                            message: nil,
                            details: nil))
                })
            return
        } else if(call.method.elementsEqual("set_debug")) {
            result(FlutterMethodNotImplemented)
            return
        } else if(call.method.elementsEqual("auth")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            guard let userAuthKey = arguments?["user_auth_key"] as? String else {
                result(
                    FlutterError(code: "An error has occurred, the userId or userAuthKey is null.",
                        message: nil,
                        details: nil))
                return
            }
            guard let userId = arguments?["user_id"] as? String else {
                result(
                    FlutterError(code: "An error has occurred, the userId or userAuthKey is null.",
                        message: nil,
                        details: nil))
                return
            }
            Carrot.shared.auth(
                withUserId: userId,
                withUserAuthKey: userAuthKey,
                successHandler: {
                    result(nil)
                },
                errorHandler: { error in
                    result(
                        FlutterError(code: error,
                            message: nil,
                            details: nil))
                })
            return
        }
        else if(call.method.elementsEqual("de_init")) {
            if(!checkPlugininnited(result: result)) {
            return
            }
                Carrot.shared.logout(
                    successHandler: {
                                        result(nil)
                                    },
                     errorHandler: { error in
                                        result(
                                            FlutterError(code: error,
                                                message: nil,
                                                details: nil))
                                    })

            return
            }
        } else if(call.method.elementsEqual("open_chat")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            Carrot.shared.openChat()
            result(nil)
            return
        } else if(call.method.elementsEqual("send_firebase_notification")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            result(FlutterMethodNotImplemented)
            return
        } else if(call.method.elementsEqual("set_token")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            guard let fcmToken = arguments?["fcm_token"] as? String else {
                result(
                    FlutterError(code: "An error has occurred, the fcmToken is null.",
                        message: nil,
                        details: nil))
                return
            }
            CarrotNotificationService.shared.setToken(fcmToken)
            return
        } else if(call.method.elementsEqual("set_user_property")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            guard let userProperties = arguments?["user_property"] as? Dictionary<String, String> else {
                result(
                    FlutterError(code: "An error has occurred, the userProperty is null.",
                        message: nil,
                        details: nil))
                return
            }
            var list = [UserProperty]()
            for (name, path) in userProperties {
                let item = UserProperty(key: name, value: path)
                list.append(item)
            }
            Carrot.shared.setUserProperty(list)
            return
        } else if(call.method.elementsEqual("track_event")) {
            if(!checkPlugininnited(result: result)) {
                return
            }
            guard let eventName = arguments?["event_name"] as? String else {
                result(
                    FlutterError(code: "An error has occurred, the eventName is null.",
                        message: nil,
                        details: nil))
                return
            }
            let eventParams = arguments?["event_params"] as? String
            Carrot.shared.trackEvent(withName: eventName, withParams: eventParams ?? "")
            return
        } else {
            result(FlutterMethodNotImplemented)
            return
        }
    }

    private func checkPlugininnited(result: @escaping FlutterResult) -> Bool {
        if(pluginInited) {
            result(
                FlutterError(code: "The plugin hasn't been initialized yet. Do Carrot.setup(...) first.",
                    message: nil, details: nil))
            return false
        }
        return true
    }
}
