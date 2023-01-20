package studio.badwolfdev.webthings_ktx

import android.content.Context
import android.util.Log
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Spy
import studio.badwolfdev.webthings_ktx.thing_object.Thing
import java.net.InetAddress
import java.net.URI

class WebthingsServerTest{
    init {
        mockkStatic(Log::class)
        every {Log.v(any(), any())} returns 0
        every {Log.d(any(), any())} returns 0
        every {Log.i(any(), any())} returns 0
        every {Log.e(any(), any())} returns 0
    }

    val mainUrl = URI("https://test.test")
    val fallbackUrl = URI("https://fallback.test")

    class WtServer(
        override val ctx: Context,
        override val gatewayDomain: String,
        override val gatewayToken: String,
        override val fallbackDomain: String
    ) : WebthingsServer {
        override fun handleThingsResponse(code: Int?, response: List<Thing>?) {
            return
        }

        override fun handleRetrofitError(throwable: Throwable) {
            return
        }

    }

    @Test
    fun uriToUse_to_mainUrl(){

        val ctx = Mockito.mock(Context::class.java)
        val wtServer = WtServer(
            ctx,
            "test.test",
            "dummyToken",
            "fallback.test")

        mockkStatic("java.net.InetAddress")
        every { InetAddress.getByName(mainUrl.host).isReachable(3000) } returns true
        every { InetAddress.getByName(fallbackUrl.host).isReachable(3000) } returns true

        val result = wtServer.uriToUse
        assertEquals(mainUrl, result)
    }

    @Test
    fun uriToUse_to_fallback(){

        val ctx = Mockito.mock(Context::class.java)
        val wtServer = WtServer(
            ctx,
            "testnotworking.test",
            "dummyToken",
            "fallback.test")

        mockkStatic("java.net.InetAddress")
        every { InetAddress.getByName(mainUrl.host).isReachable(3000) } returns false
        every { InetAddress.getByName(any<String>()).isReachable(3000) } returns false
        every { InetAddress.getByName(fallbackUrl.host).isReachable(3000) } returns true

        val result = wtServer.uriToUse
        assertEquals(fallbackUrl, result)
    }

    @Test
    fun uriToUse_to_null(){

        val ctx = Mockito.mock(Context::class.java)
        val wtServer = WtServer(
            ctx,
            "testnotworking.test",
            "dummyToken",
            "fallbacknotworking.test")

        mockkStatic("java.net.InetAddress")
        every { InetAddress.getByName(mainUrl.host).isReachable(3000) } returns false
        every { InetAddress.getByName(any<String>()).isReachable(3000) } returns false
        every { InetAddress.getByName(fallbackUrl.host).isReachable(3000) } returns true

        val result = wtServer.uriToUse
        assertNull(result)
    }
}