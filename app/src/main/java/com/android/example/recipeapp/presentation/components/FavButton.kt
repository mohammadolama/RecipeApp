package com.android.example.recipeapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ButtonState {
    IDLE, PRESSED
}

@Composable
fun FavButton() {

    var currentState by remember { mutableStateOf(ButtonState.IDLE) }

    val backgroundColor by animateColorAsState(if (currentState == ButtonState.PRESSED) MaterialTheme.colors.primary else Color.White)
    val backgroundColor2 by animateColorAsState(if (currentState == ButtonState.PRESSED) Color.White else MaterialTheme.colors.primary)
    val transition = updateTransition(currentState, label = "s")

    val borderWidth by transition.animateDp(
        label = "s",
        transitionSpec = {
            when {
                ButtonState.IDLE isTransitioningTo ButtonState.PRESSED ->
//                    tween(durationMillis = time.value , easing = LinearEasing)
                    spring(stiffness = 80f)
                else ->
//                    tween(durationMillis = time.value , easing = LinearEasing)
                    spring(stiffness = 80f)
            }
        }
    ) { state ->
        when (state) {
            ButtonState.IDLE -> 170.dp
            ButtonState.PRESSED -> 50.dp
        }
    }


    val radius by transition.animateDp(
        label = "s",
        transitionSpec = {
            when {
                ButtonState.IDLE isTransitioningTo ButtonState.PRESSED ->
//                    tween(durationMillis = time.value , easing = LinearEasing)
                    spring(stiffness = 80f)
                else ->
//                    tween(durationMillis = time.value , easing = LinearEasing)
                    spring(stiffness = 80f)
            }
        }
    ) { state ->
        when (state) {
            ButtonState.IDLE -> 35.dp
            ButtonState.PRESSED -> 35.dp
        }
    }
    Button(
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        ),
//    backgroundColor = Color.White,
        shape = RoundedCornerShape(radius),
        modifier = Modifier.size(borderWidth, 35.dp),
        onClick = {
            if (currentState == ButtonState.IDLE) {
                currentState = ButtonState.PRESSED
            } else {
                currentState = ButtonState.IDLE
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
//                Modifier.width(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Favorite,
                    "adad",
                    tint = backgroundColor2,
                    modifier = Modifier.size(15.dp)
                )
            }

            AnimatedVisibility(currentState == ButtonState.IDLE) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "ADD TO FAVORITES!",
                    softWrap = false,
                    color = MaterialTheme.colors.primary,
                    style = TextStyle(
                        fontSize = 10.sp
                    )
                )
            }


        }
    }

}