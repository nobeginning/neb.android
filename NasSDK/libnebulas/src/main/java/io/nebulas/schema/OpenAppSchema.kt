package io.nebulas.schema

/**
 * 打开调启App
 * Created by donald99 on 18/5/23.
 */

object OpenAppSchema {

    /**
     * 获取schema url
     * @param paramsJSON json 字符串
     * @return schemaurl
     */
    @JvmStatic
    fun getSchemaUrl(paramsJSON: String): String {
        return "openapp.nasnano://virtual?params=$paramsJSON"
    }
}
