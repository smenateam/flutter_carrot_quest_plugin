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
                val apiKey = call.argument<String>("app_key")
                val appId = call.argument<String>("app_id")
                if (apiKey == null || appId == null) {
                    result.error("An error has occurred, the apiKey or appId is null.", null, null)
                    return
                }
                Carrot.setup(context, apiKey, appId, object : CarrotSDK.Callback<Boolean> {
                    override fun onFailure(p0: Throwable?) {
                        result.error(p0.toString(), null, null)
                        pluginInited = false
                    }

                    override fun onResponse(p0: Boolean?) {
                        result.success(null)
                        pluginInited = true
                    }
                })
            }
            "open_chat" -> {
                if (!pluginInited) {
                    result.error("The plugin hasn't been initialized yet. Do Carrot.setup(...) first .", null, null)
                    return
                }
                Carrot.openChat(context)
                result.success(null)
            }
            else -> {
                result.notImplemented()
            }
        }
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
