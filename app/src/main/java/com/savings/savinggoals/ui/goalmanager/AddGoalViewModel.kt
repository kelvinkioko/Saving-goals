package com.savings.savinggoals.ui.goalmanager

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.constants.Hive
import com.savings.savinggoals.constants.formatDateHeader
import com.savings.savinggoals.constants.getCurrentDateAsDate
import com.savings.savinggoals.constants.getCurrentDateAsString
import com.savings.savinggoals.constants.getEndDateAsString
import com.savings.savinggoals.constants.getWeeksOfMonth
import com.savings.savinggoals.database.entity.GoalEntity
import com.savings.savinggoals.database.entity.GoalSavingEntity
import com.savings.savinggoals.database.entity.GoalTypeEntity
import com.savings.savinggoals.database.repository.GoalRepository
import com.savings.savinggoals.ui.goalmanager.image.ImageUpdateBottomSheet
import com.savings.savinggoals.ui.goalmanager.type.GoalTypeBottomSheet
import com.savings.savinggoals.util.Event
import com.savings.savinggoals.util.asEvent
import com.savings.savinggoals.util.getBitmapFormUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGoalViewModel(private val goalRepository: GoalRepository) : ViewModel() {
    private val _uiState = MutableLiveData<AddGoalUIState>()
    val uiState: LiveData<AddGoalUIState> = _uiState

    private val _action = MutableLiveData<Event<AddGoalActions>>()
    val action: LiveData<Event<AddGoalActions>> = _action

    private lateinit var goalTypeEntity: GoalTypeEntity
    private lateinit var startDate: String
    private lateinit var endDate: String
    private var bitmapUri: Uri? = null

    init {
        setupDefaultGoalType()
    }

    private fun setupDefaultGoalType() {
        goalTypeEntity = GoalTypeEntity(
            goalTypeID = "GT52",
            goalType = "52 Week saving",
            description = "This is a 52 week saving goal where you save weekly and the amount to save increases each week by a certain set amount."
        )
        startDate = getCurrentDateAsString()
        val splitStartDate = startDate.split("/")
        endDate = getEndDateAsString(day = splitStartDate[0].toInt(), month = splitStartDate[1].toInt(), year = splitStartDate[2].toInt())

        _uiState.postValue(AddGoalUIState.PageSetup(goalTypeEntity = goalTypeEntity, startDate = formatDateHeader(startDate), endDate = formatDateHeader(endDate)))
    }

    fun setupEndDate(endDate: String) {
        this.endDate = endDate
    }

    fun setupStartDate(startDate: String) {
        this.startDate = startDate

        if (this::goalTypeEntity.isInitialized && goalTypeEntity.goalTypeID.equals("GT52", ignoreCase = true)) {
            val splitStartDate = startDate.split("/")
            endDate = getEndDateAsString(day = splitStartDate[0].toInt(), month = splitStartDate[1].toInt(), year = splitStartDate[2].toInt())

            _uiState.postValue(AddGoalUIState.UpdateEndDate(endDate = formatDateHeader(endDate)))
        }
    }

    fun setup52WeekTarget(startAmount: String, incrementalAmount: String) {
        var totalAmount = 0.0f
        if (this::startDate.isInitialized) {
            for (i in 0 until 52) {
                val amount = if (i > 0) startAmount.toFloat() + (incrementalAmount.toFloat() * i.toFloat()) else startAmount.toFloat()
                totalAmount += amount
            }
        }
        _uiState.postValue(AddGoalUIState.TargetAmount(targetAmount = totalAmount.toString()))
    }

    fun setup52WeekRange(startAmount: String, incrementalAmount: String) {
        var totalAmount = 0.0f
        if (this::startDate.isInitialized) {
            val dateArray = startDate.split("/")
            val weeksRange = getWeeksOfMonth(day = dateArray[0].toInt(), month = dateArray[1].toInt(), year = dateArray[2].toInt())

            for (i in weeksRange.indices) {
                val amount = if (i > 0) startAmount.toFloat() + incrementalAmount.toFloat() else startAmount.toFloat()
                totalAmount += amount
            }
        }
        _uiState.postValue(AddGoalUIState.TargetAmount(targetAmount = totalAmount.toString()))
    }

    fun chooseGoalType() {
        val bottomSheetFragment = GoalTypeBottomSheet(
            goalTypeID = goalTypeEntity.goalTypeID,
            resendGoalTypeCallback = {
                this.goalTypeEntity = it
                _uiState.postValue(AddGoalUIState.GoalType(it))
            }
        )
        _action.postValue(AddGoalActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun chooseCoverPhoto() {
        val bottomSheetFragment = ImageUpdateBottomSheet(
            resendGoalTypeCallback = {
                this.bitmapUri = it
                _uiState.postValue(AddGoalUIState.DisplayBitmap(it))
            }
        )
        _action.postValue(AddGoalActions.BottomNavigate(bottomSheetFragment).asEvent())
    }

    fun removeCoverPhoto() {
        bitmapUri = null
        _uiState.postValue(AddGoalUIState.CoverPhotoRemoved)
    }

    fun saveGoal(activity: Activity, goalName: String, goalDescription: String, startAmount: String, targetAmount: String, incrementalAmount: String, currency: String) {
        viewModelScope.launch() {
            withContext(Dispatchers.IO) {
                val coverImage = if (bitmapUri != null) {
                    getBitmapFormUri(activity = activity, uri = bitmapUri)
                } else {
                    ""
                }

                val goalID = "GOAL-${Hive().getTimestamp()}"
                val goal = GoalEntity(
                    id = 0,
                    goalID = goalID,
                    name = goalName,
                    description = goalDescription,
                    currency = currency,
                    amount = "0.00",
                    target_amount = targetAmount,
                    increment_amount = incrementalAmount,
                    start_date = startDate,
                    target_date = endDate,
                    type = "${goalTypeEntity.goalTypeID}#${goalTypeEntity.goalType}",
                    image = coverImage,
                    goal_date = getCurrentDateAsDate(),
                    createdAt = getCurrentDateAsString()
                )
                goalRepository.insertGoal(goal)

                if (goalTypeEntity.goalTypeID.equals("GT52", ignoreCase = true)) {
                    var totalAmount = 0.0f
                    if (::startDate.isInitialized) {
                        val dateArray = startDate.split("/")
                        val weeksRange = getWeeksOfMonth(day = dateArray[0].toInt(), month = dateArray[1].toInt(), year = dateArray[2].toInt())

                        for (i in weeksRange.indices) {
                            val amount = if (i > 0) startAmount.toFloat() + (incrementalAmount.toFloat() * i) else startAmount.toFloat()
                            totalAmount += amount

                            val goalSavingEntity = GoalSavingEntity(
                                id = 0,
                                savingID = "GSE-${Hive().getTimestamp()}-$i",
                                goalID = goalID,
                                weekPosition = "Week ${i + 1} of 52",
                                startDate = weeksRange[i].startDate,
                                endDate = weeksRange[i].endDate,
                                amount = amount.toString(),
                                note = "",
                                save_type = "Deposit",
                                save_status = "Pending", // Can either be pending or Complete
                                goal_date = getCurrentDateAsDate(),
                                createdAt = getCurrentDateAsString()
                            )
                            goalRepository.insertGoalSaving(goalSavingEntity = goalSavingEntity)
                        }
                        goalRepository.updateTargetAmount(goalID = goalID, target_amount = totalAmount.toString())
                    }
                }

                _uiState.postValue(AddGoalUIState.Success)
            }
        }
    }
}

sealed class AddGoalActions {
    data class BottomNavigate(val bottomSheetDialogFragment: BottomSheetDialogFragment) : AddGoalActions()

    data class Navigate(val destination: NavDirections) : AddGoalActions()
}

sealed class AddGoalUIState {
    object Success : AddGoalUIState()

    object CoverPhotoRemoved : AddGoalUIState()

    data class PageSetup(val goalTypeEntity: GoalTypeEntity, val startDate: String, val endDate: String) : AddGoalUIState()

    data class DisplayBitmap(val bitmap: Uri) : AddGoalUIState()

    data class GoalType(val goalTypeEntity: GoalTypeEntity) : AddGoalUIState()

    data class TargetAmount(val targetAmount: String) : AddGoalUIState()

    data class UpdateEndDate(val endDate: String) : AddGoalUIState()
}
