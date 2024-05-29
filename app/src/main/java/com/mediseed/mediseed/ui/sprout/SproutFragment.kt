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
import com.mediseed.mediseed.ui.storage.StorageFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SproutFragment : Fragment() {

    private var _binding: FragmentSproutBinding? = null
    private val binding get() = _binding!!
    private lateinit var sproutViewModel: SproutViewModel

    private var level = 1
    private var tree = 0



    companion object {

        private const val LAST_PILL_CLICK_TIME_KEY = "last_pill_click_time"
        private const val LAST_SHARE_CLICK_DATE_KEY = "last_share_click_date"
        private const val SHARE_CLICK_COUNT_KEY = "share_click_count"
        private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000
        private const val MAX_SHARE_CLICKS_PER_DAY = 3
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
            }

            sproutShareButton.setOnClickListener {
//                handleShareButtonClick()
                shareApp()
                updateProgress(10)

            }

            nameImageButton.setOnClickListener {
                showNameEditDialog()
            }
        }
    }

    private fun setupProgressBar() {
        with(binding) {
            progressBar.progress = 0
            progressBarPercentTextView.text = "0%"
            levelTextView.text = "$level"
            treeTextView.text = "$tree"
        }
        updateSproutImage()
    }

    private fun updateProgress(increment: Int) {
        with(binding) {
            var progress = progressBar.progress + increment

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
        } else {
            Toast.makeText(requireContext(), "하루에 세 번만 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }

}