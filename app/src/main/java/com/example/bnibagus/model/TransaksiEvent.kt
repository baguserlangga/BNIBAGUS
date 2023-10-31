package com.example.bnibagus.model

sealed interface TransaksiEvent {
    object saveTransaksi : TransaksiEvent
    data class SetBank(val Bank: String) : TransaksiEvent
    data class SetIdTransaksi(val idTransaksi: String) : TransaksiEvent
    data class SetMerchant(val merchant: String): TransaksiEvent
    data class SetNominal(val nominal: Int): TransaksiEvent

    object showDialog : TransaksiEvent
    object HideDialog : TransaksiEvent

//    data class SortContacts(val sortType: SortType) : TransaksiEvent
//    data class DeleteContact(val contact: DataTransaksiQr) : TransaksiEvent
}