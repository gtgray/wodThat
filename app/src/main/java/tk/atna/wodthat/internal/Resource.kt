package tk.atna.wodthat.internal

open class Resource<T>(
    val resultCode: ResultCode,
    val data: T? = null,
    val message: String? = null
) {

    class Typed<T>(
        val type: ResourceType,
        resultCode: ResultCode,
        data: T? = null,
        message: String? = null
    ) : Resource<T>(resultCode, data, message)

}

