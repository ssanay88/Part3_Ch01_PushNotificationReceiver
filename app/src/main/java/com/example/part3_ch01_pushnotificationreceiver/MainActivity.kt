package com.example.part3_ch01_pushnotificationreceiver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.part3_ch01_pushnotificationreceiver.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initFirebase()
        updateResult()


    }

    // 펜딩 인텐트로 인해 새로운 액티비티 실행되는 경우
    // 여기서는 Notification 클릭으로 액티비티 실행
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)    // 새로 들어온 인텐트로 교체 - 데이터 갱신
        updateResult(true)
    }

    private fun initFirebase() {
        // firebaseMessage의 토큰을 가져온다.
        // 토큰은 Message를 보낼때 식별자 역할을 해준다.
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful) {
                mainBinding.firebaseTokenTextView.text = task.result
            }
        }
    }


    // isNewIntent : 앱이 꺼져있다가 notification으로 인해 열렸는가 or 앱이 켜진 상태로 인텐트를 타고 들어온건가
    // true -> notification을 통해 앱 실행 , false -> 앱 실행으로 인한 Activity 시작
    private fun updateResult(isNewIntent : Boolean = false) {
        // notificationType이 null일 경우 앱 실행으로 시작된것이므로  앱 런쳐로 실행
        mainBinding.resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런쳐") + if (isNewIntent) {
            "(으)로 갱신했습니다."
        } else {
            "(으)로 실행했습니다."
        }

    }


}




/*
# 공부할 것 #
Firebase 토큰을 확인할 수 있다. - Firebase Cloud Messaging
일반 , 확장형 , 커스텀 알림을 볼 수 있다. - Notification

# Firebase Cloud Messaging(FCM)
무료로 메세지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션
1. 알림 메시지 : 구현 쉬움 , 앱이 백그라운드에 있을 경우 포그라운드로 앱이 열고 상호작용 가능
2. 데이터 메시지 : 구현 어려움 , 앱이 어떤 상태는 앱이 메시지에 대한 처리 가능

깃허브에 오픈소스로 코드를 올릴 경우 google-services.json 파일은 앱에 연동되는 정보를 가지고 있기 때문에 제외하고 깃허브에 올려줍니다.

메세지를 수신하려변 Firebase Messaging Service를 추가해야 한다.

notification : 흔히 사용하는 알림 , 음악을 들을 때 미디어 컨트롤을 가능하게 해주는 기능도 있다.
Android 8.0 을 기준으로 각종 제약이 달라지므로 공부 필요!!

 */