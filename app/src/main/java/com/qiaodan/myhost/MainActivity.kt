package com.qiaodan.myhost

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.model.PluginInfo
import com.qihoo360.replugin.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val PLUGIN_NAME = "com.qiaodan.myplugin"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<View>(R.id.btn_install).setOnClickListener(this)

        findViewById<View>(R.id.btn_preload).setOnClickListener(this)

        findViewById<View>(R.id.btn_start).setOnClickListener(this)

        findViewById<View>(R.id.btn_open).setOnClickListener(this)


    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.btn_install -> {
                simulateInstallExternalPlugin()
            }

            R.id.btn_preload -> {
                RePlugin.preload(PLUGIN_NAME)
            }

            R.id.btn_start -> {
                RePlugin.fetchContext(PLUGIN_NAME)
            }
            R.id.btn_open -> {
                RePlugin.startActivity(this@MainActivity, RePlugin.createIntent(PLUGIN_NAME, "com.qiaodan.myplugin.PluginActivity"))
            }

        }
    }


    /**
     * 模拟安装或升级（覆盖安装）外置插件
     * 注意：为方便演示，外置插件临时放置到Host的assets/external目录下，具体说明见README
     */
    private fun simulateInstallExternalPlugin() {
        val demo3Apk = "app-release.apk"
        val demo3apkPath = "external" + File.separator + demo3Apk
        // 文件是否已经存在？直接删除重来
        val pluginFilePath = filesDir.absolutePath + File.separator + demo3Apk
        val pluginFile = File(pluginFilePath)
        if (pluginFile.exists()) {
            FileUtils.deleteQuietly(pluginFile)
        }
        // 开始复制
        copyAssetsFileToAppFiles(demo3apkPath, demo3Apk)
        var info: PluginInfo? = null
        if (pluginFile.exists()) {
            info = RePlugin.install(pluginFilePath)
        }
        if (info != null) {
            Toast.makeText(this@MainActivity, "插件安装成功！！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "插件安装失败！！", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 从assets目录中复制某文件内容
     * @param  assetFileName assets目录下的Apk源文件路径
     * @param  newFileName 复制到/data/data/package_name/files/目录下文件名
     */
    private fun copyAssetsFileToAppFiles(assetFileName: String, newFileName: String) {
        var ism: InputStream? = null
        var fos: FileOutputStream? = null
        val buffsize = 1024
        try {
            ism = this.assets.open(assetFileName)
            fos = openFileOutput(newFileName, MODE_PRIVATE)
            var byteCount = 0
            val buffer = ByteArray(buffsize)
            while (ism.read(buffer).also { byteCount = it } != -1) {
                fos.write(buffer, 0, byteCount)
            }
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                ism!!.close()
                fos!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}