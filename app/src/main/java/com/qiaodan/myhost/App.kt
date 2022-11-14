package com.qiaodan.myhost

import android.util.Log
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.RePluginApplication

/**
 * author: created by song on 2022/11/14 16:47
 * description: TODO
 */
class App: RePluginApplication() {


    override fun onCreate() {
        super.onCreate()
        Log.d("PluginTag", "Host: Application onCreate!")
    }



}