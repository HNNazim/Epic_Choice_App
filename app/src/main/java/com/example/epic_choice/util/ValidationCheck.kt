package com.example.epic_choice.util

import android.util.Patterns

fun validateFirstName(firstName:String):RegisterValidation{

    if (firstName.isEmpty())
        return RegisterValidation.Failed("Fields Can't Be Empty")

    return RegisterValidation.Success
}

fun validateLastName(lastName:String):RegisterValidation{

    if (lastName.isEmpty())
        return RegisterValidation.Failed("Fields Can't Be Empty")

    return RegisterValidation.Success
}

fun validateEmail(email:String):RegisterValidation{

    if (email.isEmpty())
        return RegisterValidation.Failed("Fields Can't Be Empty!")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format!")

    return RegisterValidation.Success
}

fun validatePassword(password:String):RegisterValidation{

    if (password.isEmpty())
        return RegisterValidation.Failed("Fields Can't Be Empty!")

    if (password.length<8)
        return RegisterValidation.Failed("Password Should Contain Atleast 8 Characters!")

    return RegisterValidation.Success
}

