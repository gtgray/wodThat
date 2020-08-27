package tk.atna.wodthat.internal

enum class ResultCode {

    SUCCESS(),
    IN_PROGRESS(),
    REQUEST_CANCELED(),
    INVALID_SESSION(),
    EMPTY_RESPONSE(),
    INVALID_ARGUMENTS(),
    UNKNOWN_ERROR(),
    ;

//    @StringRes int messageRes // todo: add default result message for each code

}
