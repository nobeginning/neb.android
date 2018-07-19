package io.nebulas.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.google.gson.Gson
import io.nebulas.Constants
import io.nebulas.action.ContractAction
import io.nebulas.model.*
import io.nebulas.okhttp.OkHttpManager
import io.nebulas.utils.Util
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal


/**
 * 智能合约调用
 *
 *
 * Created by donald99 on 18/5/23.
 */
object SmartContracts {

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * 转账接口
     * 兼容旧版本SDK的接口  / compatible for the old sdk
     */
    @JvmStatic
    fun pay(context: Context, mainNet: Int, goods: GoodsModel, to: String, value: String, serialNumber: String) {
        pay(context, mainNet, to, value, goods.name, goods.desc, serialNumber, null, null)
    }

    /**
     * pay接口：       星云地址之间的转账
     *
     * @param mainNet      0 测试网    1 主网
     * @param goods        商品详情
     * @param to           转账目标地址
     * @param value        转账value，单位为wei (1NAS =10^18 wei)
     * @param serialNumber 随机码
     * @param gasPrice     gasPrice
     * @param gasLimit     gasLimit
     */
    @JvmStatic
    fun pay(context: Context,
            mainNet: Int,
            to: String,
            value: String,
            goodsName: String? = null,
            goodsDesc: String? = null,
            serialNumber: String = Util.getRandomCode(32),
            gasPrice: String? = null,
            gasLimit: String? = null) {

        val resultActivityPath = "${context.packageName}.nebulas.ResultActivity"
        if (to.isEmpty()) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"to\" when calling function#SmartContracts.pay")
            return
        }
        if (!checkNullableInteger(gasPrice)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasPrice\" when calling function#SmartContracts.pay")
            return
        }
        if (!checkNullableInteger(gasLimit)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasLimit\" when calling function#SmartContracts.pay")
            return
        }

        val openAppMode = OpenAppModeWithPkg(context.packageName, resultActivityPath)
        openAppMode.category = Constants.CATEGORY_TRANSFER
        openAppMode.des = Constants.DESCRIPTION_TRANSFER

        val pageParamsModel = TransferPageParamsModel()
        pageParamsModel.serialNumber = serialNumber
        if (mainNet == 0) {
            pageParamsModel.callback = Constants.TEST_NET_CALL_BACK
        } else {
            pageParamsModel.callback = Constants.MAIN_NET_CALL_BACK
        }
        pageParamsModel.goods = GoodsModel().apply {
            name = goodsName
            desc = goodsDesc
        }

        val payloadModel = PayloadModel()
        payloadModel.type = Constants.PAYLOAD_TYPE_PAY

        val payModel = PayModel()
        payModel.currency = Constants.PAY_CURRENCY
        payModel.payload = payloadModel
        payModel.value = if (value.isEmpty()) "0" else value
        payModel.to = to

        pageParamsModel.pay = payModel

        val gasInfo = GasInfo()
        gasInfo.gasPrice = gasPrice
        gasInfo.gasLimit = gasLimit
        pageParamsModel.gasInfo = gasInfo

        openAppMode.pageParams = pageParamsModel

        val bundle = openAppMode.toBundle()

        ContractAction.start(context, bundle)
    }

    /**
     * 调用智能合约
     * 兼容旧版本SDK的接口  / compatible for the old sdk
     */
    @JvmStatic
    fun call(context: Context, mainNet: Int, goods: GoodsModel, functionName: String, to: String, value: String, args: Array<String>, serialNumber: String) {
        call(context, mainNet, to, value, functionName, args, goods.name, goods.desc, serialNumber, null, null)
    }

    /**
     * call函数：      调用智能合约
     * 所得结果上链
     *
     * @param mainNet      0 测试网    1 主网
     * @param functionName 调用合约的函数名
     * @param to           转账目标地址
     * @param value        转账value，单位为wei (1NAS =10^18 wei)
     * @param args         函数参数列表
     * @param serialNumber 随机码
     */
    @JvmStatic
    fun call(context: Context,
             mainNet: Int,
             to: String,
             value: String,
             functionName: String,
             args: Array<String>,
             goodsName: String? = null,
             goodsDesc: String? = null,
             serialNumber: String = Util.getRandomCode(32),
             gasPrice: String? = null,
             gasLimit: String? = null) {

        val resultActivityPath = "${context.packageName}.nebulas.ResultActivity"
        if (to.isEmpty()) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"to\" when calling function#SmartContracts.call")
            return
        }
        if (!checkNullableInteger(gasPrice)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasPrice\" when calling function#SmartContracts.call")
            return
        }
        if (!checkNullableInteger(gasLimit)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasLimit\" when calling function#SmartContracts.call")
            return
        }

        val openAppMode = OpenAppModeWithPkg(context.packageName, resultActivityPath)
        openAppMode.category = Constants.CATEGORY_TRANSFER
        openAppMode.des = Constants.DESCRIPTION_TRANSFER

        val pageParamsModel = TransferPageParamsModel()
        pageParamsModel.serialNumber = serialNumber
        if (mainNet == 0) {
            pageParamsModel.callback = Constants.TEST_NET_CALL_BACK
        } else {
            pageParamsModel.callback = Constants.MAIN_NET_CALL_BACK
        }
        pageParamsModel.goods = GoodsModel().apply {
            name = goodsName
            desc = goodsDesc
        }

        val payloadModel = PayloadModel()
        payloadModel.type = Constants.PAYLOAD_TYPE_CALL
        payloadModel.function = functionName
        payloadModel.args = args

        val payModel = PayModel()
        payModel.currency = Constants.PAY_CURRENCY
        payModel.payload = payloadModel
        payModel.value = value
        payModel.to = to

        pageParamsModel.pay = payModel

        val gasInfo = GasInfo()
        gasInfo.gasPrice = gasPrice
        gasInfo.gasLimit = gasLimit
        pageParamsModel.gasInfo = gasInfo

        openAppMode.pageParams = pageParamsModel

        val bundle = openAppMode.toBundle()

        ContractAction.start(context, bundle)
    }

    /**
     * 部署智能合约
     *
     * @param mainNet      0 测试网    1 主网
     * @param source       智能合约代码
     * @param sourceType   智能合约Type
     * @param serialNumber 随机码
     */
    @JvmStatic
    fun deploy(context: Context,
               mainNet: Int,
               source: String,
               sourceType: String,
               goodsName: String? = null,
               goodsDesc: String? = null,
               serialNumber: String = Util.getRandomCode(32),
               gasPrice: String? = null,
               gasLimit: String? = null) {

        val resultActivityPath = "${context.packageName}.nebulas.ResultActivity"
        if (source.isEmpty()) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"source\" when calling function#SmartContracts.deploy")
            return
        }
        if (sourceType.isEmpty()) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"sourceType\" when calling function#SmartContracts.deploy")
            return
        }
        if (!checkNullableInteger(gasPrice)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasPrice\" when calling function#SmartContracts.deploy")
            return
        }
        if (!checkNullableInteger(gasLimit)) {
            ContractAction.localError(context, resultActivityPath, 20001, "IllegalArgument : \"gasLimit\" when calling function#SmartContracts.deploy")
            return
        }

        val openAppMode = OpenAppModeWithPkg(context.packageName, "${context.packageName}.nebulas.ResultActivity")
        openAppMode.category = Constants.CATEGORY_TRANSFER
        openAppMode.des = Constants.DESCRIPTION_TRANSFER

        val pageParamsModel = TransferPageParamsModel()
        pageParamsModel.serialNumber = serialNumber
        if (mainNet == 0) {
            pageParamsModel.callback = Constants.TEST_NET_CALL_BACK
        } else {
            pageParamsModel.callback = Constants.MAIN_NET_CALL_BACK
        }
        pageParamsModel.goods = GoodsModel().apply {
            name = goodsName
            desc = goodsDesc
        }

        val payloadModel = PayloadModel()
        payloadModel.type = Constants.PAYLOAD_TYPE_DEPLOY
        payloadModel.source = source
        payloadModel.sourceType = sourceType

        val payModel = PayModel()
        payModel.currency = Constants.PAY_CURRENCY
        payModel.payload = payloadModel

        pageParamsModel.pay = payModel

        val gasInfo = GasInfo()
        gasInfo.gasPrice = gasPrice
        gasInfo.gasLimit = gasLimit
        pageParamsModel.gasInfo = gasInfo

        openAppMode.pageParams = pageParamsModel

        val bundle = openAppMode.toBundle()

        ContractAction.start(context, bundle)
    }

    /**
     * Auth
     */
    @JvmStatic
    fun auth(context: Context) {
        val pm = context.packageManager
        val packageInfo = pm.getPackageInfo(context.packageName, 0)
        val appIconDrawable = pm.getApplicationIcon(context.packageName)
        val appNameRes = packageInfo.applicationInfo.labelRes
        val appName = context.resources.getString(appNameRes)
        val appIcon = Util.getAppIconBytes(appIconDrawable)
        val openAppMode = OpenAppModeWithPkg(context.packageName, "${context.packageName}.nebulas.ResultActivity")
        openAppMode.category = Constants.CATEGORY_AUTH
        openAppMode.des = Constants.DESCRIPTION_AUTH

        val model = AuthPageParamsModel()
        model.appName = appName
        model.appIcon = appIcon

        openAppMode.pageParams = model

        ContractAction.start(context, openAppMode.toBundle())
    }

    /**
     * 查询地址详细信息
     * 兼容旧版本SDK的接口  / compatible for the old sdk
     */
    @JvmStatic
    fun queryAccountState(address: String, callback: StatusCallback?){
        queryAccountState(0, address, callback)
    }

    /**
     * 查询地址详细信息
     * @param address
     * @param callback
     */
    @JvmStatic
    fun queryAccountState(mainNet: Int, address: String, callback: StatusCallback?) {
        if (address.isBlank()) {
            callback?.onFail("IllegalArgument : \"address\" when calling function#SmartContracts.queryAccountState")
            return
        }
        val accountState = AccountState()
        accountState.address = address
        val requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(accountState))
        val url = if (mainNet == 0) {
            Constants.TEST_NET_RPC_ACCOUNT_STATE_URL
        } else {
            Constants.MAIN_NET_RPC_ACCOUNT_STATE_URL
        }
        val request = Request.Builder().url(url).post(requestBody).build()
        OkHttpManager.getInstance().okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback?.apply {
                    mainHandler.post {
                        onFail(e.message ?: e.toString())
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response?) {
                if (callback != null && response != null) {
                    mainHandler.post {
                        callback.onSuccess(response.body()?.string() ?: "{}")
                    }
                }
            }
        })
    }

    /**
     * 注：该方法为模拟执行，执行的结果并不会上链。
     * 常用于调用get类型的接口，查询数据。
     *
     * @param contractModel
     * @param from
     * @param to
     * @param nonce
     * @param callback
     */
    @JvmStatic
    fun simulateCall(contractModel: ContractModel<*>?, from: String, to: String, nonce: Int, callback: StatusCallback?) {
        if (contractModel == null || TextUtils.isEmpty(from)) {
            return
        }
        val callContractModel = CallContractModel()
        callContractModel.contract = contractModel
        callContractModel.from = from
        callContractModel.to = to
        callContractModel.nonce = nonce
        callContractModel.gasLimit = "400000"
        callContractModel.gasPrice = "1000000"
        callContractModel.value = "0"
        val requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(callContractModel))
        val request = Request.Builder().url(Constants.MAIN_NET_RPC_CALL_URL).post(requestBody).build()
        OkHttpManager.getInstance().okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback?.apply {
                    mainHandler.post {
                        onFail(e.message ?: e.toString())
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response?) {
                if (callback != null && response != null) {
                    mainHandler.post {
                        callback.onSuccess(response.body()?.string() ?: "{}")
                    }
                }
            }
        })
    }

    /**
     * 查询交易状态
     *
     * @param mainNet      0 测试网    1 主网
     * @param serialNumber
     */
    @JvmStatic
    fun queryTransferStatus(mainNet: Int, serialNumber: String, callback: StatusCallback?) {
        if (serialNumber.isBlank()) {
            callback?.onFail("IllegalArgument : \"serialNumber\" when calling function#SmartContracts.queryTransferStatus")
            return
        }
        val endpoint = if (mainNet == 0) {
            Constants.MAIN_NET_PAY_URL + "pay/query?payId=" + serialNumber
        } else {
            Constants.MAIN_NET_PAY_URL + "mainnet/pay/query?payId=" + serialNumber
        }

        val request = Request.Builder().get().url(endpoint).build()
        OkHttpManager.getInstance().okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback?.apply {
                    mainHandler.post {
                        onFail(e.message ?: e.toString())
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response?) {
                if (callback != null && response != null) {
                    mainHandler.post {
                        callback.onSuccess(response.body()?.string() ?: "{}")
                    }
                }
            }
        })
    }

    /**
     * 查询交易状态
     *
     * @param mainNet      0 测试网    1 主网
     * @param transactionHash   交易hash
     */
    @JvmStatic
    fun queryTransferStatusByHash(mainNet: Int, transactionHash: String, callback: StatusCallback?) {
        if (transactionHash.isBlank()) {
            callback?.onFail("IllegalArgument : \"transactionHash\" when calling function#SmartContracts.queryTransferStatusByHash")
            return
        }

        val url = if (mainNet == 0) {
            Constants.TEST_NET_RPC_QUERY_TX
        } else {
            Constants.MAIN_NET_RPC_QUERY_TX
        }

        val request = Request.Builder()
                .url(url)
                .post(FormBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(mapOf(Pair("hash", transactionHash)))))
                .build()
        OkHttpManager.getInstance().okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback?.apply {
                    mainHandler.post {
                        onFail(e.message ?: e.toString())
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response?) {
                if (callback != null && response != null) {
                    val responseString = response.body()?.string() ?: "{}"
                    try {
                        val finalResult: String
                        val json = JSONObject(responseString)
                        val resultJson = json.optJSONObject("result")
                        if (resultJson != null) {
                            finalResult = JSONObject().apply {
                                put("code", 0)
                                put("msg", "success")
                                put("data", resultJson)
                            }.toString()
                            mainHandler.post {
                                callback.onSuccess(finalResult)
                            }
                            return
                        }
                        if (json.has("error")) {
                            val msg = json.optString("error") ?: "Unknown error"
                            finalResult = JSONObject().apply {
                                put("code", 1)
                                put("msg", msg)
                                put("data", JSONObject())
                            }.toString()
                            mainHandler.post {
                                callback.onSuccess(finalResult)
                            }
                            return
                        }
                        throw IllegalStateException("")
                    } catch (e: Exception) {
                        mainHandler.post {
                            callback.onSuccess(JSONObject().apply {
                                put("code", 10000)
                                put("msg", "Result parse failed")
                            }.toString())
                        }
                    }
                }
            }
        })
    }

    interface StatusCallback {
        fun onSuccess(response: String)
        fun onFail(error: String)
    }

    private fun checkNullableInteger(gasPrice: String?): Boolean {
        if (gasPrice != null) {
            if (gasPrice.isBlank()) {
                return true
            }
            val m = "[0-9]+".toRegex()
            val match = m.matches(gasPrice)
            if (!match) {
                return false
            }
            return try {
                val gas = BigDecimal(gasPrice)
                gas.toInt() > 0
            } catch (e: Exception) {
                false
            }
        }
        return true
    }

}
