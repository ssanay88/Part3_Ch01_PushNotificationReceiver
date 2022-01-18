package com.example.part3_ch01_pushnotificationreceiver

// enum?
enum class NotificationType(val title: String , val id: Int) {

    NORMAL("일반 알림",0),
    EXPANDABLE("확장형 알림",1),
    CUSTOM("커스텀 알림",2)
}

/*
enum : 열거 클래스
클래스 안에 각각의 enum은 해당 클래스의 인스턴스이다.


 */