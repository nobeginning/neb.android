package io.nebulas.api

import io.nebulas.response.Response

interface ResponseHandler {

    fun onResponse(response:Response)

}