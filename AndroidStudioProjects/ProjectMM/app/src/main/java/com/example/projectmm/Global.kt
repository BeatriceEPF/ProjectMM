package com.example.projectmm

import android.app.Application
import android.content.Context
import org.json.JSONObject

class Global : Application() {
    private val fileName = "ProfileSettings_ProjectMM.text"

    private var profileId = ""
    private var profile = JSONObject()
    private var profilesJSON = JSONObject()

    fun getProfileId(): String {
        return profileId
    }
    fun setProfileId(_profileId: String) {
        this.profileId = _profileId
    }


    fun getProfile(): JSONObject {
        return profile
    }
    fun setProfile(_profile: JSONObject) {
        this.profile = _profile
    }

    fun getProfilesJSON(): JSONObject {
        return profilesJSON
    }
    fun setProfilesJSON(_profilesJSON: JSONObject) {
        this.profilesJSON = _profilesJSON
        saveInternalData()
    }


    private fun saveInternalData() {
        val fileBody: String = this.profilesJSON.toString()

        openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output?.write(fileBody.toByteArray())
        }
    }
}