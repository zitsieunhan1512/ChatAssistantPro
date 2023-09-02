package app.mbl.hcmute.chatApp.domain

interface Mapper<in LeftObject, out RightObject> {

    fun LeftObject.mapLeftToRight(): RightObject

}