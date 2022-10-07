package com.example.bluetooth_sample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.bluetooth_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var BTadapter: BluetoothAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BTadapter = BluetoothAdapter.getDefaultAdapter()
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.pairedText.text = "Paired devices: "

        if (BTadapter==null){
            binding.textV.text = "Bluetooth is not available"
        }
        else{
            binding.textV.text = "Bluetooth is available"
        }

        binding.onButton.setOnClickListener(){
            if (BTadapter.isEnabled){
                Toast.makeText(this,"Bluetooth is already ON", Toast.LENGTH_SHORT).show()
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestMultiplePermissions.launch(arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT))
                }
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
        }

        binding.offButton.setOnClickListener {
            if (!BTadapter.isEnabled){
                Toast.makeText(this,"Bluetooth is already OFF", Toast.LENGTH_SHORT).show()
            }
            else{
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    BTadapter.disable()
                    Toast.makeText(this,"Bluetooth is OFF", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.discoverableButton.setOnClickListener {
            if (!BTadapter.isEnabled){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestMultiplePermissions.launch(arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT))
                }
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
            Toast.makeText(this,"Making your device discoverable", Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions.launch(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT))
            }
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            requestBluetooth.launch(enableBtIntent)
        }

        binding.pairedDvButton.setOnClickListener {
            if (BTadapter.isEnabled){
                var text = "1. Paired devices: "
                val devices = BTadapter.bondedDevices
                for (device in devices){
                    text += "\n${device.name}"
                }
                binding.pairedText.text = text
            }
            else{
                Toast.makeText(this,"Turn on Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(binding.root)
    }




    private var requestBluetooth = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //granted
        }else{
            //deny
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }
}