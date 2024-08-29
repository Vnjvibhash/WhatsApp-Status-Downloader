package `in`.innovateria.wa_statussaver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import `in`.innovateria.wa_statussaver.databinding.ActivityMainBinding
import `in`.innovateria.wa_statussaver.Utils.Constants
import `in`.innovateria.wa_statussaver.Utils.SharedPrefKeys
import `in`.innovateria.wa_statussaver.Utils.SharedPrefUtils
import `in`.innovateria.wa_statussaver.Utils.replaceFragment
import `in`.innovateria.wa_statussaver.Utils.slideFromStart
import `in`.innovateria.wa_statussaver.Utils.slideToEndWithFadeOut
import `in`.innovateria.wa_statussaver.Fragments.SettingsFragment
import `in`.innovateria.wa_statussaver.Fragments.StatusFragment

class MainActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPrefUtils.init(activity)
        binding.apply {
            splashLogic()
            requestPermission()
            val whatsAppStatusFragment = StatusFragment()
            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
            replaceFragment(whatsAppStatusFragment, bundle)
            toolBar.setNavigationOnClickListener {
                drawerLayout.open()
            }
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_status -> {
                        // whatsapp status
                        val whatsAppStatusFragment = StatusFragment()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
                        replaceFragment(whatsAppStatusFragment, bundle)
                        drawerLayout.close()
                    }

                    R.id.menu_business_status -> {
                        // whatsapp business status
                        val whatsAppStatusFragment = StatusFragment()
                        val bundle = Bundle()
                        bundle.putString(
                            Constants.FRAGMENT_TYPE_KEY,
                            Constants.TYPE_WHATSAPP_BUSINESS
                        )
                        replaceFragment(whatsAppStatusFragment, bundle)
                        drawerLayout.close()
                    }

                    R.id.menu_settings -> {
                        // settings
                        replaceFragment(SettingsFragment())
                        drawerLayout.close()
                    }
                }

                return@setNavigationItemSelectedListener true
            }

        }
    }

    private val PERMISSION_REQUEST_CODE = 50
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val isPermissionsGranted = SharedPrefUtils.getPrefBoolean(
                SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                false
            )
            if (!isPermissionsGranted) {
                ActivityCompat.requestPermissions(
                    /* activity = */ activity,
                    /* permissions = */ arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    /* requestCode = */ PERMISSION_REQUEST_CODE
                )
                Toast.makeText(activity, "Please Grant Permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isGranted) {
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED, true)
            } else {
                SharedPrefUtils.putPrefBoolean(
                    SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                    false
                )

            }
        }
    }

    private fun splashLogic() {
        binding.apply {
            splashLayout.cardView.slideFromStart()
            Handler(Looper.myLooper()!!).postDelayed({
                splashScreenHolder.slideToEndWithFadeOut()
                splashScreenHolder.visibility = View.GONE
            }, 2000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager?.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}