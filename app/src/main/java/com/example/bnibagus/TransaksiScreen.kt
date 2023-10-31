package com.example.bnibagus

import android.content.ContentValues
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bnibagus.model.TransaksiEvent
import com.example.bnibagus.model.TransaksiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaksiScreen (
    navController: NavController,
    state: TransaksiState,
                     onEvent:(TransaksiEvent)->Unit){
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            onEvent(TransaksiEvent.showDialog)

        })
        {


        }
    }, modifier = Modifier.padding(16.dp)
    ) { padding ->

        Text(text = "Transaksi Screeen")
        if(state.isAddingTransaksi==true){
            AddTransaksiDialog(state = state, onEvent =onEvent )
        }
        LazyColumn(contentPadding = padding,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            items(state.Transaksi){ transaksi->
                Row(modifier = Modifier.fillMaxWidth())
                {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${transaksi.bank} merchant ${transaksi.merchant}"
                            , fontSize = 20.sp)
                        Text(text = transaksi.nominal.toString())

                    }
                }
            }

        }
//        val scanLauncher = rememberLauncherForActivityResult(
//            contract = ScanContract(),
//            onResult = { result -> Log.i(ContentValues.TAG, "scanned code: ${result.contents}") }
//        )
//        Button(onClick = { scanLauncher.launch(ScanOptions()) }) {
//            Text(text = "Scan barcode")
//        }

    }

}