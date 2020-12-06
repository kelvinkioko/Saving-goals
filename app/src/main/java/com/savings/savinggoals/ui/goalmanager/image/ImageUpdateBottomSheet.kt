package com.savings.savinggoals.ui.goalmanager.image

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import coil.load
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savings.savinggoals.R
import com.savings.savinggoals.constants.PermissionListener
import com.savings.savinggoals.databinding.DialogImageManagerBinding
import com.savings.savinggoals.util.viewBinding

class ImageUpdateBottomSheet(val resendGoalTypeCallback: (Uri) -> (Unit)) : BottomSheetDialogFragment() {

    private val binding by viewBinding(DialogImageManagerBinding::bind)

    private val permissionRequestSCode = 1155

    private lateinit var bitmap: Uri

    /**
     * This function makes BottomSheetDialogFragment full screen and without collapsed state
     * For some reason this doesn't work without the params.height
     */
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_image_manager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            cancelAction.setOnClickListener {
                dismiss()
            }

            addPhotoClickable.setOnClickListener {
                PermissionListener(requireActivity()).loadPermissions()

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, permissionRequestSCode)
            }

            changePhotoAction.setOnClickListener {
                PermissionListener(requireActivity()).loadPermissions()

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, permissionRequestSCode)
            }

            saveGoal.setOnClickListener {
                resendGoalTypeCallback(bitmap)
                dismiss()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1150) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, permissionRequestSCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == permissionRequestSCode) {
            binding.apply {
                addPhotoGroup.isGone = true
                goalVisualGroup.isVisible = true
                Glide.with(requireContext()).load(data?.data).into(goalVisual) // handle chosen image
                bitmap = data?.data!!
            }
        }
    }
}
