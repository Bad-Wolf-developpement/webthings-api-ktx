package studio.badwolfdev.webthings_ktx

import android.content.Context
import android.util.Log
import okhttp3.HttpUrl
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import studio.badwolfdev.webthings_ktx.Const.RETURN_OK
import studio.badwolfdev.webthings_ktx.Const.WRONG_SERVER_ADDRESS
import studio.badwolfdev.webthings_ktx.api.ApiService
import studio.badwolfdev.webthings_ktx.thing_object.Thing
import java.net.InetAddress
import java.net.URI

private const val TAG = "WebthingsServer"
/**
 * Interface representing a Webthings Server Object
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
interface WebthingsServer {
    //TODO add logic for fallbackServer
    //TODO create an activity to get the token from a web browser(copy/paste at first and other way later)

    /**
     * Application context to access the ressources
     */
    val ctx: Context

    /**
     * domain of the gateway
     */
    val gatewayDomain: String

    /**
     * Token generated from the webthings UI
     */
    val gatewayToken: String

    /**
     * Is the gateway reachable with HTTPS
     * Default: true
     */
    val ssl: Boolean
        get() = true

    /**
     * Fallback domain for the gateway(ie: if no internet access you could fallback to local ip or domain)
     * Default: gateway.local
     */
    val fallbackDomain: String
        get() = "gateway.local"

    /**
     * Port to communicate with the gateway
     * Default: 443
     */
    val gatewayPort: Int
        get() = 443

    /**
     * gateway URI build from provided information
     */
    val gatewayUri: URI
        get() {
           return if (ssl){
                if (gatewayPort == 443){
                    URI("https://$gatewayDomain")
                }else{
                    URI("https://$gatewayDomain:$gatewayPort")
                }
            }else{
                if (gatewayPort == 80){
                    URI("http://$gatewayDomain")
                }else{
                    URI("http://$gatewayDomain:$gatewayPort")
                }
            }
        }

    /**
     * fallback URI build from provided information
     */
    val fallbackUri: URI
        get() {
            return if (ssl){
                if (gatewayPort == 443){
                    URI("https://$fallbackDomain")
                }else{
                    URI("https://$fallbackDomain:$gatewayPort")
                }
            }else{
                if (gatewayPort == 80){
                    URI("http://$fallbackDomain")
                }else{
                    URI("http://$fallbackDomain:$gatewayPort")
                }
            }
        }

    /**
     * which uri to use
     *
     * test if main uri is accessible if not test fallback uri
     *
     * @return URI or null
     */
    val uriToUse: URI?
        get() {
            //TODO webthings proxy didn'T forward ping find a workaround
            return if (isGatewayReachable(gatewayUri)){
                gatewayUri
            }else if (isGatewayReachable(fallbackUri)){
                fallbackUri
            }else{
                null
                //TODO show error message
            }
        }

    //var things: List<Thing>?

    val webthingsApi: ApiService
        get() = ApiService(
            uriToUse.toString(),//TODO should i move this to the getThings or similar for fallback check
            gatewayToken
        )

    /**
     * Method who handle the response of the api call
     *
     * All Api call will call this once they get the response
     * so you need to override this to handle the response
     *
     * @param code Http code of the transaction
     * @param response [Response] received from retrofit
     */
    fun handleThingsResponse(code: Int?, response: List<Thing>?)

    /**
     * Method who handle the response of the api call
     *
     * All Api call will call this once they get the response
     * so you need to override this to handle the response
     *
     * @param code Http code of the transaction
     * @param response [Response] received from retrofit
     * @param throwable [Throwable] error received
     */
    fun handleRetrofitError(throwable: Throwable)

    /**
     * Method to get the list of things from the gateway
     *
     * The function will populate a list of [Thing] object
     */
    fun getThings() {
        //TODO choose the uri to use before calling the action
        //TODO url shouldn't have protocol in front
        webthingsApi.getThingsList { response, throwable ->
            if (response == null){
                Log.d("TAG", "API CALL FAILED IN getThings")
                handleRetrofitError(throwable!!)
            }else{
                when (response.code()){
                    200 -> {
                        Log.d("TAG", "API CALL SUCCESS, 200")
                        Log.d("TAG", "response: ${response.body()}")
                        handleThingsResponse(response.code(), response.body())
                    }
                    else -> {
                        Log.d("TAG", "UNHANDLED ERROR: ${response}")
                        handleThingsResponse(response.code(), response.body())

                    }
                }
            }
        }

    }

    /**
     * Method to test if the url is valid
     *
     * @return [WRONG_SERVER_ADDRESS] if url is invalid or [RETURN_OK]
     */
    private fun validateUrl(url: String): Int {
       //TODO instead of return throws exception
        return if (!Patterns.WEB_URL.matcher(url).matches())
            WRONG_SERVER_ADDRESS
        else if (HttpUrl.parse(url) == null) {
            WRONG_SERVER_ADDRESS
        } else {
            RETURN_OK
        }
    }

    /**
     * method to test if the gateway is reachable
     *
     * @return Boolean true if uri is reachable
     */
    fun isGatewayReachable(
        uri: URI,
        timeout: Int = 3000): Boolean {
        Log.d(TAG, "Looking if gateway is reachable on : $uri")

        val status = runBlocking(Dispatchers.IO) {

            Log.d(TAG, "testing access to domain: ${uri.host}")
            InetAddress.getByName(uri.host).isReachable(timeout)
        }
        Log.d(TAG, "is reachable: $status")
        return status
    }
}




