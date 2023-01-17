package studio.badwolfdev.webthings_ktx.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import studio.badwolfdev.webthings_ktx.thing_object.Thing

/**
 * Class representing the Api Service to make api call
 *
 * @param baseUrl Base url of the gateway api
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
class ApiService(
    private val baseUrl: String,
    private val token: String) {

    /**
     * get the list of all things from the gateway
     *
     * @param token Webthings api token
     * @param onResult action to perform once result is received, nullable, receive the response as a param
     */
    init {
        //todo if baseurl didn'T end by / add a /
    }
    fun getThingsList(
        onResult: (Response<List<Thing>>?)-> Unit){
        val httpClient = OkHttpClient.Builder().apply {
            //TODO add logging interceptor
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("User-Agent", "webthings-ktx-library")
                    builder.header("Accept", "application/json")
                    builder.header("Authorization", "Bearer $token")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }

        val retrofit = ApiServiceBuilder.buildService(httpClient, baseUrl, ApiInterface::class.java)
        retrofit.getAllThings().enqueue(
            object : Callback<List<Thing>>{
                override fun onResponse(call: Call<List<Thing>>, response: Response<List<Thing>>) {
                    onResult(response)
                }

                override fun onFailure(call: Call<List<Thing>>, t: Throwable) {
                    Log.e("GETTHINGS", "Api call failed: ${t}")
                    onResult(null)//TODO send the throwable to onResult? for error handling
                }


            }
        )
    }
}