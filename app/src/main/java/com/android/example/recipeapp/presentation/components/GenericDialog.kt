package com.android.example.recipeapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


data class PositiveAction(
    val positiveBtnText: String,
    val onPositiveBtnAction: () -> Unit
)


data class NegativeAction(
    val negativeBtnText: String,
    val onNegativeBtnAction: () -> Unit
)


@Composable
fun GenericDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String,
    description: String?,
    positiveAction: PositiveAction? = null,
    negativeAction: NegativeAction? = null,

    ) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            if (description != null) {
                Text(text = description)
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (negativeAction != null) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = negativeAction.onNegativeBtnAction,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onError,
                        )
                    ) {
                        Text(text = negativeAction.negativeBtnText)
                    }
                }

                if (positiveAction != null) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = positiveAction.onPositiveBtnAction,
                    ) {
                        Text(text = positiveAction.positiveBtnText)
                    }
                }

            }
        }
    )


}











