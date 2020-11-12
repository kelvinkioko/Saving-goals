package com.savings.savinggoals.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.savings.savinggoals.database.dao.CurrencyDao
import com.savings.savinggoals.database.dao.GoalDao
import com.savings.savinggoals.database.entity.CurrencyEntity
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.util.Converters

@Database(entities = [CurrencyEntity::class, GoalEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SavingGoalDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: SavingGoalDatabase? = null

        fun getDatabase(context: Context): SavingGoalDatabase {
            var tempInstance = INSTANCE
            if (tempInstance == null) {
                tempInstance = Room.databaseBuilder(context.applicationContext,
                        SavingGoalDatabase::class.java, "saving_goal_schema")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return tempInstance
        }
    }
}
