package studio.badwolfdev.webthings_ktx.api

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * allow to create an UnsafeClient(accept all ssl certificate even if invalid
 *
 * @author Arist0v
 * @author Bad Wolf Developpement
 */
object UnsafeOkHttpClient {

    fun getUnsafeClient(httpClient: OkHttpClient.Builder): OkHttpClient.Builder{
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            //val builder = OkHttpClient.Builder()
            httpClient.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            httpClient.hostnameVerifier { _, _ -> true }
            //builder.build()
            return httpClient
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}