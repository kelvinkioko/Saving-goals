package com.savings.savinggoals.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.savings.savinggoals.R
import com.savings.savinggoals.databinding.SettingsFragmentBinding
import com.savings.savinggoals.util.drive.DriveServiceHelper
import com.savings.savinggoals.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val binding by viewBinding(SettingsFragmentBinding::bind)

    private val viewModel: SettingsViewModel by viewModel()

    private val requestCodeSignin = 100
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mDriveServiceHelper: DriveServiceHelper
    private val TAG = "MainActivity"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupClickListeners()
    }

    private fun setupToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            currencyTypes.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.toCurrencyFragment())
            }
            tellAFriend.setOnClickListener {
                viewModel.shareApp(requireActivity())
            }
            ratingReview.setOnClickListener {
                viewModel.rateApp(requireActivity())
            }
            backUpContent.setOnClickListener {
                val account = GoogleSignIn.getLastSignedInAccount(requireContext())
                if (account == null) {
                    signIn()
                } else {
                    mDriveServiceHelper = DriveServiceHelper(
                        getGoogleDriveService(requireContext(), account, getString(R.string.app_name))
                    )
                }
                viewModel.rateApp(requireActivity())
            }
            restoreContent.setOnClickListener {
                viewModel.rateApp(requireActivity())
            }
        }
    }

    private fun signIn() {
        mGoogleSignInClient = buildGoogleSignInClient()
        startActivityForResult(mGoogleSignInClient.signInIntent, requestCodeSignin)
    }

    private fun buildGoogleSignInClient(): GoogleSignInClient {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(requireContext(), signInOptions)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        when (requestCode) {
            requestCodeSignin -> {
                if (resultCode == AppCompatActivity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, resultData)
    }

    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleSignInAccount ->
                println("Signed in as " + googleSignInAccount.email)
                mDriveServiceHelper = DriveServiceHelper(
                    getGoogleDriveService(
                        requireContext(),
                        googleSignInAccount,
                        getString(R.string.app_name)
                    )
                )
            }
            .addOnFailureListener { e -> println("Unable to sign in.") }
    }

    private fun getGoogleDriveService(context: Context?, account: GoogleSignInAccount, appName: String?): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, setOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account
        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        )
            .setApplicationName(appName)
            .build()
    }
}
