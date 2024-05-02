package com.kurlic.pechka.back.services.heatservice

enum class ServiceState(num: Int) {
    Launching(0),
    Active(1),
    Stopped(2)
}