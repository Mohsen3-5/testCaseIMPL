package com.example.jetcompose.domin.service.repo

interface SharedPreferencesINF {
    fun editServiceStatus(status:Boolean)
    fun getServiceStatus():Boolean
}