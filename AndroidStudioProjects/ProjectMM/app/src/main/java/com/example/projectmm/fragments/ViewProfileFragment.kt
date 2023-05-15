package com.example.projectmm.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectmm.R
import com.example.projectmm.model.Profile
import org.json.JSONArray
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        testJSONStorage()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }

    private fun testJSONStorage() {
        // Create an ArrayList of profiles
        val profiles = ArrayList<Profile>()

        val profile1 = Profile("arthurDu78", "Homeland", arrayOf("15", "173", "21"))
        val profile2 = Profile("florianDu92", "theDeep", arrayOf("4", "9", "7"))
        val profile3 = Profile("beaDu91", "Butcher", arrayOf("34", "1", "982"))

        profiles.add(profile1)
        profiles.add(profile2)
        profiles.add(profile3)


        // Convert the ArrayList to a JSON
        val profilesJsonSaved = createJSONFromProfiles(profiles)

        // Save the JSON to the internal Storage
        val fileName = "ProfileSettings_ProjectMM.text"
        saveInternalData(fileName, profilesJsonSaved)

        // Load the JSON from the internal Storage
        val profilesJsonLoaded = loadInternalData(fileName)

        // Read from the Loaded JSON, tests
        Log.d("JSON_LOADED_TEST", profilesJsonLoaded.toString())

        val profile1Loaded = profilesJsonLoaded.getString("profile0")
        Log.d("JSON_PARSE_TEST", profile1Loaded)
    }

    private fun createJSONFromProfiles(profiles : ArrayList<Profile>): JSONObject {
        val rootObject = JSONObject();

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

    private fun saveInternalData(fileName : String, profilesJson : JSONObject) {
        val fileBody: String = profilesJson.toString()

        activity?.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output?.write(fileBody.toByteArray())
        }
    }

    private fun loadInternalData(fileName : String): JSONObject {
        activity?.openFileInput(fileName).use { stream ->
            val text = stream?.bufferedReader().use {
                it?.readText()
            }
            return JSONObject(text.toString())
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}