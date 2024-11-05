package com.example.epic_choice.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.epic_choice.R


/// Define your custom font family
val SukarBlack2 = FontFamily(
    Font(R.font.sukarblack2, FontWeight.Bold)
)

val SukarBlack3 = FontFamily(
    Font(R.font.sukarblack3, FontWeight.Normal)
)

// Define your typography for Material 3
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = SukarBlack2,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SukarBlack3,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SukarBlack3,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    // You can define other text styles as needed
    bodyMedium = TextStyle(
        fontFamily = SukarBlack3,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SukarBlack2,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)