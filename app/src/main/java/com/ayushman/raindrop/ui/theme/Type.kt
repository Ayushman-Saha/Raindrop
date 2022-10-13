package com.ayushman.raindrop.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ayushman.raindrop.R

//The poppins typography
val Poppins = FontFamily(
    Font(
        R.font.poppins,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    ),
    Font(
        R.font.poppins_semibold,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal
    ),
    Font(
        R.font.poppins_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Black
    ),
    body2= TextStyle(
      fontFamily = Poppins,
      fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = TextGrey
    ),
    button = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = White
    ),
    caption = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Black
    ),
    h1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = Black
    ),
    h2 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        color = Black
    ),

    subtitle1 = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Black
    ),

    )