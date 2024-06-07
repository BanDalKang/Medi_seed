package com.mediseed.mediseed.ui.presentation.sprout

import android.app.Application
import android.app.usage.UsageEvents
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SproutViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val LAST_PILL_CLICK_DATE_KEY = "last_pill_click_date"
        const val LAST_SHARE_CLICK_DATE_KEY = "last_share_click_date"
        const val SHARE_CLICK_COUNT_KEY = "share_click_count"
        const val PREFS_NAME = "SproutPreferences"
        const val LEVEL_KEY = "level"
        const val TREE_KEY = "tree"
        const val PILL_REST_KEY = "pill_rest"
        const val SHARE_REST_KEY = "share_rest"
        const val PROGRESS_KEY = "progress"
        const val SPROUT_NAME_KEY = "sprout_name"
    }
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _level = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(LEVEL_KEY, 1) }
    val level: LiveData<Int> get() = _level
    private val _tree = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(TREE_KEY, 0) }
    val tree: LiveData<Int> get() = _tree
    private val _pillRest = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(PILL_REST_KEY, 1) }
    val pillRest: LiveData<Int> get() = _pillRest
    private val _shareRest = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(SHARE_REST_KEY, 3) }
    val shareRest: LiveData<Int> get() = _shareRest
    private val _progress = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(PROGRESS_KEY, 0) }
    val progress: LiveData<Int> get() = _progress
    private val _sproutName = MutableLiveData<String>().apply { value = sharedPreferences.getString(SPROUT_NAME_KEY, "새싹이") }
    val sproutName: LiveData<String> get() = _sproutName
    private val _lastShareClickDate = MutableLiveData<String>().apply { value = sharedPreferences.getString(LAST_SHARE_CLICK_DATE_KEY, "") }
    val lastShareClickDate: LiveData<String> get() = _lastShareClickDate
    private val _lastPillClickDate = MutableLiveData<String>().apply { value = sharedPreferences.getString(LAST_PILL_CLICK_DATE_KEY, "") }
    val lastPillClickDate: LiveData<String> get() = _lastPillClickDate
    private val _shareClickCount = MutableLiveData<Int>().apply { value = sharedPreferences.getInt(SHARE_CLICK_COUNT_KEY, 0) }
    val shareClickCount: LiveData<Int> get() = _shareClickCount
    private val _showTreeUpDialog = MutableLiveData<Event<Unit>>()
    val showTreeUpDialog: LiveData<Event<Unit>> get() = _showTreeUpDialog
    private val _showLevelUpAnimation = MutableLiveData<Event<Unit>>()
    val showLevelUpAnimation: LiveData<Event<Unit>> get() = _showLevelUpAnimation

    init {
        lastShareClickDateCheck()
    }

    fun handlePillButtonClick() {
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        _pillRest.value = _pillRest.value ?: 1
        if (_pillRest.value == 1) {
            updateProgress(20)
            _lastPillClickDate.value = currentDate
            _pillRest.value = 0
            savePreferences()
        } else {
            showPillButtonClickLimitToast.value = true
        }
    }

    fun handleShareButtonClick() {
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val shareClickCount = _shareClickCount.value ?: 0
        _shareRest.value = _shareRest.value ?: 3
        if (_shareRest.value!! > 0) {
            updateProgress(10)
            _lastShareClickDate.value = currentDate
            _shareRest.value = (_shareRest.value ?: 3) - 1
            _shareClickCount.value = shareClickCount + 1
            savePreferences()
        } else {
            showShareButtonClickLimitToast.value = true
        }
    }

    fun updateProgress(increment: Int) {
        val newProgress = (_progress.value ?: 0) + increment
        if (newProgress >= 100) {
            _level.value = (_level.value ?: 1) + 1
            _showLevelUpAnimation.value = Event(Unit)
            _progress.value = 0
            if (_level.value ?: 1 > 5) {
                _tree.value = (_tree.value ?: 0) + 1
                _showTreeUpDialog.value = Event(Unit)
                _level.value = 1
            }
        } else {
            _progress.value = newProgress
        }
        savePreferences()
    }

    fun updateSproutName(newName: String) {
        _sproutName.value = newName
        savePreferences()
    }

    private fun lastPillClickDateCheck() {
        val lastPillClickDate = _lastPillClickDate.value ?: ""
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        // 마지막 클릭 날짜와 현재 날짜가 다른 경우 초기화
        if (currentDate != lastPillClickDate) {
            _pillRest.value = 1
            _lastPillClickDate.value = currentDate
            savePreferences()
        }
    }

    private fun lastShareClickDateCheck() {
        val lastShareClickDate = _lastShareClickDate.value ?: ""
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        // 마지막 클릭 날짜와 현재 날짜가 다른 경우 초기화
        if (currentDate != lastShareClickDate) {
            _shareRest.value = 3
            _lastShareClickDate.value = currentDate
            savePreferences()
        }
    }

    private fun savePreferences() {
        with(sharedPreferences.edit()) {
            putInt(LEVEL_KEY, _level.value ?: 1)
            putInt(TREE_KEY, _tree.value ?: 0)
            putInt(PILL_REST_KEY, _pillRest.value ?: 1)
            putInt(SHARE_REST_KEY, _shareRest.value ?: 3)
            putInt(PROGRESS_KEY, _progress.value ?: 0)
            putString(SPROUT_NAME_KEY, _sproutName.value ?: "새싹이")
            putString(LAST_PILL_CLICK_DATE_KEY, _lastPillClickDate.value ?: "")
            putString(LAST_SHARE_CLICK_DATE_KEY, _lastShareClickDate.value ?: "")
            putInt(SHARE_CLICK_COUNT_KEY, _shareClickCount.value ?: 0)
            apply()
        }
    }
    val showPillButtonClickLimitToast = MutableLiveData<Boolean>()
    val showShareButtonClickLimitToast = MutableLiveData<Boolean>()
}

class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    fun peekContent(): T = content
}
