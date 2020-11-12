package com.savings.savinggoals.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "country") var country: String,
    @ColumnInfo(name = "currency") var currency: String,
    @ColumnInfo(name = "currencyCode") var currencyCode: String,
    @ColumnInfo(name = "currencySymbol") var currencySymbol: String,
    @ColumnInfo(name = "region") var region: String
) : Parcelable
