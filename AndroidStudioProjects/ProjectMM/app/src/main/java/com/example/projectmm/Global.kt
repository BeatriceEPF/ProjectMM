package com.example.projectmm

import android.app.Application

class Global : Application() {
    private var profileId = false

    fun getProfileId(): Boolean? {
        return profileId
    }

    fun setProfileId(_profileId: Boolean) {
        this.profileId = _profileId
    }
}