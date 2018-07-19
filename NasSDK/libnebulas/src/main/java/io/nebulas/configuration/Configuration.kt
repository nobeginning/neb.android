package io.nebulas.configuration

import io.nebulas.Constants

object Configuration {

    enum class ReleaseType(val desc: String, val pkgSuffix: String, val netType:Int) {
        RELEASE("Release/正式发行版本", "", Constants.MAIN_NET),
        TEST_NET("TestNet/社区版本", ".testnet", Constants.TEST_NET),
        DEBUG("Debug/Develop/开发调试版本", ".debug", Constants.TEST_NET)
    }

    private const val packagePrefix = "io.nebulas.wallet.android"
    private const val actPath = "$packagePrefix.module.launch.LaunchActivity"

    private var releaseType: ReleaseType = ReleaseType.RELEASE

    fun useTestNet() {
        releaseType = ReleaseType.TEST_NET
    }

    fun useRelease() {
        releaseType = ReleaseType.RELEASE
    }

    fun useDebug() {
        releaseType = ReleaseType.DEBUG
    }

    fun getCurrentReleaseType(): ReleaseType {
        return releaseType
    }

    fun getNasNanoPackage(): String {
        return packagePrefix.plus(releaseType.pkgSuffix)
    }

    fun getInvokeTargetActivity(): String {
        return actPath
    }
}