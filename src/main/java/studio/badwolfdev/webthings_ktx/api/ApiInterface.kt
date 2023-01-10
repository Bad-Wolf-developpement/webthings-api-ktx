package studio.badwolfdev.webthings_ktx.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import studio.badwolfdev.webthings_ktx.Thing

/**
 * interface for Retrofit Api
 */
interface ApiInterface {

    @GET("/things")
    fun getAllThings(): Call<List<Thing>>
}
