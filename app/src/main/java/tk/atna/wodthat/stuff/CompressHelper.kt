package tk.atna.wodthat.stuff

import com.squareup.moshi.*
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.internal.Util
import tk.atna.wodthat.model.MuscleColor
import java.io.IOException
import java.lang.NullPointerException
import java.lang.reflect.Type
import java.util.*

object CompressHelper {

    val MOSHI: Moshi = createMoshi()


    inline fun <reified T> toJson(source: T): String? {
        try {
            return MOSHI.adapter(T::class.java).toJson(source)
        //
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //
        return null
    }

    inline fun <reified T> fromJson(json: String?): T? {
        json?.let {
            try {
                return MOSHI.adapter(T::class.java).fromJson(json)
            //
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //
        return null
    }

    inline fun <reified T> fromJson(json: String?, type: Type): T? {
        json?.let {
            try {
                return MOSHI.adapter<T?>(type).fromJson(json)
                //
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //
        return null
    }

    fun moshi() = MOSHI

    private fun createMoshi() =
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(Locale::class.java, LocaleJsonAdapter())
//            .add(KotlinJsonAdapterFactory()) // to use enable 'other.moshi_reflect' in build.gradle
            .build()

}

class LocaleJsonAdapter : JsonAdapter<Locale>() {

    private val options: JsonReader.Options =
        JsonReader.Options.of("language", "country", "variant")


    override fun toJson(writer: JsonWriter, value: Locale?) {
        if (value == null) {
            throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
        }
        writer.beginObject()
        writer.name("language")
        writer.value(value.language)
        writer.name("country")
        writer.value(value.country)
        writer.name("variant")
        writer.value(value.variant)
        writer.endObject()
    }

    override fun fromJson(reader: JsonReader): Locale? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull()
        }
        //
        var language: String? = null
        var country: String? = null
        var variant: String? = null
        //
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> language = reader.nextString() ?: throw Util.unexpectedNull("language", "language", reader)
                1 -> country = reader.nextString()
                2 -> variant = reader.nextString()
                -1 -> {
                    // Unknown name, skip it.
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }
        //
        return Locale(
            language ?: throw Util.missingProperty("language", "language", reader),
            country ?: "",
            variant ?: ""
        )
    }

}
