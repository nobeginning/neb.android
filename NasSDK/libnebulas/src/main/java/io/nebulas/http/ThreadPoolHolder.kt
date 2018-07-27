package io.nebulas.http

import java.util.concurrent.Executors

object ThreadPoolHolder {

    val executor = Executors.newCachedThreadPool()

}