package com.bijumondal.doctorhealthcare

object Constants {
    const val LOCAL_IMAGE_DIRECTORY = "/DoctorHealthCare"
    const val GALLERY = 1
    const val CAMERA = 2
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
    const val PATIENT_PROFILE_DETAILS_URL = "getpatientprofile"
    const val BOOK_APPOINTMENT_URL = "appointmentbook"
    const val PATIENT_APPOINTMENTS_LIST_URL = "patientappointmentlist"


    /*
    * DOCTOR
    * */

    const val DOCTOR_REGISTRATION_URL = "doctorregistration"
    const val DOCTOR_LOGIN_URL = "doctorlogin"
    const val DOCTOR_CREATE_PROFILE_URL = "createdoctorprofile"
    const val DOCTOR_DEPARTMENT_URL = "getdepartment"
    const val DOCTOR_PHOTO_URL = "doctorphoto"
    const val DOCTOR_PROFILE_DETAILS_URL = "getdoctorprofile"
    const val DOCTOR_TIME_SLOT_LIST_URL = "timeslotlist"
    const val DOCTOR_HOLIDAYS_LIST_URL = "getdoctorholidays"
    const val ADD_DOCTOR_HOLIDAYS_URL = "adddoctorholidays"
    const val DELETE_DOCTOR_HOLIDAYS_URL = "deletedoctorholidays"
    const val ADD_DOCTOR_TIMESLOTS_URL = "addtimeslot"
    const val DELETE_DOCTOR_TIMESLOTS_URL = "deletetimeslot"
    const val DOCTOR_APPOINTMENTS_LIST_URL = "getdoctorappointmentbook"


    /*
    * Others
    * */

    const val BANNER_LIST_URL = "bannerlist"
    const val ALL_DOCTORS_LIST_URL = "doctorlist"
    const val HOSPITAL_LIST_URL = "gethospitallist"


}