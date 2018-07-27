package io.nebulas.response

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

open class Response(open val errorCode: Int, open val errorMessage: String) : Parcelable {

    companion object {
        const val ERROR_CODE_UNKOWN = 10000

        val unknownErrorResponse = Response(ERROR_CODE_UNKOWN, "未知错误")

        @JvmField
        val CREATOR: Parcelable.Creator<Response> = object : Parcelable.Creator<Response> {
            override fun createFromParcel(source: Parcel): Response = Response(source)
            override fun newArray(size: Int): Array<Response?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    open fun toJSONObject():JSONObject {
        return JSONObject().apply {
            put("errorCode", errorCode)
            put("errorMessage", errorMessage)
        }
    }

    override fun toString(): String {
        return toJSONObject().toString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(errorCode)
        writeString(errorMessage)
    }
}

class TransferResponse(errorCode: Int, errorMessage: String, val hash: String) : Response(errorCode, errorMessage), Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(errorCode)
        writeString(errorMessage)
        writeString(hash)
    }

    override fun toJSONObject(): JSONObject {
        val obj = super.toJSONObject()
        return obj.apply {
            put("hash", hash)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TransferResponse> = object : Parcelable.Creator<TransferResponse> {
            override fun createFromParcel(source: Parcel): TransferResponse = TransferResponse(source)
            override fun newArray(size: Int): Array<TransferResponse?> = arrayOfNulls(size)
        }
    }
}

class AuthResponse(errorCode: Int, errorMessage: String, val address: String) : Response(errorCode, errorMessage), Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(errorCode)
        writeString(errorMessage)
        writeString(address)
    }

    override fun toJSONObject(): JSONObject {
        val obj = super.toJSONObject()
        return obj.apply {
            put("address", address)
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AuthResponse> = object : Parcelable.Creator<AuthResponse> {
            override fun createFromParcel(source: Parcel): AuthResponse = AuthResponse(source)
            override fun newArray(size: Int): Array<AuthResponse?> = arrayOfNulls(size)
        }
    }
}