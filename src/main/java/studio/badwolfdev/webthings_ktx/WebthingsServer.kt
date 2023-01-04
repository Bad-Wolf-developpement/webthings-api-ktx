package studio.badwolfdev.webthings_ktx

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import okhttp3.HttpUrl
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import studio.badwolfdev.webthings_ktx.Const.RETURN_OK
import studio.badwolfdev.webthings_ktx.Const.WRONG_SERVER_ADDRESS
import java.net.InetAddress
import java.net.URI
import java.net.URL

private const val TAG = "WebthingsServer"
/**
 * Class representing a Webthings Server Object
 *
 * @property isGatewayReachable return true if the gateway is reachable
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
abstract class WebthingsServer(
    private val ctx: Context,
    private val gatewayUrl: String,
    private val gatewayToken: String,
    private val gatewayPort: Int = 443) {

    val isGatewayReachable: Boolean
        get() {
            //TODO can we make dynamic timeout delay?
            Log.d(TAG, "Looking if gateway: ${gatewayUrl} is reachable")
            val status = runBlocking(Dispatchers.IO) {
                val uri = URL(gatewayUrl)
                Log.d(TAG, "testing access to domain: ${uri.host}")
                InetAddress.getByName(uri.host).isReachable(1000)
            }
            return status
        }


    /**
     * Method to test if the url is valid
     *
     * @return [WRONG_SERVER_ADDRESS] if url is invalid or [RETURN_OK]
     */
    private fun validateUrl(url: String): Int{
        return if (!Patterns.WEB_URL.matcher(url).matches())
            WRONG_SERVER_ADDRESS
        else if(HttpUrl.parse(url) == null){
            WRONG_SERVER_ADDRESS
        } else{
            RETURN_OK
        }
    }
}
