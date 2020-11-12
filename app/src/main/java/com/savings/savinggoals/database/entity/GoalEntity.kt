package com.savings.savinggoals.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "goal")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,

    @ColumnInfo(name = "goalID") var goalID: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "currency") var currency: String,
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "target_amount") var target_amount: String,
    @ColumnInfo(name = "increment_amount") var increment_amount: String,
    @ColumnInfo(name = "start_date") var start_date: String,
    @ColumnInfo(name = "target_date") var target_date: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "goal_date") var goal_date: Date,
    @ColumnInfo(name = "createdAt") var createdAt: String
) : Parcelable
