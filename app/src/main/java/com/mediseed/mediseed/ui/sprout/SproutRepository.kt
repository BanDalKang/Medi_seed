package com.mediseed.mediseed.ui.sprout

import android.content.Context
import android.content.SharedPreferences

class SproutRepository(context: Context) {

    companion object {
        const val LAST_PILL_CLICK_DATE_KEY = "last_pill_click_date"
        const val LAST_SHARE_CLICK_DATE_KEY = "last_share_click_date"
        const val PILL_CLICK_COUNT_KEY = "pill_click_count"
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
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLevel() = sharedPreferences.getInt(LEVEL_KEY, 1)
    fun getTree() = sharedPreferences.getInt(TREE_KEY, 0)
    fun getPillRest() = sharedPreferences.getInt(PILL_REST_KEY, 1)
    fun getShareRest() = sharedPreferences.getInt(SHARE_REST_KEY, 3)
    fun getProgress() = sharedPreferences.getInt(PROGRESS_KEY, 0)
    fun getSproutName() = sharedPreferences.getString(SPROUT_NAME_KEY, "새싹이") ?: "새싹이"
    fun getLastPillClickDate() = sharedPreferences.getString(LAST_PILL_CLICK_DATE_KEY, "") ?: ""
    fun getLastShareClickDate() = sharedPreferences.getString(LAST_SHARE_CLICK_DATE_KEY, "") ?: ""
    fun getPillClickCount() = sharedPreferences.getInt(PILL_CLICK_COUNT_KEY, 0)
    fun getShareClickCount() = sharedPreferences.getInt(SHARE_CLICK_COUNT_KEY, 0)

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
        with(sharedPreferences.edit()) {
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