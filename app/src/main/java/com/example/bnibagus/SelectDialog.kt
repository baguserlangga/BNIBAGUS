package com.example.bnibagus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bnibagus.model.TransaksiEvent
import com.example.bnibagus.model.TransaksiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDialog (
    onEvent : (TransaksiEvent)-> Unit,

):String{
    var hasil by remember{
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = {onEvent(TransaksiEvent.HideDialog) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

            }
        }, confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd)
            {
                Button(onClick = { hasil = "Donut Chart" }) {
                    Text(text = "Donut Chart")
                }
                Button(onClick = { hasil = "Line Chart" }) {
                    Text(text = "Line Chart")
                }
            }
        }

    )
    return hasil
}