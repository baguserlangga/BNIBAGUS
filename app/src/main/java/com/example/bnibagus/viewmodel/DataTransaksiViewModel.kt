package com.example.bnibagus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bnibagus.SortType
import com.example.bnibagus.model.DataTransaksiDao
import com.example.bnibagus.model.DataTransaksiQr
import com.example.bnibagus.model.TransaksiEvent
import com.example.bnibagus.model.TransaksiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DataTransaksiViewModel(private val dao: DataTransaksiDao): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.IDS)
    private val _state = MutableStateFlow(TransaksiState())
    private val _contact = _sortType.flatMapLatest { sortType->
        when(sortType){
            SortType.IDS -> dao.getTransaksiOrderdById()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state,_sortType,_contact) {state,sortType,contacts ->
        state.copy(
            Transaksi = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TransaksiState())


    fun onEvent(event: TransaksiEvent){
        when(event){

            TransaksiEvent.HideDialog ->
                _state.update { it.copy(isAddingTransaksi = false) }
            is TransaksiEvent.SetBank ->
                viewModelScope.launch {
                    _state.update { it.copy(bank = event.Bank) }
                }
            is TransaksiEvent.SetIdTransaksi ->
                viewModelScope.launch {
                    _state.update { it.copy(id_transaksi = event.idTransaksi) }
                }
            is TransaksiEvent.SetMerchant ->
                viewModelScope.launch {
                    _state.update { it.copy(merchant = event.merchant) }
                }
            is TransaksiEvent.SetNominal ->
                viewModelScope.launch {
                    _state.update { it.copy(nominal = event.nominal) }
                }
//            is TransaksiEvent.SortContacts ->
//                _sortType.value=event.sortType
            TransaksiEvent.saveTransaksi -> {
                val bank = state.value.bank
                val id_transaksi = state.value.bank
                val merchant = state.value.merchant
                val nominal = state.value.nominal

                if (bank.isEmpty() || id_transaksi.isBlank() || merchant.isBlank()|| nominal.equals(0))
                {
                    return
                }

                val transaksiQr = DataTransaksiQr(
                    bank = bank,
                    id_transaksi = id_transaksi,
                    merchant = merchant,
                    nominal = nominal,
                )
                viewModelScope.launch {
                    dao.insertTransaksi(transaksiQr)
                }
                _state.update { it.copy(
                    isAddingTransaksi = false,
                    bank = "",
                    id_transaksi = "",
                    merchant = "",
                    nominal = 0,

                    ) }
            }
            TransaksiEvent.showDialog ->
                _state.update { it.copy(isAddingTransaksi = true) }

        }
    }


}