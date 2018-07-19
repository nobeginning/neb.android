package io.nebulas

/**
 * 星云常量
 * Created by donald99 on 18/5/23.
 */

object Constants {

    @JvmField
    val TEST_NET = 0

    @JvmField
    val MAIN_NET = 1

    @JvmField
    val RANDOM_LENGTH = 32

    @JvmField
    val CATEGORY_TRANSFER = "category_transfer"

    @JvmField
    val CATEGORY_AUTH = "category_auth"

    @JvmField
    val DESCRIPTION_TRANSFER = "confirmTransfer"

    @JvmField
    val DESCRIPTION_AUTH = "confirm_auth"

    @JvmField
    val MAIN_NET_RPC_ACCOUNT_STATE_URL = "https://mainnet.nebulas.io/v1/user/accountstate"
    val TEST_NET_RPC_ACCOUNT_STATE_URL = "https://testnet.nebulas.io/v1/user/accountstate"

    @JvmField
    val MAIN_NET_RPC_CALL_URL = "https://mainnet.nebulas.io/v1/user/call"

    val MAIN_NET_RPC_QUERY_TX = "https://mainnet.nebulas.io/v1/user/getTransactionReceipt"
    val TEST_NET_RPC_QUERY_TX = "https://testnet.nebulas.io/v1/user/getTransactionReceipt"

    @JvmField
    val MAIN_NET_PAY_URL = "https://pay.nebulas.io/api/"

    @JvmField
    val TEST_NET_CALL_BACK = "https://pay.nebulas.io/api/pay"

    @JvmField
    val MAIN_NET_CALL_BACK = "https://pay.nebulas.io/api/mainnet/pay"

    @JvmField
    val PAY_CURRENCY = "NAS"

    @JvmField
    val PAYLOAD_TYPE_PAY = "binary"

    @JvmField
    val PAYLOAD_TYPE_CALL = "call"

    @JvmField
    val PAYLOAD_TYPE_DEPLOY = "deploy"

    @JvmField
    val NAS_NANO_DOWNLOAD_URL = "https://nano.nebulas.io/"

    @JvmField
    val NAS_NANO_PACKAGE_NAME = "io.nebulas.wallet.android"//nas nano package name

}
