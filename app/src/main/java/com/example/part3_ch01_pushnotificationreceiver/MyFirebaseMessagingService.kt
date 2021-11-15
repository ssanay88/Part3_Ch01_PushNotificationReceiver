package com.example.part3_ch01_pushnotificationreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // FCM을 사용하는 앱은 onNewToken을 오버라이딩 해야 한다.
    // 토큰들은 상황에 따라 언제든지 변경 가능하다. 따라서 상황에 맞게 토큰을 가져올 수 있어야 한다. 토큰이 갱신될 때 마다 불러온다.
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    // Firebase Cloud Messaging에서 메세지를 수신할 때 마다 아래 함수를 불러옵니다.
    // 알림 메세지 X , 데이터 메세지 O : 따로 처리가 필요하다
    override fun onMessageReceived(receiveMessage : RemoteMessage) {
        // break Point
        super.onMessageReceived(receiveMessage)

        // 메세지를 FCM을 통해 받을 경우 우선 버전 체크 후 채널 생성
        createNotificationChannel()

        // 받아온 메세지의 내용들을 저장
        val type = receiveMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }    // enum class에서 일치하는 타입을 가져온다
        val title = receiveMessage.data["title"]
        val message = receiveMessage.data["message"]

        type ?: return  // type은 nullable이기 때문에 null일 경우 그냥 반환하여 실행 X


        // Notification 실행
        NotificationManagerCompat.from(this)
            .notify(type.id , createNotification(type,title,message))

    }

    // 채널을 만드는 함수
    private fun createNotificationChannel() {
        // 안드로이드 버전 8.0(오레오) 이상일 경우 채널을 생성해줘야 한다. 미만일 경우 채널 따로 필요하지 않다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CANNEL_ID , CHANNEL_NAME , NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    // type에 따른 다른 종류의 Notification 생성 함수
    private fun createNotification(type: NotificationType , title:String? , message:String?):Notification {

        val intent = Intent(this,MainActivity::class.java).apply {
            putExtra("notificationType","${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            // FALG_ACTIVITY_STANDARD의 경우 같은 액티비티여도 한번 더 쌓이지만 , SINGLE_TOP을 했을 경우 같은 B라는 액티비티에서 인텐트를 적용해도
            // 갱신된 액티비티로 바뀐다.
        }

        // 펜딩인텐트라 한번 더 감싼다.
        // type 별로 다른 펜딩 인텐트 생성 , 같은 타입일 경우에는 갱신만 하도록 한다.
        val pendingIntent = PendingIntent.getActivity(this, type.id , intent , FLAG_UPDATE_CURRENT)



        // 기본 설정된 알림
        val notificationBuilder = NotificationCompat.Builder(this, CANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)    // 8.0 미만의 버전에서는 Priority를 지정해줘야 한다.
            .setContentIntent(pendingIntent)    // 펜딩 인텐트 연결
            .setAutoCancel(true)    // Notification 클릭 시 알림 삭제


        when(type) {
            // 일반 알림
            NotificationType.NORMAL -> Unit    // 일반 알림은 추가할 기능이 없음

            // 확장형 알림
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText("😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺" +
                        "😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚 😋 😛 😝 😜 🤪 🤨 " +
                        "🧐 🤓 😎 🥸 🤩 🥳 😏 😒 😞 😔 😟 😕 🙁 ☹ 😣 😖 😫 😩 🥺" +
                        " 😢 😭 😤 😠 😡 🤬 🤯 😳 🥵 🥶 😱 😨 😰 😥 🤭 🤫 🤥 😶" +
                        " 😐 😑 😬 🙄 😯 😦 😧 😮 😲 🥱 😴 🤤 😪 😵 🤐 🥴"))
            }

            // 커스텀 알림림
            // 잘 안쓰는걸 천하지만 위에 두 타입으로 표현할 수 없는 경우 사용
            NotificationType.CUSTOM -> {
                notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(RemoteViews(packageName,R.layout.view_custom_notification).apply {
                        setTextViewText(R.id.title,title)
                        setTextViewText(R.id.message, message)
                    })
            }
        }

        return notificationBuilder.build()

    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"  // 채널 이름
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"    // 채널 설명
        private const val CANNEL_ID = "Channel ID"


    }



}