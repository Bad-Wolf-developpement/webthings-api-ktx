package studio.badwolfdev.webthings_ktx

import android.util.Log
import okhttp3.HttpUrl
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import studio.badwolfdev.webthings_ktx.Const.RETURN_OK
import studio.badwolfdev.webthings_ktx.Const.WRONG_SERVER_ADDRESS
import java.net.InetAddress
import java.net.URI

private const val TAG = "WebthingsServer"
/**
 * Class representing a Webthings Server Object
 *
 * @property isGatewayReachable return true if the gateway is reachable
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
abstract class WebthingsServer(private val gatewayUrl: String, private val gatewayPort: Int = 443) {

    val isGatewayReachable: Boolean
        get() {
            //TODO webthings by proxy didn't answer to ping
            Log.d(TAG, "Looking if gateway: ${gatewayUrl} is reachable")
            val status = runBlocking(Dispatchers.IO) {
                val uri = URI(gatewayUrl)
                Log.d(TAG, "testing access to domain: ${uri.host}")
                InetAddress.getByName(uri.host).isReachable(3000)
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