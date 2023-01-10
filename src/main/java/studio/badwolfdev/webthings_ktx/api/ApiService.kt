package studio.badwolfdev.webthings_ktx.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import studio.badwolfdev.webthings_ktx.Thing

/**
 * Class representing the Api Service to make api call
 *
 * @param baseUrl Base url of the gateway api
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
class ApiService(val baseUrl: String) {

    /**
     * get the list of all things from the gateway
     */
    fun getThingsList(token: String){
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
        //TODO maybe use a callback receive as param and that will be overriden from the webthings interface
        retrofit.getAllThings().enqueue(
            object : Callback<List<Thing>>{
                override fun onResponse(call: Call<List<Thing>>, response: Response<List<Thing>>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<List<Thing>>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            }
        )
    }
}