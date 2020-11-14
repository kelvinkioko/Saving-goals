package com.savings.savinggoals.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "goal_saving")
data class GoalSavingEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "savingID") var savingID: String,
    @ColumnInfo(name = "goalID") var goalID: String,
    @ColumnInfo(name = "weekPosition") var weekPosition: String = "",
    @ColumnInfo(name = "startDate") var startDate: String = "",
    @ColumnInfo(name = "endDate") var endDate: String = "",
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "save_type") var save_type: String,
    @ColumnInfo(name = "save_status") var save_status: String,
    @ColumnInfo(name = "goal_date") var goal_date: Date,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
