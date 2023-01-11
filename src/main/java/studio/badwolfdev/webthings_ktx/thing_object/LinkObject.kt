package studio.badwolfdev.webthings_ktx.thing_object

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Data class representing a thing link
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
data class LinkObject(

    @Expose
    @SerializedName("rel")
    val relation: String,
    //TODO create a list of valid relation

    @Expose
    @SerializedName("href")
    val href: String,

    @Expose
    @SerializedName("mediaType")
    val mediaType: String? = null
)
