package com.bijumondal.doctorhealthcare

object Constants {
    const val API_DATE_FORMAT = "yyyy-MM-dd"
    const val API_TIME_FORMAT = "HH:mm:ss"
    const val API_CONTENT_TYPE = "application/json"

    const val API_BASE_URL = "http://helping-world.in/medical/"

    //?XDEBUG_SESSION_START=netbeans-xdebug

    /*
    * PATIENT
    * */

    const val PATIENT_REGISTRATION_URL = "patientregistration"
    const val PATIENT_LOGIN_URL = "patientlogin"
    const val PATIENT_CREATE_PROFILE_URL = "createpatientprofile"
    const val PATIENT_PHOTO_URL = "patientphoto"


    /*
    * DOCTOR
    * */

    const val DOCTOR_REGISTRATION_URL = "doctorregistration"
    const val DOCTOR_LOGIN_URL = "doctorlogin"
    const val DOCTOR_CREATE_PROFILE_URL = "createdoctorprofile"
    const val DOCTOR_DEPARTMENT_URL = "getdepartment"
    const val DOCTOR_PHOTO_URL = "doctorphoto"


}