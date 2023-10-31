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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bnibagus.model.TransaksiEvent
import com.example.bnibagus.model.TransaksiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AddTransaksiDialog (
    state : TransaksiState,
    onEvent : (TransaksiEvent)-> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {onEvent(TransaksiEvent.HideDialog) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = state.bank,
                    onValueChange = {
                        onEvent(TransaksiEvent.SetBank(it))
                    },
                    placeholder = {
                        Text(text = "bank")
                    }
                )
                TextField(
                    value = state.merchant,
                    onValueChange = {
                        onEvent(TransaksiEvent.SetMerchant(it))
                    },
                    placeholder = {
                        Text(text = "merchant")
                    }
                )
                TextField(
                    value = state.id_transaksi,
                    onValueChange = {
                        onEvent(TransaksiEvent.SetIdTransaksi(it))
                    },
                    placeholder = {
                        Text(text = "id transaksi")
                    }
                )
                TextField(
                    value = state.nominal.toString(),
                    onValueChange = {

                        onEvent(TransaksiEvent.SetNominal(it.toInt()))

                    },
                    placeholder = {
                        Text(text = "nominal")
                    }
                )

            }
        }, confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd)
            {
                Button(onClick = { onEvent(TransaksiEvent.saveTransaksi) }) {
                    Text(text = "Save")
                }
            }
        }

    )
}