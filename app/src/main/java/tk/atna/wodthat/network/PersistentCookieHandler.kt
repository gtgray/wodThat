package tk.atna.wodthat.network

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import tk.atna.wodthat.stuff.PersistenceHelper

class PersistentCookieHandler private constructor(
    private val persistenceHelper: PersistenceHelper
) : CookieJar {

    companion object {
        private const val PERSISTED_COOKIES = "persisted_cookies"
    }

    constructor(context: Context) : this(
        PersistenceHelper(context)
    )

    init {
        // todo: make memory cache
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val key = HttpUrl.Builder().scheme(url.scheme).host(url.host).build().toString()
        val cookies = pullCookies(key)

//        Log.w("------------------- URI " + key)
//        Log.w("------------------- COOKIES " + cookies)

//        return new ArrayList<>(discoverValid(cookies)) // todo: need filter?
        return ArrayList(cookies)
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val key = HttpUrl.Builder().scheme(url.scheme).host(url.host).build().toString()

//        Log.w("------------------- KEY " + key)
//        Log.w("------------------- COOKIES " + cookies)

//        saveCookies(key, discoverPersisted(cookies)) // todo: need filter?
        updateCookies(key, cookies)
    }

    private fun discoverPersisted(cookies: Collection<Cookie>): Collection<Cookie> {
        val persistentCookies = arrayListOf<Cookie>()
        cookies.forEach { cookie ->
            if (cookie.persistent) {
                persistentCookies.add(cookie)
            }
        }
        //
        return persistentCookies
    }

    private fun discoverValid(cookies: Collection<Cookie>): Collection<Cookie> {
        val validCookies = arrayListOf<Cookie>()
        cookies.forEach { cookie ->
            if (isValid(cookie))
                validCookies.add(cookie)
        }
        //
        return validCookies
    }

    private fun isValid(cookie: Cookie): Boolean = (cookie.expiresAt > System.currentTimeMillis())

    fun clearCookies() = persistenceHelper.flushParameter(PERSISTED_COOKIES, true)

    private fun updateCookies(key: String, cookies: Collection<Cookie>) {
        val cookieMap = pullAllCookies()
        var current = cookieMap[key]
        if (current == null) {
            current = hashMapOf()
            cookieMap[key] = current
        }
        //
        current.putAll(toMap(cookies))
        saveCookies(cookieMap)
    }

    private fun replaceCookies(key: String, cookies: Collection<Cookie>) {
        val cookieMap = pullAllCookies()
        cookieMap[key] = toMap(cookies)
        saveCookies(cookieMap)
    }

    private fun toMap(cookies: Collection<Cookie>): MutableMap<String, Cookie> {
        val map = mutableMapOf<String, Cookie>()
        if (cookies.isNotEmpty()) {
            cookies.forEach { cookie ->
                map[cookie.name] = cookie
            }
        }
        //
        return map
    }

    private fun pullCookies(key: String): Collection<Cookie> =
        (pullAllCookies()[key] ?: mutableMapOf()).values

    private fun pullAllCookies(): MutableMap<String, MutableMap<String, Cookie>> =
        persistenceHelper.readObject(PERSISTED_COOKIES) ?: hashMapOf()

    private fun saveCookies(cookieMap: Map<String, Map<String, Cookie>>) =
        persistenceHelper.writeObject(PERSISTED_COOKIES, cookieMap, true)

}
