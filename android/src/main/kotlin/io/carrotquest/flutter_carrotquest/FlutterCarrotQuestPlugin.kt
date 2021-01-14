package io.carrotquest.flutter_carrotquest

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import io.carrotquest_sdk.android.Carrot
import io.carrotquest_sdk.android.core.main.CarrotSDK
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class FlutterCarrotQuestPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    private var pluginInited = false

    override fun onAttachedToEngine(
            @NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    ) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_carrotquest")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "setup" -> {
                if (pluginInited) {
                    result.error("Plugin is already initialized.", null, null)
                    return
                }
                val apiKey = call.argument<String>("api_key")
                val appId = call.argument<String>("app_id")
                if (apiKey == null || appId == null) {
                    result.error("An error has occurred, the apiKey or appId is null.", null, null)
                    return
                }
                Carrot.setup(context, apiKey, appId, object : CarrotSDK.Callback<Boolean> {
                    override fun onFailure(p0: Throwable?) {
                        pluginInited = false
                        result.error(p0.toString(), null, null)
                    }

                    override fun onResponse(p0: Boolean?) {
                        pluginInited = true
                        result.success(null)
                    }
                })
            }
            "set_debug" -> {
                var isDebug = call.argument<Boolean>("is_debug")
                if (isDebug == null) isDebug = true
                try {
                    Carrot.setDebug(isDebug)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "auth" -> {
                if (!checkPlugininnited(result)) return
                val userId = call.argument<String>("user_id")
                val userAuthKey = call.argument<String>("user_auth_key")
                if (userId == null || userAuthKey == null) {
                    result.error("An error has occurred, the userId or userAuthKey is null.", null, null)
                    return
                }
                Carrot.auth(userId, userAuthKey, object : CarrotSDK.Callback<Boolean> {
                    override fun onResponse(p0: Boolean?) {
                        result.success(p0)
                    }

                    override fun onFailure(p0: Throwable?) {
                        result.error(p0?.localizedMessage, null, null)
                    }
                })
            }
            "de_init" -> {
                if (!checkPlugininnited(result)) return
                try {
                    Carrot.deInit()
                    pluginInited = false
                    result.success(null)
                } catch (e: Exception) {
                    pluginInited = false
                    result.error(e.localizedMessage, null, null)
                }
            }
            "open_chat" -> {
               if (!checkPlugininnited(result)) return
                try {
                    Carrot.openChat(activity)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            else -> {
                result.notImplemented()
            }
        }


       }

    private fun checkPlugininnited(@NonNull result: Result): Boolean{
        if (!pluginInited) {
            result.error("The plugin hasn't been initialized yet. Do Carrot.setup(...) first .", null, null)
            return false
        }
        return true
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity;
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }
}
