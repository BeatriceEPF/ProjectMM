package com.example.projectmm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmm.model.Profile
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class ConnectProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")

    lateinit var modeExtra : String;
    private val fileName = "ProfilesDetails.text"

    lateinit var enterId : String;
    lateinit var enterPasswd : String;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_profile)

        val global = applicationContext as Global

        // GET MODE OF CONNECTION
        val extras = intent.extras
        modeExtra = extras?.get("modeConnect") as String

        val labelError = findViewById<TextView>(R.id.label_error)
        val forgetButton = findViewById<Button>(R.id.button_without_log)
        val okButton = findViewById<Button>(R.id.button_ok)

        forgetButton.setOnClickListener {
            if(modeExtra == "log") {
                okButton.text = "S'inscrire"
                forgetButton.text = "J'ai déjà un compte ?"
                modeExtra = "sign"
            }
            else if(modeExtra == "sign") {
                okButton.text = "Se connecter"
                forgetButton.text = "Je n'ai pas de compte ?"
                modeExtra = "log"
            }
        }
        okButton.setOnClickListener {

            enterId = findViewById<TextView>(R.id.entry_id).text.toString()
            enterPasswd = findViewById<TextView>(R.id.entry_passwd).text.toString()

            if(modeExtra == "log") {
                if (findAccount()) {
                    labelError.text = ""

                    val intent = Intent(this, ViewProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            else if(modeExtra == "sign") {
                if (isAccount()) {
                    labelError.text = "COMPTE EXISTANT"
                }
                else {
                    labelError.text = ""
                    addAccount()

                    val intent = Intent(this, ViewProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        global.setProfilesJSON(loadInternalData());
        Log.d("JSON_INIT", global.getProfilesJSON().toString())
    }

    private fun findAccount(): Boolean {
        val global = applicationContext as Global
        //testJSONStorage()

        val labelError = findViewById<TextView>(R.id.label_error)
        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt()

        Log.d("Pourquoi", nbProfiles.toString())

        Log.d("enterId", enterId)
        Log.d("enterPasswd", enterPasswd)

        if(nbProfiles == 0) labelError.text = "COMPTE INTROUVABLE"

        for(i in 0 until nbProfiles) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))

            Log.d("Pourquoi", "ici")
            Log.d("profile_id", profile.getString("profile_id"))
            Log.d("profile_passwd", profile.getString("profile_passwd"))

            if ((profile.getString("profile_id") == enterId)
                && (profile.getString("profile_passwd") == enterPasswd)) {

                global.setProfileId(enterId);
                global.setProfile(profile);

                return true;
            }
            else if (profile.getString("profile_id") != enterId) {
                labelError.text = "COMPTE INTROUVABLE"
            }
            else if (profile.getString("profile_id") == enterId &&
                 profile.getString("profile_passwd") != enterPasswd) {
                labelError.text = "MOT DE PASSE INVALIDE"
            }
        }
        return false;
    }

    private fun isAccount(): Boolean {
        val global = applicationContext as Global
        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt()

        for(i in 0 until nbProfiles-1) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))
            if (profile.getString("profile_id") == enterId) {
                return true;
            }
        }

        return false;
    }

    private fun addAccount() {
        val global = applicationContext as Global

        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt() + 1

        val rootObject = JSONObject();
        rootObject.put("nb_profile", nbProfiles.toString());

        for(i in 0 until nbProfiles-1) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))
            rootObject.put("profile$i", profile)
        }


        val profileObject = JSONObject()

        profileObject.put("profile_id", enterId);
        profileObject.put("profile_passwd", enterPasswd);
        profileObject.put("movie", JSONArray(ArrayList<String>()))

        val temp = nbProfiles-1
        rootObject.put("profile$temp", profileObject)

        global.setProfileId(enterId);
        global.setProfile(profileObject);
        global.setProfilesJSON(rootObject)
    }

    private fun loadInternalData(): JSONObject {

        try {
            openFileInput(fileName).use { stream ->
                val text = stream?.bufferedReader().use {
                    it?.readText()
                }
                return JSONObject(text.toString())
            }
        } catch (e: FileNotFoundException) {
            val rootObject = JSONObject();
            rootObject.put("nb_profile", 0);

            return rootObject
        }
    }
}