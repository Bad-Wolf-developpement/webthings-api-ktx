package studio.badwolfdev.webthings_ktx.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        logging.level = HttpLoggingInterceptor.Level.NONE//TODO set this to BODY in debug and NONE in prod
        val client = if (httpClient != null){
            httpClient
                .addInterceptor(logging)
                .build()
            }else {
                OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()
            }

        return retrofit
            .baseUrl(baseUrl)
            .client(client)
            .build().create(service)
    }
}