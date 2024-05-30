package com.mediseed.mediseed.ui.sprout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentSproutBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SproutFragment : Fragment() {

    private var _binding: FragmentSproutBinding? = null
    private val binding get() = _binding!!
    private lateinit var sproutViewModel: SproutViewModel

    private var level = 1
    private var tree = 0
    private var pillRest = 1
    private var shareRest = 3
    private var progress = 0
    private var sproutName = "새싹이"



    companion object {

        private const val LAST_PILL_CLICK_TIME_KEY = "last_pill_click_time"
        private const val LAST_SHARE_CLICK_DATE_KEY = "last_share_click_date"
        private const val SHARE_CLICK_COUNT_KEY = "share_click_count"
        private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000
        private const val MAX_SHARE_CLICKS_PER_DAY = 3
        private const val PREFS_NAME = "SproutPreferences"
        private const val LEVEL_KEY = "level"
        private const val TREE_KEY = "tree"
        private const val PILL_REST_KEY = "pill_rest"
        private const val SHARE_REST_KEY = "share_rest"
        private const val PROGRESS_KEY = "progress"
        private const val SPROUT_NAME_KEY = "sprout_name"
        fun newInstance() = SproutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSproutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPreferences()
        setupListeners()
        setupProgressBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        with(binding) {
            sproutPillButton.setOnClickListener {
//                handlePillButtonClick()
                updateProgress(20)
                pillRest -= 1
                pillRestText.text = "$pillRest 남음"
                savePreferences()
            }

            sproutShareButton.setOnClickListener {
//                handleShareButtonClick()
                shareApp()
                updateProgress(10)
                shareRest -= 1
                shareRestText.text = "$shareRest 남음"
                savePreferences()

            }

            nameImageButton.setOnClickListener {
                showNameEditDialog()
            }
        }
    }

    private fun setupProgressBar() {
        with(binding) {
            progressBar.progress = progress
            progressBarPercentTextView.text = "$progress%"
            levelTextView.text = "$level"
            treeTextView.text = "$tree"
            pillRestText.text = "$pillRest 남음"
            shareRestText.text = "$shareRest 남음"
            nameTextView.text = sproutName
        }
        updateSproutImage()
    }

    private fun updateProgress(increment: Int) {
        with(binding) {
            progress += increment

            if (progress >= 100) {
                level += 1
                progress = 0
                if (level > 5) {
                    tree += 1
                    level = 1
                    treeTextView.text = "$tree"
                }
                levelTextView.text = "$level"
                updateSproutImage()
            }

            progressBar.progress = progress
            progressBarPercentTextView.text = "$progress%"

            savePreferences()
        }
    }

    private fun showNameEditDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("이름 변경")

        val input = EditText(requireContext())
        input.setText(binding.nameTextView.text)
        builder.setView(input)

        builder.setPositiveButton("확인") { dialog, _ ->
            val newName = input.text.toString()
            if (newName.isNotBlank()) {
                binding.nameTextView.text = newName
                sproutName = newName
                savePreferences()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun updateSproutImage() {
        val imageResource = when (level) {
            1 -> R.drawable.tree1
            2 -> R.drawable.tree2
            3 -> R.drawable.tree3
            4 -> R.drawable.tree4
            5 -> R.drawable.tree5
            else -> R.drawable.tree1 // Default image
        }
        binding.sproutImageView.setImageResource(imageResource)
    }

    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "새싹이통") // 공유할 내용의 제목
            putExtra(Intent.EXTRA_TEXT, "우리동네 폐의약품 수거함 '새싹이통'에서 확인하세요!") // 공유할 내용
        }
        startActivity(Intent.createChooser(shareIntent, "앱 공유하기"))
    }

    private fun handlePillButtonClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("SproutPreferences", Context.MODE_PRIVATE)
        val lastPillClickTime = sharedPreferences.getLong(LAST_PILL_CLICK_TIME_KEY, 0)
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastPillClickTime >= ONE_DAY_IN_MILLIS) {
            updateProgress(20)
            sharedPreferences.edit().putLong(LAST_PILL_CLICK_TIME_KEY, currentTime).apply()
            pillRest -= 1
            binding.pillRestText.text = "$pillRest 남음"
            savePreferences()
        } else {
            Toast.makeText(requireContext(), "하루에 한 번만 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleShareButtonClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("SproutPreferences", Context.MODE_PRIVATE)
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastShareClickDate = sharedPreferences.getString(LAST_SHARE_CLICK_DATE_KEY, "")
        var shareClickCount = sharedPreferences.getInt(SHARE_CLICK_COUNT_KEY, 0)

        if (currentDate != lastShareClickDate) {
            // 날짜가 변경된 경우 클릭 카운트 리셋
            shareClickCount = 0
            sharedPreferences.edit()
                .putString(LAST_SHARE_CLICK_DATE_KEY, currentDate)
                .putInt(SHARE_CLICK_COUNT_KEY, shareClickCount)
                .apply()
        }

        if (shareClickCount < MAX_SHARE_CLICKS_PER_DAY) {
            shareApp()
            updateProgress(10)
            shareClickCount++
            sharedPreferences.edit().putInt(SHARE_CLICK_COUNT_KEY, shareClickCount).apply()
            shareRest -= 1
            binding.shareRestText.text = "$shareRest 남음"
            savePreferences()
        } else {
            Toast.makeText(requireContext(), "하루에 세 번만 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        level = sharedPreferences.getInt(LEVEL_KEY, 1)
        tree = sharedPreferences.getInt(TREE_KEY, 0)
        pillRest = sharedPreferences.getInt(PILL_REST_KEY, 1)
        shareRest = sharedPreferences.getInt(SHARE_REST_KEY, 3)
        progress = sharedPreferences.getInt(PROGRESS_KEY, 0)
        sproutName = sharedPreferences.getString(SPROUT_NAME_KEY, "새싹이") ?: ""
    }

    private fun savePreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(LEVEL_KEY, level)
            putInt(TREE_KEY, tree)
            putInt(PILL_REST_KEY, pillRest)
            putInt(SHARE_REST_KEY, shareRest)
            putInt(PROGRESS_KEY, progress)
            putString(SPROUT_NAME_KEY, sproutName)
            apply()
        }
    }

}