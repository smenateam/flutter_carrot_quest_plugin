package io.carrotquest.flutter_carrotquest

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import io.carrotquest_sdk.android.Carrot
import io.carrotquest_sdk.android.core.main.CarrotSDK
import io.carrotquest_sdk.android.models.UserProperty
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
//import android.content.pm.PackageManager
//import android.content.ComponentName
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.app.NotificationManager

class FlutterCarrotquestPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var context: Context? = null
    private var activity: Activity? = null

    private var pluginInitted = false

    override fun onAttachedToEngine(
        @NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    ) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_carrotquest")
        channel.setMethodCallHandler(this)

        /// работает только на версиях сдк < 25
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            // Получение имен всех каналов
            // notificationManager!!.notificationChannels.toString()
            // Удаление канала. После этой операции cq не сможет создать канал с таким же именем
            notificationManager?.deleteNotificationChannel("cq_notifications_channel");
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "setup" -> {
                if (pluginInitted) {
                    result.error("Plugin is already initialized.", null, null)
                    return
                }
                val apiKey = call.argument<String?>("api_key")
                val appId = call.argument<String?>("app_id")
                if (apiKey == null || appId == null) {
                    result.error("An error has occurred, the apiKey or appId is null.", null, null)
                    return
                }
                val con = context
                if (con != null) {
                    var res: Boolean? = null
                    Carrot.setup(con, apiKey, appId, object : CarrotSDK.Callback<Boolean> {
                        override fun onFailure(p0: Throwable?) {
                            if (res == null) {
                                res = false
                                result.error(p0!!.localizedMessage, null, null)
                            }
                            pluginInitted = false
                        }

                        override fun onResponse(p0: Boolean?) {
                            pluginInitted = true
                            if (res == null) {
                                res = true
                                result.success(true)
                            }
                        }
                    })
                } else {
                    result.error("Activity in null", null, null)
                }
            }
            "set_debug" -> {
                var isDebug = call.argument<Boolean?>("is_debug")
                if (isDebug == null) isDebug = true
                try {
                    Carrot.setDebug(isDebug)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "auth" -> {
                if (!checkPluginInitiated(result)) return
                val userId = call.argument<String?>("user_id")
                val userAuthKey = call.argument<String?>("user_auth_key")
                if (userId == null || userAuthKey == null) {
                    result.error(
                        "An error has occurred, the userId or userAuthKey is null.",
                        null,
                        null
                    )
                    return
                }
                var res: Boolean? = null
                Carrot.auth(userId, userAuthKey, object : CarrotSDK.Callback<Boolean> {
                    override fun onFailure(p0: Throwable?) {
                        if (res != null) {
                            res = false
                            result.error(p0!!.localizedMessage, null, null)
                        }
                    }

                    override fun onResponse(p0: Boolean?) {
                        if (res != null) {
                            res = p0
                            result.success(p0)
                        }
                    }
                })
            }
            "de_init" -> {
                if (!checkPluginInitiated(result)) return
                var res: Boolean? = null
                Carrot.deInit(object : CarrotSDK.Callback<Boolean> {
                    override fun onFailure(p0: Throwable?) {
                        pluginInitted = false
                        if (res != null) {
                            res = false
                            result.error(p0!!.localizedMessage, null, null)
                        }
                    }

                    override fun onResponse(p0: Boolean?) {
                        pluginInitted = false
                        if (res != null) {
                            res = p0
                            result.success(p0)
                        }
                    }
                })
            }
            "open_chat" -> {
                if (!checkPluginInitiated(result)) return
                try {
                    if (activity != null) {
                        Carrot.openChat(activity)
                        result.success(null)
                    } else {
                        result.error("Activity in null", null, null)
                    }
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "send_firebase_notification" -> {
                if (!checkPluginInitiated(result)) return
                val remoteMessage = call.argument<Any?>("remote_message")
                if (remoteMessage == null) {
                    result.error("An error has occurred, the remoteMessage is null.", null, null)
                    return
                }
                try {
                    //Carrot.sendFirebasePushNotification(remoteMessage)
                    //result.success(null)
                    result.notImplemented()
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "set_token" -> {
                val token = call.argument<String>("fcm_token")
                Carrot.sendFcmToken(token);
                result.success(true)
            }
            "set_user_property" -> {
                if (!checkPluginInitiated(result)) return
                val userProperties = call.argument<Map<String, String>?>("user_property")
                if (userProperties == null || userProperties.isEmpty()) {
                    result.error(
                        "An error has occurred, the userProperty is null or empty.",
                        null,
                        null
                    )
                    return
                }
                try {
                    val list: ArrayList<UserProperty> = arrayListOf()
                    for (key in userProperties.keys) {
                        val property = UserProperty(key, userProperties[key])
                        list.add(property)
                    }
                    Carrot.setUserProperty(list)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }

            }
            "track_event" -> {
                if (!checkPluginInitiated(result)) return
                val eventName = call.argument<String?>("event_name")
                val eventParams = call.argument<String?>("event_params")
                if (eventName == null) {
                    result.error("An error has occurred, the event_name is null.", null, null)
                    return
                }
                try {
                    if (eventParams != null)
                        Carrot.trackEvent(eventName, eventParams)
                    else
                        Carrot.trackEvent(eventName)
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

    private fun checkPluginInitiated(@NonNull result: Result): Boolean {
        if (!pluginInitted) {
            result.error(
                "The plugin hasn't been initialized yet. Do Carrot.setup(...) first .",
                null,
                null
            )
            return false
        }
        return true
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        pluginInitted = false
        context = null
    }

    override fun onDetachedFromActivity() {
        pluginInitted = false
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }
}
