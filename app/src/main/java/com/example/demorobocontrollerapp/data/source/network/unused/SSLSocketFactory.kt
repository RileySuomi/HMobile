package com.example.demorobocontrollerapp.data.source.network.unused

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object SSLHelper {

    private const val TAG = "SSLHelper"
    /**
     * Creates an SSLSocketFactory that trusts the certificate specified by the raw resource.
     *
     * @param context The context to access resources.
     * @param certResId The raw resource ID of the certificate.
     * @return The configured SSLSocketFactory or null if an error occurs.
     */
    fun getSocketFactory(context: Context, @RawRes certResId: Int): SSLSocketFactory? {
        return try {
            // Load the certificate from the raw resource
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val caInput: InputStream = context.resources.openRawResource(certResId)
            val certificate = caInput.use { certificateFactory.generateCertificate(it) }

            // Create a KeyStore containing our trusted certificate
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null) // Initialize an empty KeyStore
                setCertificateEntry("ca", certificate)
            }

            // Create a TrustManager that trusts the certificate in our KeyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

            // Create an SSLContext that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")  // Or "TLSv1.2" if needed
            sslContext.init(null, trustManagerFactory.trustManagers, null)
            sslContext.socketFactory
        } catch (e: Exception) {
            Log.e(TAG, "Error creating SSLSocketFactory", e)
            null
        }
    }

    /**
     * Retrieves the X509TrustManager based on the provided certificate in the raw resource.
     *
     * @param context The context to access resources.
     * @param certResId The raw resource ID of the certificate.
     * @return The X509TrustManager or null if an error occurs.
     */
    fun getTrustManager(context: Context, @RawRes certResId: Int): X509TrustManager? {
        return try {
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val caInput: InputStream = context.resources.openRawResource(certResId)
            val certificate = caInput.use { certificateFactory.generateCertificate(it) }

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null)
                setCertificateEntry("ca", certificate)
            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }

            // Assume the first TrustManager is an X509TrustManager
            trustManagerFactory.trustManagers[0] as X509TrustManager
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving X509TrustManager", e)
            null
        }
    }
}
