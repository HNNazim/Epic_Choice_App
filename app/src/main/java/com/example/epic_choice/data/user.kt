package com.example.epic_choice.data

import android.provider.ContactsContract.CommonDataKinds.Email
import okio.Path

data class user(
    val firstName:String,
    val lastName: String,
    val email: String,
    var imagePath: String = ""
)
{
    constructor(): this("","","", "")

}
