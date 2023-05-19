package com.example.projectmm

import android.app.Application
import org.json.JSONObject

class Global : Application() {
    private var profileId = ""
    private var profilesJSON = JSONObject()

    fun getProfileId(): String {
        return profileId
    }
    fun setProfileId(_profileId: String) {
        this.profileId = _profileId
    }


    fun getProfilesJSON(): JSONObject {
        return profilesJSON
    }
    fun setProfilesJSON(_profilesJSON: JSONObject) {
        this.profilesJSON = _profilesJSON
    }

}