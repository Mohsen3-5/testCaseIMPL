package com.example.jetcompose.domin.service.helpers

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

object UsageStatsHelper {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getCurrentAppPackageName(context: Context): String? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 10000*10000// 1 minute
        val interval = UsageStatsManager.INTERVAL_BEST
        var packageName: String? = null
        val usageStatsList = usageStatsManager.queryUsageStats(interval, beginTime, endTime)
        if (usageStatsList.isNotEmpty()) {
            val sortedList = usageStatsList.sortedBy { it.lastTimeUsed }
            if(sortedList[sortedList.size - 1].packageName=="android"){
                packageName = sortedList[sortedList.size - 2].packageName
            }else{
                packageName = sortedList[sortedList.size - 1].packageName
            }
        }
        return packageName
    }
}