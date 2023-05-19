
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

) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Profile

        if (id != other.id) return false
        if (passwd != other.passwd) return false
        if (!favMoviesList.contentEquals(other.favMoviesList)) return false

        return true
    }
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + passwd.hashCode()
        result = 31 * result + favMoviesList.contentHashCode()
        return result
    }
}
