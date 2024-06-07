package com.mediseed.mediseed.ui.presentation.sprout

import android.Manifest
import android.animation.Animator
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mediseed.mediseed.databinding.FragmentSproutBinding
import com.mediseed.mediseed.ui.presentation.main.MainActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.ui.share.SharedViewModel

class SproutFragment : Fragment() {

    private var _binding: FragmentSproutBinding? = null
    private val binding get() = _binding!!
    private lateinit var sproutViewModel: SproutViewModel
    private val sharedViewMdoel: SharedViewModel by activityViewModels()

    private val mainActivity by lazy {
        activity as? MainActivity
    }

    companion object {
        fun newInstance() = SproutFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSproutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sproutViewModel = ViewModelProvider(this).get(SproutViewModel::class.java)
        setupObservers()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        with(binding) {
            sproutPillButton.setOnClickListener {
                activateFeed()
            }
            sproutShareButton.setOnClickListener {
                sproutViewModel.handleShareButtonClick()
                shareApp()
            }
            nameImageButton.setOnClickListener {
                showNameEditDialog()
            }
        }
    }

    private fun setupObservers() {
        with(sproutViewModel) {
            level.observe(viewLifecycleOwner) { level ->
                binding.levelTextView.text = "$level"
                updateSproutImage(level)
                playLevelUpAnimation()
            }
            tree.observe(viewLifecycleOwner) { tree ->
                binding.treeTextView.text = "$tree"
            }
            pillRest.observe(viewLifecycleOwner) { pillRest ->
                binding.pillRestText.text = "$pillRest 남음"
            }
            shareRest.observe(viewLifecycleOwner) { shareRest ->
                binding.shareRestText.text = "$shareRest 남음"
            }
            progress.observe(viewLifecycleOwner) { progress ->
                binding.progressBar.progress = progress
                binding.progressBarPercentTextView.text = "$progress%"
            }
            sproutName.observe(viewLifecycleOwner) { sproutName ->
                binding.nameTextView.text = sproutName
            }
            showPillButtonClickLimitToast.observe(viewLifecycleOwner) { show ->
                if (show == true) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sprout_pill_button_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                    showPillButtonClickLimitToast.value = false
                }
            }
            showShareButtonClickLimitToast.observe(viewLifecycleOwner) { show ->
                if (show == true) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sprout_share_button_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                    showShareButtonClickLimitToast.value = false
                }
            }
        }
    }

    private fun showNameEditDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_layout, null)
        val input = dialogView.findViewById<EditText>(R.id.dialogInput)
        val confirmButton = dialogView.findViewById<Button>(R.id.dialogConfirmButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.dialogCancelButton)
        input.setText(binding.nameTextView.text)
        val dialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
            .setView(dialogView)
            .create()

        confirmButton.setOnClickListener {
            val newName = input.text.toString()
            if (newName.isNotBlank()) {
                sproutViewModel.updateSproutName(newName)
            }
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
        input.requestFocus()
    }

    private fun updateSproutImage(level: Int) {
        val imageResource = when (level) {
            1 -> R.drawable.img_tree1
            2 -> R.drawable.img_tree2
            3 -> R.drawable.img_tree3
            4 -> R.drawable.img_tree4
            5 -> R.drawable.img_tree5
            else -> R.drawable.img_tree1
        }
        binding.sproutImageView.setImageResource(imageResource)
    }

    private fun getData(): Boolean? {
        return sharedViewMdoel.nearDistance.value
    }
    private fun activateFeed() {
        if (getData() == true) {
            sproutViewModel.handlePillButtonClick()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.sprout_distance_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, getString(R.string.sprout_share_message))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.sprout_share_title)))
    }





private fun playLevelUpAnimation() {
    binding.levelUpAnimationView.apply {
        visibility = View.VISIBLE
        setMinAndMaxFrame(0, 70)
        playAnimation()
        addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
}

override fun onResume() {
    super.onResume()
    mainActivity?.hideBar()
}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
