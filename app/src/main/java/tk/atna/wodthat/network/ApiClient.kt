package tk.atna.wodthat.network

import android.content.Context
import android.net.Uri
import android.util.MalformedJsonException
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import tk.atna.wodthat.R
import tk.atna.wodthat.stuff.CompressHelper
import tk.atna.wodthat.stuff.Log
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {

    private lateinit var baseUrl: HttpUrl
    private lateinit var okHttpBuilder: OkHttpClient.Builder
    private lateinit var cookieHandler: PersistentCookieHandler
    private lateinit var adapterBuilder: Retrofit.Builder
    private lateinit var retrofit: Retrofit

    companion object {
        @Volatile
        private var INSTANCE: ApiClient? = null

        private const val SERVER_SCHEME = R.string.server_scheme
        private const val SERVER_HOST = R.string.server_host


        fun getInstance(context: Context): ApiClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: init(context)
                    .also { apiClient -> INSTANCE = apiClient }
            }
        }

        private fun init(context: Context) =
            ApiClient(context)
                .createAdapter(context)

    }

    private constructor(context: Context) : this() {
        baseUrl = HttpUrl.Builder()
            .scheme(context.getString(SERVER_SCHEME))
            .host(context.getString(SERVER_HOST))
            .build()
    }

    private fun createAdapter(context: Context): ApiClient {
        cookieHandler = PersistentCookieHandler(context)
        okHttpBuilder = OkHttpClient.Builder()
            .cookieJar(cookieHandler)
//                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
//                .writeTimeout(60000, TimeUnit.MILLISECONDS)
        //
        adapterBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(CompressHelper.moshi()).asLenient())
        //
        return this
    }

    private fun addHeader(name: String, value: String): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val builder = chain.request().newBuilder()
                builder.header(name, value)
                return chain.proceed(builder.build())
            }
        }
    }

    fun enableCache(context: Context): ApiClient {
        val cacheSize: Long = 1024 * 1024 * 2
        val cache = Cache(context.cacheDir, cacheSize)
        okHttpBuilder.cache(cache)
            .addInterceptor(
                addHeader(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 1
                )
            )
        //
        return this
    }

    fun enableLogging(): ApiClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpBuilder.addInterceptor(logging)
        return this
    }

    fun <S> createService(serviceClass: Class<S>): S {
        retrofit = adapterBuilder
            .client(okHttpBuilder.build())
            .build()
        //
        return retrofit.create(serviceClass)
    }

    fun executeRawRequest(uri: Uri): Call {
//        val builder = OkHttpClient.Builder()
//                .readTimeout(180000, TimeUnit.MILLISECONDS)
//                .writeTimeout(180000, TimeUnit.MILLISECONDS)
        val request = Request.Builder()
            .url(uri.toString())
            .get()
            .build()
        //
        return okHttpBuilder.build()
            .newCall(request)
    }

    fun clearCookies() {
        cookieHandler.clearCookies()
    }



}
