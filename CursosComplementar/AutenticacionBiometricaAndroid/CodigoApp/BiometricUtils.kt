package com.example.aplicacionbiometrica

import android.content.Context
import android.graphics.fonts.Font
import androidx.appcompat.app.AppCompatActivity

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

interface BiometricAuthCallBack{
    fun onSuccess()
    fun onError()
    fun onNotRecognized()
}

object BiometricUtils {

    fun isDeviceReady(context: Context) =
        getCapability(context) == BIOMETRIC_SUCCESS
    fun showPrompt(
        title: String="Autenticaction Biometric",
        subtitle: String = "Introduce tus credenciales",
        description: String = "Introduce tus huellas para verificar que eres tú",
        cancelButton: String ="Cancelar",
        activity: AppCompatActivity,
        callback: BiometricAuthCallBack
    ){
        val promptInfo=
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setAllowedAuthenticators(BIOMETRIC_WEAK)
                .setNegativeButtonText(cancelButton)
                .build()

        val prompt = initPrompt(activity,callback)
        prompt.authenticate(promptInfo)
    }

    private fun initPrompt(
        activity: AppCompatActivity,
        callback: BiometricAuthCallBack) : BiometricPrompt{
        val executer = ContextCompat.getMainExecutor(activity)
        val autenticationCallback = object :BiometricPrompt.AuthenticationCallback(){


            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                callback.onSuccess()
            }

            override fun onAuthenticationError(
                 errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                callback.onError()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                callback.onNotRecognized()
            }
        }
        return  BiometricPrompt(activity, executer,autenticationCallback)
    }

    private fun getCapability(context: Context): Int =
        BiometricManager.from(context).canAuthenticate(BIOMETRIC_WEAK)

}