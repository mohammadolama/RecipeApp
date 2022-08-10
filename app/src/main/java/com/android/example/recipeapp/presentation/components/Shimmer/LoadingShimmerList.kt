package com.android.example.recipeapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.android.example.recipeapp.presentation.components.Shimmer.ShimmerCardItem

@Composable
fun LoadingShimmerList(
    cardHeight: Dp,
    padding: Dp
){

    val shimmerColorShades = listOf(
        Color.LightGray.copy(0.9f),
        Color.LightGray.copy(0.2f),
        Color.LightGray.copy(0.9f)
    )
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, delayMillis = 300, easing = LinearEasing),
            RepeatMode.Restart
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(translateAnim-200, translateAnim-200),
        end = Offset(translateAnim, translateAnim)
    )
    val brush2 = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(translateAnim-300, translateAnim-300),
        end = Offset(translateAnim-100, translateAnim-100)
    )




    LazyColumn(content = {
        items(5){
            ShimmerCardItem(brush =brush , brush2 =brush2 , cardHeight =cardHeight , padding = padding)
        }
    })

}