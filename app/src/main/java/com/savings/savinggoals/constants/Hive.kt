package com.savings.savinggoals.constants

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentDateAsString(): String {
    val calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("dd/MM/yyyy")
    return mdformat.format(calendar.time)
}

class Hive {

    fun getCurrentMonth(currentMonth: String = "", toFuture: Boolean = false, toPast: Boolean = false): Pair<String, String>? {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val displayFormat = SimpleDateFormat("MMM, yyyy")
        @SuppressLint("SimpleDateFormat") val dateFormat = SimpleDateFormat("MM/yyyy")
        if (currentMonth.isNotEmpty()) {
            val date: Date = displayFormat.parse(currentMonth)!!
            calendar.time = date
            if (toFuture) {
                calendar.add(Calendar.MONTH, 1)
            } else if (toPast) {
                calendar.add(Calendar.MONTH, -1)
            }
        }
        return Pair<String, String>(displayFormat.format(calendar.time), "${dateFormat.format(calendar.time)}")
    }

    fun getCurrentYear(currentYear: String = "", toFuture: Boolean = false, toPast: Boolean = false): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val displayFormat = SimpleDateFormat("yyyy")
        if (currentYear.isNotEmpty()) {
            val date: Date = displayFormat.parse(currentYear)!!
            calendar.time = date
            if (toFuture) {
                calendar.add(Calendar.YEAR, 1)
            } else if (toPast) {
                calendar.add(Calendar.YEAR, -1)
            }
        }
        return displayFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromString(stringDate: String): Date {
        val finalFormat = SimpleDateFormat("dd/MM/yyyy")
        var date: Date? = null
        try {
            date = finalFormat.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date!!
    }

    fun getStringFromDate(date: Date): String {
        @SuppressLint("SimpleDateFormat") val finalFormat = SimpleDateFormat("dd/MM/yyyy")
        return finalFormat.format(date)
    }

    fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return mdformat.format(calendar.time)
    }

    fun getTimestamp(): String {
        val calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val mdformat = SimpleDateFormat("yyyyMMdd_HHmmss")
        return mdformat.format(calendar.time)
    }

    fun formatCurrency(amount: Float): String {
        val df = DecimalFormat("#,###,###,###,###.00")
        df.minimumFractionDigits = 2
        return df.format(amount)
    }

    fun loadWeeks() {
        val cal = Calendar.getInstance()
        for (i in 0..11) {
            cal[Calendar.YEAR] = 2020
            cal[Calendar.DAY_OF_MONTH] = 1
            cal[Calendar.MONTH] = i
            val maxWeekNumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH)
            println("loadWeeks For Month :: $i Num Week :: $maxWeekNumber")
        }
    }

    fun getWeekRange(year: Int, week_no: Int): Pair<String, String> {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        cal[Calendar.YEAR] = year
        cal[Calendar.WEEK_OF_YEAR] = week_no
        val sunday = cal.time
        cal.add(Calendar.DATE, 6)
        val saturday = cal.time
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return Pair<String, String>(sdf.format(sunday), sdf.format(saturday))
    }

    fun getWeeksOfMonth(month: Int, year: Int): List<WeekRangeEntity> {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month - 1
        cal[Calendar.DAY_OF_MONTH] = 1
        val monthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val positionOfWeekOfYear: ArrayList<String> = ArrayList()
        val weekRange = mutableListOf<WeekRangeEntity>()
        for (i in 0 until monthDays) {
            cal.set(year, month - 1, i)
            val weekOfYear: Int = cal.get(Calendar.WEEK_OF_YEAR)

            if (!positionOfWeekOfYear.contains("$weekOfYear")) {
                positionOfWeekOfYear.add("$weekOfYear")
                val range = getWeekRange(year, cal[Calendar.WEEK_OF_YEAR])
                weekRange.add(WeekRangeEntity(
                    weekPosition = weekOfYear.toString(),
                    startDate = range.first,
                    endDate = range.second
                ))
            }
            cal.add(Calendar.DATE, 1)
        }
        return weekRange
    }
}

fun getMonthGivenNumber(monthNumber: String): String {
    var month = ""
    when {
        monthNumber.equals("1", ignoreCase = true) -> {
            month = "January"
        }
        monthNumber.equals("2", ignoreCase = true) -> {
            month = "February"
        }
        monthNumber.equals("3", ignoreCase = true) -> {
            month = "March"
        }
        monthNumber.equals("4", ignoreCase = true) -> {
            month = "April"
        }
        monthNumber.equals("5", ignoreCase = true) -> {
            month = "May"
        }
        monthNumber.equals("6", ignoreCase = true) -> {
            month = "June"
        }
        monthNumber.equals("7", ignoreCase = true) -> {
            month = "July"
        }
        monthNumber.equals("8", ignoreCase = true) -> {
            month = "August"
        }
        monthNumber.equals("9", ignoreCase = true) -> {
            month = "September"
        }
        monthNumber.equals("10", ignoreCase = true) -> {
            month = "October"
        }
        monthNumber.equals("11", ignoreCase = true) -> {
            month = "November"
        }
        else -> {
            month = "December"
        }
    }
    return month
}

fun getShortMonthGivenNumber(monthNumber: String): String {
    var month = ""
    when {
        monthNumber.equals("01", ignoreCase = true) -> month = "Jan"
        monthNumber.equals("02", ignoreCase = true) -> month = "Feb"
        monthNumber.equals("03", ignoreCase = true) -> month = "Mar"
        monthNumber.equals("04", ignoreCase = true) -> month = "Apr"
        monthNumber.equals("05", ignoreCase = true) -> month = "May"
        monthNumber.equals("06", ignoreCase = true) -> month = "Jun"
        monthNumber.equals("07", ignoreCase = true) -> month = "Jul"
        monthNumber.equals("08", ignoreCase = true) -> month = "Aug"
        monthNumber.equals("09", ignoreCase = true) -> month = "Sep"
        monthNumber.equals("10", ignoreCase = true) -> month = "Oct"
        monthNumber.equals("11", ignoreCase = true) -> month = "Nov"
        monthNumber.equals("12", ignoreCase = true) -> month = "Dec"
    }
    return month
}

data class WeekRangeEntity(
    var weekPosition: String,
    var startDate: String,
    var endDate: String
)

fun getCurrentDateAsDate(): Date {
    val calendar = Calendar.getInstance()
    return calendar.time
}

@SuppressLint("SimpleDateFormat")
fun formatDateHeader(date: String): String {
    val inFormat = SimpleDateFormat("dd/MM/yyyy")
    val input: Date = inFormat.parse(date)
    val weekDayFormat = SimpleDateFormat("EEEE").format(input)
    val dayFormat = SimpleDateFormat("dd").format(input)
    val monthFormat = SimpleDateFormat("MMM").format(input)
    val yearFormat = SimpleDateFormat("yyyy").format(input)

    return "$dayFormat, $monthFormat $yearFormat"
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
