package com.mediseed.mediseed.ui.sprout

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediseed.mediseed.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SproutViewModel @Inject constructor(private val pref: SharedPreferences) : ViewModel() {

    private val _level = MutableLiveData<Int>().apply { value = getLevel() }
    val level: LiveData<Int> get() = _level

    private val _tree = MutableLiveData<Int>().apply { value = getTree() }
    val tree: LiveData<Int> get() = _tree

    private val _pillRest = MutableLiveData<Int>().apply { value = getPillRest() }
    val pillRest: LiveData<Int> get() = _pillRest

    private val _shareRest = MutableLiveData<Int>().apply { value = getShareRest() }
    val shareRest: LiveData<Int> get() = _shareRest

    private val _progress = MutableLiveData<Int>().apply { value = getProgress() }
    val progress: LiveData<Int> get() = _progress

    private val _sproutName = MutableLiveData<String>().apply { value = getSproutName() }
    val sproutName: LiveData<String> get() = _sproutName

    private val _lastShareClickDate = MutableLiveData<String>().apply { value = getLastShareClickDate() }
    val lastShareClickDate: LiveData<String> get() = _lastShareClickDate

    private val _lastPillClickDate = MutableLiveData<String>().apply { value = getLastPillClickDate() }
    val lastPillClickDate: LiveData<String> get() = _lastPillClickDate

    private val _pillClickCount = MutableLiveData<Int>().apply { value = getPillClickCount() }
    val pillClickCount: LiveData<Int> get() = _pillClickCount

    private val _shareClickCount = MutableLiveData<Int>().apply { value = getShareClickCount() }
    val shareClickCount: LiveData<Int> get() = _shareClickCount

    private val _showTreeUpDialog = MutableLiveData<Event<Unit>>()
    val showTreeUpDialog: LiveData<Event<Unit>> get() = _showTreeUpDialog

    private val _showLevelUpAnimation = MutableLiveData<Event<Unit>>()
    val showLevelUpAnimation: LiveData<Event<Unit>> get() = _showLevelUpAnimation

    private val _showProgressAnimation = MutableLiveData<Event<Unit>>()
    val showProgressAnimation: LiveData<Event<Unit>> get() = _showProgressAnimation
    private val _showPillClickAnimation = MutableLiveData<Event<Unit>>()
    val showPillClickAnimation: LiveData<Event<Unit>> get() = _showPillClickAnimation
    private val _showShareClickAnimation = MutableLiveData<Event<Unit>>()
    val showShareClickAnimation: LiveData<Event<Unit>> get() = _showShareClickAnimation

    private var isProgressUpdating = false

    init {
        lastShareClickDateCheck()
        lastPillClickDateCheck()
    }

    fun setInitialValues() {
        _pillClickCount.value = getPillClickCount()
        _shareClickCount.value = getShareClickCount()
    }

    fun handlePillButtonClick() {
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val pillClickCount = _pillClickCount.value ?: 0
        if (_pillRest.value == 1) {
            updateProgress(100)
            _lastPillClickDate.value = currentDate
            _pillRest.value = 0
            _pillClickCount.value = pillClickCount + 1
            _showPillClickAnimation.value = Event(Unit)
            savePreferences()
        } else {
            showPillButtonClickLimitToast.value = true
        }
    }

    fun handleShareButtonClick() {
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val shareClickCount = _shareClickCount.value ?: 0
        if ((_shareRest.value ?: 3) > 0) {
            updateProgress(50)
            _lastShareClickDate.value = currentDate
            _shareRest.value = (_shareRest.value ?: 3) - 1
            _shareClickCount.value = shareClickCount + 1
            _showShareClickAnimation.value = Event(Unit)
            savePreferences()
        } else {
            showShareButtonClickLimitToast.value = true
        }
    }

    private fun updateProgress(increment: Int) {
        if (isProgressUpdating) return

        viewModelScope.launch {
            isProgressUpdating = true
            val currentProgress = _progress.value ?: 0
            var level = _level.value ?: 1
            var maxProgress = level * 100
            var targetProgress = currentProgress + increment

            _showProgressAnimation.value = Event(Unit)

            for (i in currentProgress until targetProgress.coerceAtMost(maxProgress)) {
                delay(10)
                _progress.value = i + 1
            }

            while (targetProgress >= maxProgress) {
                level += 1
                _level.value = level
                _showLevelUpAnimation.value = Event(Unit)
                targetProgress -= maxProgress
                maxProgress = level * 100

                if (level > 5) {
                    _tree.value = (_tree.value ?: 0) + 1
                    _showTreeUpDialog.value = Event(Unit)
                    _level.value = 1
                    targetProgress = 0
                    break
                }
            }

            _progress.value = targetProgress
            savePreferences()
            isProgressUpdating = false
        }
    }

    fun updateSproutName(newName: String) {
        _sproutName.value = newName
        savePreferences()
    }

    private fun lastPillClickDateCheck() {
        val lastPillClickDate = _lastPillClickDate.value ?: ""
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (currentDate != lastPillClickDate) {
            _pillRest.value = 1
            _lastPillClickDate.value = currentDate
            savePreferences()
        }
    }

    private fun lastShareClickDateCheck() {
        val lastShareClickDate = _lastShareClickDate.value ?: ""
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (currentDate != lastShareClickDate) {
            _shareRest.value = 3
            _lastShareClickDate.value = currentDate
            savePreferences()
        }
    }

    private fun savePreferences() {
        saveData(
            _level.value ?: 1,
            _tree.value ?: 0,
            _pillRest.value ?: 1,
            _shareRest.value ?: 3,
            _progress.value ?: 0,
            _sproutName.value ?: "새싹이",
            _lastPillClickDate.value ?: "",
            _lastShareClickDate.value ?: "",
            _pillClickCount.value ?: 0,
            _shareClickCount.value ?: 0
        )
    }

    val showPillButtonClickLimitToast = MutableLiveData<Boolean>()
    val showShareButtonClickLimitToast = MutableLiveData<Boolean>()

    companion object {
        const val LAST_PILL_CLICK_DATE_KEY = "last_pill_click_date"
        const val LAST_SHARE_CLICK_DATE_KEY = "last_share_click_date"
        const val PILL_CLICK_COUNT_KEY = "pill_click_count"
        const val SHARE_CLICK_COUNT_KEY = "share_click_count"
        const val LEVEL_KEY = "level"
        const val TREE_KEY = "tree"
        const val PILL_REST_KEY = "pill_rest"
        const val SHARE_REST_KEY = "share_rest"
        const val PROGRESS_KEY = "progress"
        const val SPROUT_NAME_KEY = "sprout_name"
    }

    fun getLevel() = pref.getInt(LEVEL_KEY, 1)
    fun getTree() = pref.getInt(TREE_KEY, 0)
    fun getPillRest() = pref.getInt(PILL_REST_KEY, 1)
    fun getShareRest() = pref.getInt(SHARE_REST_KEY, 3)
    fun getProgress() = pref.getInt(PROGRESS_KEY, 0)
    fun getSproutName() = pref.getString(SPROUT_NAME_KEY, "새싹이") ?: "새싹이"
    fun getLastPillClickDate() = pref.getString(LAST_PILL_CLICK_DATE_KEY, "") ?: ""
    fun getLastShareClickDate() = pref.getString(LAST_SHARE_CLICK_DATE_KEY, "") ?: ""
    fun getPillClickCount() = pref.getInt(PILL_CLICK_COUNT_KEY, 0)
    fun getShareClickCount() = pref.getInt(SHARE_CLICK_COUNT_KEY, 0)

    fun saveData(
        level: Int,
        tree: Int,
        pillRest: Int,
        shareRest: Int,
        progress: Int,
        sproutName: String,
        lastPillClickDate: String,
        lastShareClickDate: String,
        pillClickCount: Int,
        shareClickCount: Int
    ) {
        with(pref.edit()) {
            putInt(LEVEL_KEY, level)
            putInt(TREE_KEY, tree)
            putInt(PILL_REST_KEY, pillRest)
            putInt(SHARE_REST_KEY, shareRest)
            putInt(PROGRESS_KEY, progress)
            putString(SPROUT_NAME_KEY, sproutName)
            putString(LAST_PILL_CLICK_DATE_KEY, lastPillClickDate)
            putString(LAST_SHARE_CLICK_DATE_KEY, lastShareClickDate)
            putInt(PILL_CLICK_COUNT_KEY, pillClickCount)
            putInt(SHARE_CLICK_COUNT_KEY, shareClickCount)
            apply()
        }
    }
}