package ru.gozerov.core.coroutines

import kotlin.coroutines.cancellation.CancellationException

/**
 *
 * Use that to right catch exception in coroutines
 * You need throw CancellationException to the top of hierarchy to provide principles of structured concurrency
 *
 * */

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}