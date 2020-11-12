package com.savings.savinggoals.database.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalTypeEntity(
    var goalTypeID: String,
    var goalType: String,
    var description: String
) : Parcelable
