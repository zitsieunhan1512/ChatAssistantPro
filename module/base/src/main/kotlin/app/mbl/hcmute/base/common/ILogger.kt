package app.mbl.hcmute.base.common

interface ILogger {
    fun log(level: Int, tag: String?, functionName: String, message: String)
    fun d(className: String, functionName: String, msg: String?)
    fun i(className: String, functionName: String, msg: String?)
    fun e(className: String, functionName: String, msg: String?)
    fun w(className: String, functionName: String, msg: String?)
    fun v(className: String, functionName: String, msg: String?)
}