package studio.badwolfdev.webthings_ktx.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import studio.badwolfdev.webthings_ktx.BuildConfig

/**
 * Builder object for the Api Service
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
object ApiServiceBuilder {

    /**
     * Http logger interceptor
     */
    private val logging = HttpLoggingInterceptor()

    /**
     * Gson object
     *
     * allow excluding field without @expose annotation
     */
    private val getGson: Gson
        get() {
            return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        }

    /**
     * Retrofit builder who have a gson builder added to it
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(getGson))

    /**
     * Build the retrofit service
     *
     * @param httpClient OkHttpClient.Builder or null
     * @param baseUrl Base url of the api server
     * @param service Interface [Class] to communicate with the api
     *
     * @return retrofit service
     */
    fun<T> buildService(httpClient: OkHttpClient.Builder?, baseUrl: String, service: Class<T>): T{
        //if BuildConfig.BUILD_TYPE
        Log.d("DEBUG", "baseURL: $baseUrl")
        Log.d("DEBUG", "Httpclient: $httpClient")
        logging.level = HttpLoggingInterceptor.Level.NONE//TODO set this to BODY in debug and NONE in prod
        val client = if (httpClient != null){
            if (baseUrl != "https://gateway.local/") {
                //TODO create a trusted ssl url list that must be populated
                Log.d("DEBUG", "http client NOT null")
                httpClient
                    .addInterceptor(logging)
                    .build()
                }else{
                    UnsafeOkHttpClient
                        .getUnsafeClient(httpClient)
                        .addInterceptor(logging)
                        .build()
                }
            }else {
            Log.d("DEBUG", "http client null")
                if (baseUrl != "https://gateway.local/") {
                    Log.d("DEBUG", "url NOT gateway.local")
                    OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()
                }else{
                    //get unsafe http client
                    Log.d("DEBUG", "url gateway.local")
                    UnsafeOkHttpClient.getUnsafeClient(OkHttpClient.Builder())
                        .addInterceptor(logging)
                        .build()
                }
            }

        return retrofit
            .baseUrl(baseUrl)
            .client(client)
            .build().create(service)
    }
}