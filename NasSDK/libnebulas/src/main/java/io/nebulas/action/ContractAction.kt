package io.nebulas.action

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast

import io.nebulas.Constants
import io.nebulas.configuration.Configuration

/**
 * Created by donald99 on 18/5/23.
 */

object ContractAction {

    /**
     * Schema 方式启动星云钱包
     * @param context 上下文
     * @param url     schema
     */
    @JvmStatic
    fun start(context: Context?, url: String) {
        if (context == null || TextUtils.isEmpty(url)) {
            return
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            handleException(context)
        }
    }

    fun start(context: Context, bundle: Bundle) {
        val intent = Intent()
        intent.putExtras(bundle)
        intent.putExtra("time", System.currentTimeMillis())
        intent.component = ComponentName(Configuration.getNasNanoPackage(), Configuration.getInvokeTargetActivity())
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.LAUNCHER")
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        } catch (e: Exception) {
            handleException(context)
        }
    }

    fun localError(context: Context, activityPath:String, errorCode:Int, errorMessage:String) {
        val intent = Intent().apply {
            putExtra("errorCode", errorCode)
            putExtra("errorMessage", errorMessage)
            component = ComponentName(context.packageName, activityPath)
        }
        context.startActivity(intent)
    }

    private fun handleException(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Toast.makeText(context, "没安装Nebulas智能数字钱包，正在前往官网下载...", Toast.LENGTH_LONG).show()
        intent.data = Uri.parse(String.format(Constants.NAS_NANO_DOWNLOAD_URL))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "没安装应用市场或浏览器，下载失败", Toast.LENGTH_SHORT).show()
        }
    }

}

