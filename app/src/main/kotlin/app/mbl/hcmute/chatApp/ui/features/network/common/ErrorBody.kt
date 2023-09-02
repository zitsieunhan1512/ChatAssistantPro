package app.mbl.hcmute.chatApp.ui.features.network.common

data class ErrorBody(
    val statusCode: Int,
    val error: String,
    val message: String,
)