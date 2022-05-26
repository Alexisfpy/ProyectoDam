package com.example.aplicacionbiometrica

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BiometricAuthCallBack {
    companion object{
        private const val USER_DATA = "user_data"
        private const val NAME = "Name"
        private const val EMAIL= "Email"
        private const val PHONE= "Phone"
        private const val TITLE = "Aplicación Biométrica Alexis"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(TITLE)
        checkBiometricCapability()
        showBiometricPrompt()
        fillUserData()
        bt_save_data.setOnClickListener { saveUserData() }
    }
    override fun onSuccess() {
        Toast.makeText(this, "Huella EXITOSA", Toast.LENGTH_LONG).show()
        layout_parent.visibility = View.VISIBLE
    }

    override fun onError() {
        finish()
    }

    override fun onNotRecognized() {
        Log.d("MainActivity", "Huella no encontrada")
    }

    private fun checkBiometricCapability(){

        if (!BiometricUtils.isDeviceReady(this)){
            finish()
        }else{
            Toast.makeText(this, "Biometria disponible", Toast.LENGTH_LONG).show()
        }
    }

    private fun showBiometricPrompt(){
        BiometricUtils.showPrompt(
            activity = this,
            callback = this
        )
    }
    private fun fillUserData(){
        val sharePreferences:SharedPreferences
                = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        et_name.setText(sharePreferences.getString(NAME,""))
        et_email.setText(sharePreferences.getString(EMAIL,""))
        et_phone.setText(sharePreferences.getString(PHONE,""))
    }

    private fun saveUserData(){
        val sharePreferences:SharedPreferences
                = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()
        editor.putString(NAME, et_name.text.toString())
        editor.putString(EMAIL, et_email.text.toString())
        editor.putString(PHONE, et_phone.text.toString())
        editor.apply()
    }

}