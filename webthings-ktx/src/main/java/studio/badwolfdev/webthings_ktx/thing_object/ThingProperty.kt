package studio.badwolfdev.webthings_ktx.thing_object

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Data class representing a thing property
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
data class ThingProperty(

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("value")
    var value: Any,//todo change the value type based on the type

    @Expose
    @SerializedName("visible")
    val visible: Boolean,

    @Expose
    @SerializedName("title")
    val title: String,

    @Expose
    @SerializedName("type")
    val type: String,

    @Expose
    @SerializedName("@type")
    val propertyType: String,

    @Expose
    @SerializedName("readOnly")
    val readOnly: Boolean,

    @Expose
    @SerializedName("links")
    val links: List<LinkObject>
)
