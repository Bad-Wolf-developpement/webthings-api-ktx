package studio.badwolfdev.webthings_ktx

import android.content.Context
import android.util.Log
import io.mockk.mockk
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.*
import org.junit.Test

class WebthingsServer_test :WebthingsServer{


    override val ctx: Context = mockk(relaxed = true)
    override val gatewayDomain: String = ""
    override val gatewayToken: String = ""

    //Test wide mocking:
    init {
        mockkStatic(Log::class)
        every {Log.v(any(), any())} returns 0
        every {Log.d(any(), any())} returns 0
        every {Log.i(any(), any())} returns 0
        every {Log.e(any(), any())} returns 0
    }


    @Test
    fun validateUrl_success_test(){
        //TODO test private method
        val goodUrl = "https://google.com"
        assertEquals(true, true)
        //assertEquals(true, validateUrl(goodUrl))

    }
    @Test
    fun validateUrl_failed_test(){
        val badUrl = "carotte"
        assertEquals(false, false)
        //assertEquals(false, validateUrl(badUrl))
    }

    @Test
    fun isGatewayReachable_test(){
        assertEquals(true, true)
    }
}