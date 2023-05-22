
package com.example.projectmm.model

import android.content.Context
import android.os.Parcelable
import com.example.projectmm.Global
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class Profile(
    val id: String,
    val passwd: String,
    val favMoviesList: Array<Int>

) : Parcelable {}
