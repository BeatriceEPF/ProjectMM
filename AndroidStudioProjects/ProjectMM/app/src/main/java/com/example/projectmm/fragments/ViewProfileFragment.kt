package com.example.projectmm.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectmm.R
import com.example.projectmm.model.Profile
import org.json.JSONObject
import java.io.File

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }



    fun createJSONFromProfiles(profiles : ArrayList<Profile>): JSONObject {
        val rootObject = JSONObject();
        var cpt = 0;

        for (profile in profiles)
        {
            rootObject.put("line", cpt++)
            val profileObject = JSONObject()

            profileObject.put("profile_id", profile.id);
            profileObject.put("profile_passwd", profile.passwd);
            profileObject.put("profile_fav", profile.favMoviesList);

            rootObject.put("profile", profileObject)
        }
        return rootObject;
    }

    fun saveInternalData(fileName : String, profilesJson : JSONObject) {
        val profilesString: String = profilesJson.toString()
        val fos = activity?.openFileOutput(fileName, Context.MODE_PRIVATE)

        fos?.write(profilesString.toByteArray());
        fos?.close();
    }

    fun loadInternalData(fileName : String): ArrayList<Profile> {
        val fin = activity?.openFileInput(fileName)

        var c: Int
        val profiles = ArrayList<Profile>()

        val cpt = 0;

        while (fin?.read() != -1) {
            val id = fin?.read()?.toChar().toString()
            val passwd = fin?.read()?.toChar().toString()

            val favMoviesString = fin?.read()?.toChar().toString()
            val favMovies = favMoviesString.substring(1, favMoviesString.length-1).split(",").toTypedArray()

            var profile : Profile = Profile(id, passwd, favMovies)

            profiles.add(profile)
        }
        fin.close()

        return profiles
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