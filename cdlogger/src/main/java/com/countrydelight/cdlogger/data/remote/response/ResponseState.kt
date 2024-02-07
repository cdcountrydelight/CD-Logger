package com.countrydelight.cdlogger.data.remote.response


/**
 * Represents a response state with a status, status code, data, and message.
 *
 * @param status The status of the response.
 * @param statusCode The HTTP status code of the response.
 * @param data The data of the response.
 * @param message The message of the response.
 */
internal data class ResponseState<out T>(
    val status: ResponseStatusEnum,
    val statusCode: Int?,
    val data: T?,
    val message: String?
) {
    companion object {

        /**
         * Creates a success response state.
         *
         * @param data The data of the response.
         * @return A success response state.
         */
        fun <T> success(data: T?): ResponseState<T> {
            return ResponseState(ResponseStatusEnum.Success, null, data, null)
        }

        /**
         * Creates an error response state.
         *
         * @param statusCode The HTTP status code of the response.
         * @param msg The message of the response.
         * @param data The data of the response.
         * @return An error response state.
         */

        fun <T> error(statusCode: Int?, msg: String, data: T? = null): ResponseState<T> {
            return ResponseState(ResponseStatusEnum.Exception, statusCode, data, msg)
        }
    }
}