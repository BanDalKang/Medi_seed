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
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentSproutBinding

class SproutFragment : Fragment() {

    private var _binding: FragmentSproutBinding? = null
    private val binding get() = _binding!!
    private lateinit var sproutViewModel: SproutViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationOfInterest: Location // 특정 목록의 위치

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationOfInterest = Location("").apply {
            latitude = 37.555945 // 서울역 위도
            longitude = 126.9723167 // 서울역 경도
        }
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
//                checkLocationPermissionAndClick()
                sproutViewModel.updateProgress(20) //테스트 코드
            }
            sproutShareButton.setOnClickListener {
                sproutViewModel.handleShareButtonClick()
                shareApp()
//                sproutViewModel.updateProgress(10) //테스트 코드
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
                    Toast.makeText(requireContext(), getString(R.string.sprout_pill_button_toast), Toast.LENGTH_SHORT).show()
                    showPillButtonClickLimitToast.value = false
                }
            }
            showShareButtonClickLimitToast.observe(viewLifecycleOwner) { show ->
                if (show == true) {
                    Toast.makeText(requireContext(), getString(R.string.sprout_share_button_toast), Toast.LENGTH_SHORT).show()
                    showShareButtonClickLimitToast.value = false
                }
            }
        }
    }

    private fun showNameEditDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_layout, null)
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

    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, getString(R.string.sprout_share_message))
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.sprout_share_title)))
    }

    private fun checkLocationPermissionAndClick() {
        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocationAndCheckDistance()
        } else {
            // 위치 권한이 없는 경우 요청
            requestLocationPermission()
        }
    }

    private fun getLocationAndCheckDistance() {
        // 거리 확인
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // 현재 위치와 특정 목록의 위치 사이의 거리 계산
                    val distanceInMeters = location.distanceTo(locationOfInterest)
                    if (distanceInMeters <= 100) {
                        sproutViewModel.handlePillButtonClick() // 정상 코드
//                        sproutViewModel.updateProgress(20) // 테스트 코드
                    } else {
                        // 거리가 100m 이상인 경우 안내 메시지 표시
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.sprout_distance_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.d(
                        "SproutFragment",
                        "Current location: (${location.latitude}, ${location.longitude})"
                    )
                    Log.d(
                        "SproutFragment",
                        "Location of interest: (${locationOfInterest.latitude}, ${locationOfInterest.longitude})"
                    )
                    Log.d(
                        "SproutFragment",
                        "Distance to location of interest: $distanceInMeters meters"
                    )
                } else {
                    // 현재 위치를 가져오는 데 실패한 경우
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sprout_location_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
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



}