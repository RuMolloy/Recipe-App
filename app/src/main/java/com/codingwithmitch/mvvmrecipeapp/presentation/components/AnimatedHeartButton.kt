package com.codingwithmitch.mvvmrecipeapp.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp

enum class HeartButtonState{
    IDLE, ACTIVE
}

@Composable
fun AnimatedHeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartButtonState>,
    onToggle: () -> Unit,
){
    val idleIconSize = 50.dp
    val expandedIconSize = 80.dp

    val currentState by remember { buttonState }
    val transition = updateTransition(targetState = currentState, label = "HeartAnimation")

    val heartColor by transition.animateColor(
        transitionSpec = {
            when {
                HeartButtonState.IDLE isTransitioningTo HeartButtonState.ACTIVE ->
                    tween(durationMillis = 500)
                else -> tween(durationMillis = 500)
            }
        },
        label = "heartColor"
    ) {
        state -> when (state) {
            HeartButtonState.IDLE -> Color.LightGray
            HeartButtonState.ACTIVE -> Color.Red
        }
    }

    val heartSize by transition.animateDp(
        transitionSpec = {
            when {
                HeartButtonState.IDLE isTransitioningTo HeartButtonState.ACTIVE ->
                    keyframes {
                        durationMillis = 500
                        expandedIconSize at 150
                        idleIconSize at 300
                    }
                else ->
                    keyframes {
                        durationMillis = 500
                        expandedIconSize at 150
                        idleIconSize at 300
                    }
            }
        }, label = "heartSize"
    ) {
        state -> when (state) {
            HeartButtonState.IDLE -> idleIconSize
            HeartButtonState.ACTIVE -> idleIconSize
        }
    }

    Image(
        imageVector = Icons.Default.Favorite,
        contentDescription = "HeartButton",
        modifier = modifier
            .height(heartSize)
            .width(heartSize)
            .clickable(
                onClick = onToggle
            ),
        colorFilter = ColorFilter.tint(heartColor)
    )
}