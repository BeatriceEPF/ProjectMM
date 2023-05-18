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

class ConnectProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")

    lateinit var modeExtra : String;
    private val fileName = "ProfileSettings_ProjectMM.text"

    lateinit var enterId : String;
    lateinit var enterPasswd : String;

    lateinit var profilesJsonLoaded : JSONObject

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_profile)

        // GET MODE OF CONNECTION
        val extras = intent.extras
        modeExtra = extras?.get("modeConnect") as String

        profilesJsonLoaded = loadInternalData()

        Log.d("LOADED_JSON", profilesJsonLoaded.toString())

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
                    Log.d("CONNEXION_TEST", "connexion succeeded")

                    val global = applicationContext as Global
                    global.setProfileId(enterId);

                    val intent = Intent(this, ViewProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            else if(modeExtra == "sign") {
                if (isAccount()) {
                    labelError.text = "COMPTE EXISTANT"
                }
                else {
                    Log.d("CONNEXION_TEST", "inscription succeeded")

                    addAccount()

                    val global = applicationContext as Global
                    global.setProfileId(enterId);

                    val intent = Intent(this, ViewProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        profilesJsonLoaded = loadInternalData()
    }

    private fun findAccount(): Boolean {
        val labelError = findViewById<TextView>(R.id.label_error)
        val nbProfiles = profilesJsonLoaded.getString("nb_profile").toInt()

        for(i in 0 until nbProfiles) {
            val profile = JSONObject(profilesJsonLoaded.getString("profile$i"))

            if ((profile.getString("profile_id") == enterId)
                && (profile.getString("profile_passwd") == enterPasswd)) {
                return true;
            }

            else if (profile.getString("profile_id") != enterId) {
                labelError.text = "COMPTE INTROUVABLE"
            }
            else if (profile.getString("profile_passwd") == enterPasswd) {
                labelError.text = "MOT DE PASSE INVALIDE"
            }
        }
        return false;
    }


    private fun isAccount(): Boolean {
        val nbProfiles = profilesJsonLoaded.getString("nb_profile").toInt()

        for(i in 0 until nbProfiles-1) {
            val profile = JSONObject(profilesJsonLoaded.getString("profile$i"))
            if (profile.getString("profile_id") == enterId) {
                return true;
            }
        }
        return false;
    }

    private fun addAccount() {
        val nbProfiles = profilesJsonLoaded.getString("nb_profile").toInt()

        val rootObject = JSONObject();
        rootObject.put("nb_profile", nbProfiles.toString());

        for(i in 0 until nbProfiles) {
            val profile = JSONObject(profilesJsonLoaded.getString("profile$i"))
            rootObject.put("profile$i", profile)
        }


        val profileObject = JSONObject()

        profileObject.put("profile_id", enterId);
        profileObject.put("profile_passwd", enterPasswd);
        profileObject.put("movie", ArrayList<String>())

        rootObject.put("profile$nbProfiles", profileObject)

        Log.d("ADD_ACCOUNT_TEST", rootObject.toString())

        saveInternalData(rootObject)
    }


    private fun testJSONStorage() {
        // Create an ArrayList of profiles
        val profiles = ArrayList<Profile>()

        val profile1 = Profile("Florian", "thedeep", arrayOf("4", "9", "7"))
        val profile2 = Profile("Beatrice", "butcher", arrayOf("34", "1", "982"))

        profiles.add(profile1)
        profiles.add(profile2)


        // Convert the ArrayList to a JSON
        val profilesJsonSaved = createJSONFromProfiles(profiles)


        saveInternalData(profilesJsonSaved)

        // Load the JSON from the internal Storage
        val profilesJsonLoaded = loadInternalData()

        // Read from the Loaded JSON, tests
        Log.d("JSON_LOADED_TEST", profilesJsonLoaded.toString())

        val profile1Loaded = profilesJsonLoaded.getString("profile0")
        Log.d("JSON_PARSE_TEST", profile1Loaded)
    }

    private fun createJSONFromProfiles(profiles : ArrayList<Profile>): JSONObject {
        val rootObject = JSONObject();
        rootObject.put("nb_profile", profiles.size);

        for ((cpt, profile) in profiles.withIndex())
        {
            val profileObject = JSONObject()

            profileObject.put("profile_id", profile.id);
            profileObject.put("profile_passwd", profile.passwd);

            profileObject.put("movie", JSONArray(profile.favMoviesList))
            // REPLACE ABOVE BY :
            // for (favMovie in profile.favMoviesList) profileObject.put("movie", favMovie.id)

            rootObject.put("profile$cpt", profileObject)
        }
        return rootObject;
    }

    private fun saveInternalData(profilesJson : JSONObject) {
        val fileBody: String = profilesJson.toString()

        openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output?.write(fileBody.toByteArray())
        }
    }


    private fun loadInternalData(): JSONObject {
        openFileInput(fileName).use { stream ->
            val text = stream?.bufferedReader().use {
                it?.readText()
            }
            return JSONObject(text.toString())
        }
    }

}