package com.zrb.baseappmvp.tools

/**
 * Created by zrb on 2017/6/13.
 */

class ExceptionUtil {
    class LoginOuttimeException : Exception() {
        var meg = ""

    }

    class ResponseBodyException : Exception()
    class RemoteLoginException : Exception()
    class HttpErrorException : Exception()
    class GsonException : Exception()
    class NoNetworkException : Exception()
    class OSSErrorException : Exception()
    class JXSAuthorityException : Exception()
}
