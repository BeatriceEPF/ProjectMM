package com.example.projectmm

import android.app.Application

class Global : Application() {
    private var profileId = ""

    fun getProfileId(): String {
        return profileId
    }

    fun setProfileId(_profileId: String) {
        this.profileId = _profileId
    }
}