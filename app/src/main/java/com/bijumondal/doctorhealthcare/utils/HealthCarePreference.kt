package com.bijumondal.doctorhealthcare.utils

import android.content.Context
import android.content.SharedPreferences


class HealthCarePreference(val context: Context) {

    companion object {
        private const val PREFS_NAME = "com.doctorhealthcare.sessionPref"
        private const val KEY_FIRST_TIME_APP_LOAD = "FIRST_TIME_APP_LOAD"
        private const val KEY_LOGGED_IN = "LOGGED_IN"
        private const val KEY_REGISTERED = "REGISTERED"
        private const val KEY_EMAIL_AND_NUMBER_AVAILABLE = "EMAIL_AND_NUMBER_AVAILABLE"
        private const val KEY_PROFILE_PIC_URL = "PROFILE_PIC_URL"
        private const val KEY_PROFILE_IMAGE = "PROFILE_IMAGE"
        private const val KEY_FIRST_NAME = "FIRST_NAME"
        private const val KEY_LAST_NAME = "LAST_NAME"
        private const val KEY_NAME = "NAME"
        private const val KEY_AUTH = "sessionId"
        private const val KEY_EMAIL = "EMAIL"
        private const val KEY_CODE = "CODE"
        private const val KEY_NUMBER = "NUMBER"
        private const val KEY_GENDER = "GENDER"
        private const val KEY_BLOOD_GROUP = "BLOOD GROUP"
        private const val KEY_DOCTOR_DEPT = "DOCTOR DEPT"
        private const val KEY_DOB = "DATE OF BIRTH"
        private const val KEY_ADDRESS = "ADDRESS"
        private const val KEY_HOSPITAL_NAME = "HOSPITAL_NAME"
        private const val KEY_HOSPITAL_ID = "HOSPITAL_ID"
        private const val KEY_VISIT_AMOUNT = "VISIT_AMOUNT"
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

    fun setProfileImage(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_PROFILE_IMAGE, text)
        editor.apply()
    }

    fun getProfileImage(): String? {
        return sharedPref.getString(KEY_PROFILE_IMAGE, null)
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

    fun setName(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun getName(): String? {
        return sharedPref.getString(KEY_NAME, null)
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

    fun setHospitalId(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_HOSPITAL_ID, value)
        editor.apply()
    }

    fun getHospitalId(): String? {
        return sharedPref.getString(KEY_HOSPITAL_ID, "")
    }

    fun setNumber(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NUMBER, value)
        editor.apply()
    }

    fun getNumber(): String? {
        return sharedPref.getString(KEY_NUMBER, "")

    }

    fun setAddress(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_ADDRESS, value)
        editor.apply()
    }

    fun getAddress(): String? {
        return sharedPref.getString(KEY_ADDRESS, "")

    }

    fun setGender(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_GENDER, text)
        editor.apply()
    }

    fun getGender(): String? {
        return sharedPref.getString(KEY_GENDER, null)
    }

    fun setDOB(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_DOB, text)
        editor.apply()
    }

    fun getDOB(): String? {
        return sharedPref.getString(KEY_DOB, null)
    }

    fun setBloodGroup(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_BLOOD_GROUP, text)
        editor.apply()
    }

    fun getBloodGroup(): String? {
        return sharedPref.getString(KEY_BLOOD_GROUP, null)
    }

    fun setDoctorDept(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_DOCTOR_DEPT, text)
        editor.apply()
    }

    fun getDoctorDept(): String? {
        return sharedPref.getString(KEY_DOCTOR_DEPT, null)
    }


    fun setAuth(value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_AUTH, value)
        editor.apply()
    }

    fun getAuth(): String? {
        return sharedPref.getString(KEY_AUTH, "")

    }

    fun setHospitalName(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_HOSPITAL_NAME, text)
        editor.apply()
    }

    fun getHospitalName(): String? {
        return sharedPref.getString(KEY_HOSPITAL_NAME, null)
    }

    fun setVisitAmount(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_VISIT_AMOUNT, text)
        editor.apply()
    }

    fun getVisitAmount(): String? {
        return sharedPref.getString(KEY_VISIT_AMOUNT, null)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

}