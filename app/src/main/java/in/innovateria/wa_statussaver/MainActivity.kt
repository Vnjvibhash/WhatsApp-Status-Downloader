package `in`.innovateria.wa_statussaver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import `in`.innovateria.wa_statussaver.databinding.ActivityMainBinding
import `in`.innovateria.wa_statussaver.Utils.Constants
import `in`.innovateria.wa_statussaver.Utils.SharedPrefKeys
import `in`.innovateria.wa_statussaver.Utils.SharedPrefUtils
import `in`.innovateria.wa_statussaver.Fragments.SettingsFragment
import `in`.innovateria.wa_statussaver.Fragments.StatusFragment
import `in`.innovateria.wa_statussaver.Utils.replaceFragment

class MainActivity : AppCompatActivity() {
    private val activity = this
    private val PERMISSION_REQUEST_CODE = 50

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SharedPrefUtils.init(activity)

        binding.apply {
            requestPermission()
            val whatsAppStatusFragment = StatusFragment()
            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
            replaceFragment(whatsAppStatusFragment, bundle)

            BottomNavMenu.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_status -> {
                        val whatsAppStatusFragment = StatusFragment()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
                        replaceFragment(whatsAppStatusFragment, bundle)
                    }

                    R.id.menu_business_status -> {
                        val whatsAppStatusFragment = StatusFragment()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_BUSINESS)
                        replaceFragment(whatsAppStatusFragment, bundle)
                    }

                    R.id.menu_settings -> {
                        replaceFragment(SettingsFragment())
                    }
                }
                true
            }
        }
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager?.findFragmentById(R.id.frameLayout)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}
