package com.unipiloto.myapplication.presentation.postdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.unipiloto.myapplication.presentation.postdetail.CommentField
import com.unipiloto.myapplication.presentation.postdetail.NewCommentState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCommentForm(
    state: NewCommentState,
    onFieldChanged: (CommentField, String) -> Unit,
    onAddComment: () -> Unit,
    isAdding: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Nuevo Comentario",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo Nombre
            OutlinedTextField(
                value = state.name,
                onValueChange = { onFieldChanged(CommentField.NAME, it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre *") },
                singleLine = true,
                isError = state.nameError != null,
                supportingText = {
                    state.nameError?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Email
            OutlinedTextField(
                value = state.email,
                onValueChange = { onFieldChanged(CommentField.EMAIL, it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email *") },
                singleLine = true,
                isError = state.emailError != null,
                supportingText = {
                    state.emailError?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Comentario
            OutlinedTextField(
                value = state.body,
                onValueChange = { onFieldChanged(CommentField.BODY, it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Comentario *") },
                singleLine = false,
                isError = state.bodyError != null,
                supportingText = {
                    state.bodyError?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón enviar
            Button(
                onClick = onAddComment,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isValid && !isAdding
            ) {
                if (isAdding) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = androidx.compose.ui.graphics.Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar Comentario")
                }
            }
        }
    }
}