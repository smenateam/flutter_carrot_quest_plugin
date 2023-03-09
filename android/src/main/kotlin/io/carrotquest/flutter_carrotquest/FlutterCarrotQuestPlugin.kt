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
import android.app.NotificationChannel
import android.content.Intent
import io.carrotquest_sdk.android.presentation.mvp.dialog.view.DialogActivity

class FlutterCarrotquestPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var context: Context? = null
    private var activity: Activity? = null

    override fun onAttachedToEngine(
        @NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding
    ) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_carrotquest")
        channel.setMethodCallHandler(this)

        /// работает только на версиях сдк < 25
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            // Получение имен всех каналов
            // notificationManager!!.notificationChannels.toString()
            // Удаление канала. После этой операции cq не сможет создать канал с таким же именем
            // notificationManager?.deleteNotificationChannel("cq_notifications_channel");

            // обман cq. Мы сами создаем для них канал и отключаем его каждый запуск приложения
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    "cq_notifications_channel",
                    "Чаты",
                    NotificationManager.IMPORTANCE_NONE
                )
            )
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "setup" -> {
                if (Carrot.isInit()) {
                    result.error("Plugin is already initialized.", null, null)
                    return
                }
                val apiKey = call.argument<String?>("api_key")
                val appId = call.argument<String?>("app_id")
                if (apiKey == null || appId == null) {
                    result.error("An error has occurred, the apiKey or appId is null.", null, null)
                    return
                }
                if (context != null) {
                    var res: Boolean? = null
                    Carrot.setup(context!!, apiKey, appId, object : CarrotSDK.Callback<Boolean> {
                        override fun onFailure(p0: Throwable?) {
                            if (res == null) {
                                res = Carrot.isInit()
                                result.error(p0!!.localizedMessage, null, null)
                            }
                        }

                        override fun onResponse(p0: Boolean?) {
                            if (res == null) {
                                res = Carrot.isInit()
                                result.success(res)
                            }
                        }
                    })
                } else {
                    result.error("Activity in null", null, null)
                }
            }
            "set_debug" -> {
                val isDebug = call.argument<Boolean>("is_debug")
                try {
                    Carrot.setDebug(isDebug!!)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "auth" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }
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
                        if (res == null) {
                            res = false
                            result.error(p0!!.localizedMessage, null, null)
                        }
                    }

                    override fun onResponse(p0: Boolean?) {
                        if (res == null) {
                            res = p0
                            result.success(p0)
                        }
                    }
                })
            }
            "de_init" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }
                var res: Boolean? = null
                Carrot.deInit(object : CarrotSDK.Callback<Boolean> {
                    override fun onFailure(p0: Throwable?) {
                        if (res == null) {
                            res = Carrot.isInit()
                            result.error(p0!!.localizedMessage, null, null)
                        }
                    }

                    override fun onResponse(p0: Boolean?) {
                        if (res == null) {
                            res = Carrot.isInit()
                            result.success(p0)
                        }
                    }
                })
            }
            "open_chat" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }
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
            "open_chat_by_id" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }

                try {
                    val id = call.argument<String>("id")
                    val intent = Intent(activity, DialogActivity::class.java)
                    intent.putExtra("CONVERSATION_ID_ARG", "$id")
                    activity?.startActivity(intent)
                    result.success(null)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "send_firebase_notification" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                }
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
                try {
                    Carrot.sendFcmToken(token)
                    result.success(true)
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            "set_user_property" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }
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
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                }
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
            "get_unread_conversations" -> {
                if (!Carrot.isInit()) {
                    result.error("Plugin is not initialized.", null, null)
                    return
                }
                try {
                    result.success(Carrot.getUnreadConversations())
                } catch (e: Exception) {
                    result.error(e.localizedMessage, null, null)
                }
            }
            else -> {
                result.notImplemented()
            }
        }
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        context = null
    }

    override fun onDetachedFromActivity() {
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
