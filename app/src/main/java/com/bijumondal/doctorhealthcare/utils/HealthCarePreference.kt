package com.bijumondal.doctorhealthcare.utils

import android.content.Context
import android.content.SharedPreferences


class HealthCarePreference(val context: Context) {

    companion object {
        private const val PREFS_NAME = "com.doctorhealthcare.sessionPref"
        private const val KEY_FIRST_TIME_APP_LOAD = "FIRST_TIME_APP_LOAD"
        private const val KEY_LOGGED_IN = "LOGGED_IN"
        private const val KEY_EMAIL_AND_NUMBER_AVAILABLE = "EMAIL_AND_NUMBER_AVAILABLE"
        private const val KEY_PROFILE_PIC_URL = "PROFILE_PIC_URL"
        private const val KEY_FIRST_NAME = "FIRST_NAME"
        private const val KEY_LAST_NAME = "LAST_NAME"
        private const val KEY_AUTH = "sessionId"
        private const val KEY_EMAIL = "EMAIL"
        private const val KEY_CODE = "CODE"
        private const val KEY_NUMBER = "NUMBER"
        private const val KEY_AUTH_KEY = "AUTH_KEY"
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_USER_TYPE = "USER_TYPE_ID"


    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setIsFirstTimeAppLoad(status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_FIRST_TIME_APP_LOAD, status)
        editor.apply()
    }

    fun isFirstTimeAppLoad(): Boolean {
        return sharedPref.getBoolean(KEY_FIRST_TIME_APP_LOAD, true)
    }

    fun setAuthKey(key: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_AUTH_KEY, key)
        editor.apply()
    }


    fun getAuthKey(): String? {
        return sharedPref.getString(KEY_AUTH_KEY, null)
    }

    fun setIsEmailAndNumberAvailable(status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_EMAIL_AND_NUMBER_AVAILABLE, status)
        editor.apply()
    }

    fun isEmailAndNumberAvailable(): Boolean {
        return sharedPref.getBoolean(KEY_EMAIL_AND_NUMBER_AVAILABLE, false)
    }

    fun setUserId(id: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_USER_ID, id)
        editor.apply()
    }

    fun getUserId(): Int? {
        return sharedPref.getInt(KEY_USER_ID, 0)
    }

    fun setUserType(id: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_USER_TYPE, id)
        editor.apply()
    }

    fun getUserType(): Int? {
        return sharedPref.getInt(KEY_USER_TYPE, 0)
    }

    fun setIsLoggedIn(status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_LOGGED_IN, status)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setProfilePicUrl(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_PROFILE_PIC_URL, text)
        editor.apply()
    }

    fun getProfilePicUrl(): String? {
        return sharedPref.getString(KEY_PROFILE_PIC_URL, null)
    }


    fun setFirstName(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_FIRST_NAME, text)
        editor.apply()
    }

    fun getFirstName(): String? {
        return sharedPref.getString(KEY_FIRST_NAME, null)
    }

    fun setLastName(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_LAST_NAME, text)
        editor.apply()
    }

    fun getLastName(): String? {
        return sharedPref.getString(KEY_LAST_NAME, null)
    }

    fun setEmail(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_EMAIL, text)
        editor.apply()
    }

    fun getEmail(): String? {
        return sharedPref.getString(KEY_EMAIL, null)

    }

    fun setCode(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_CODE, value)
        editor.apply()
    }

    fun getCode(): String? {
        return sharedPref.getString(KEY_CODE, "")

    }

    fun setNumber(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NUMBER, value)
        editor.apply()
    }

    fun getNumber(): String? {
        return sharedPref.getString(KEY_NUMBER, "")

    }

    fun setAuth(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_AUTH, value)
        editor.apply()
    }

    fun getAuth(): String? {
        return sharedPref.getString(KEY_AUTH, "")

    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.apply()
    }

}