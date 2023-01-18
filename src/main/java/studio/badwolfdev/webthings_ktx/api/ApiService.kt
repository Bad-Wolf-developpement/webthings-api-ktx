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
 * @param token Token from webthings gateway
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
class ApiService(
    private val baseUrl: String,
    private val token: String) {

    private val url: String
    get() {
        return if (baseUrl.last() == '/'){
            baseUrl
        }else{
            "$baseUrl/"
        }
    }

    /**
     * get the list of all things from the gateway
     *
     * @param onResult action to perform once result is received, nullable, receive the response as a param
     */
    fun getThingsList(
        onResult: (Response<List<Thing>>?, Throwable?)-> Unit){
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

        val retrofit = ApiServiceBuilder.buildService(httpClient, url, ApiInterface::class.java)
        retrofit.getAllThings().enqueue(
            object : Callback<List<Thing>>{
                override fun onResponse(call: Call<List<Thing>>, response: Response<List<Thing>>) {
                    onResult(response, null)
                }

                override fun onFailure(call: Call<List<Thing>>, t: Throwable) {
                    Log.e("GETTHINGS", "Api call failed: $t")
                    onResult(null, t)
                }


            }
        )
    }
}