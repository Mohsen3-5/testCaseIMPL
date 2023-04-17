package com.example.jetcompose.data.repo

import android.content.Context
import com.example.jetcompose.domin.service.repo.SharedPreferencesINF
import javax.inject.Inject

class SharedPreferences @Inject constructor(
    private val context: Context
): SharedPreferencesINF {
    val sharedPref = context.getSharedPreferences("service_running", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    override fun editServiceStatus(status:Boolean){
        editor.putBoolean("service_running",status)
        editor.apply()
    }

    override fun getServiceStatus():Boolean{
       return sharedPref.getBoolean("service_running",false)
    }

}