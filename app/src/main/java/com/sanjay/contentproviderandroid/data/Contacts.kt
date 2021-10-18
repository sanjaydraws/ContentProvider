package com.sanjay.contentproviderandroid.data


data class Contacts(
    val id:String? = null ,
    val displayName:String? = null ,
    val hasPhoneNumber:Int? = null ,
    val phoneNumbers:ArrayList<String>? = ArrayList()
)