package com.example.part3_ch01_pushnotificationreceiver

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


 */