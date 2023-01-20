package studio.badwolfdev.webthings_ktx.thing_object

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Data Class representing a single Things from the gateway
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
data class Thing(

    @Expose
    @SerializedName("title")
    val title: String,

    @Expose
    @SerializedName("@context")
    val context: String,

    @Expose
    @SerializedName("@type")
    val type: List<String>,

    @Expose
    @SerializedName("description")
    val description: String,

    @Expose
    @SerializedName("href")
    val href: String,

    @Expose
    @SerializedName("properties")
    val properties: Map<String, ThingProperty>,//TODO can we convert the map into a list?

    @Expose
    @SerializedName("actions")
    val actions: Map<String, ThingAction>,

    @Expose
    @SerializedName("events")
    val events: Map<String, ThingEvent>,

    @Expose
    @SerializedName("links")
    val links: List<LinkObject>,

    /*@Expose
    @SerializedName("layoutIndex")// is this usefull to keep
    val layoutIndex: Int,*/

    /*@Expose
    @SerializedName("selectedCapability")//is this usefull to keep
    val selectedCapabilities: String,*/

    @Expose
    @SerializedName("iconHref")
    val iconLink: String?,

    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("base")
    val baseUrl: String,

    /*@SerializedName("securityDefinitions")
    val securityDefinitions: *///is this usefull to keep here, if yes create a security definitons object
)