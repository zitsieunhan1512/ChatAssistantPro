package app.mbl.hcmute.chatApp.util.exception

class ReactiveClickException(
    msg: String,
    cause: Throwable?,
    stack: Array<StackTraceElement>
) : Throwable("$msg ::: ${stack.joinToString()}", cause, true, true)