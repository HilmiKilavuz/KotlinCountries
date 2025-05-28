/**
 * BaseViewModel.kt
 * 
 * Base class for all ViewModels in the application that provides coroutine support.
 * 
 * Purpose:
 * - Provides a common base for all ViewModels
 * - Implements CoroutineScope to enable coroutines in ViewModels
 * - Manages coroutine lifecycle by canceling jobs when ViewModel is cleared
 * - Provides access to the application context
 * 
 * Technologies used:
 * - AndroidViewModel: ViewModel with application context
 * - Coroutines: Kotlin's lightweight threading framework
 * - CoroutineScope: Defines a scope for launching coroutines
 * - Job: Represents a cancellable unit of work in coroutines
 * - Dispatchers: Determine what thread coroutines run on
 */
package com.kilavuzhilmi.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Abstract base class for all ViewModels in the application
 * Extends AndroidViewModel to have access to application context
 * Implements CoroutineScope to enable coroutine use in ViewModels
 * 
 * @param application The application instance provided to ViewModels
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    /**
     * Job instance to manage coroutines
     * Used to cancel all coroutines when ViewModel is cleared
     */
    private val job = Job()

    /**
     * The coroutine context for this scope
     * Combines the job with the Main dispatcher
     * 
     * Dispatchers.Main: Coroutines using this context will run on the main UI thread
     * This is essential for updating LiveData and UI components
     */
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    /**
     * Called when the ViewModel is being destroyed
     * Cancels all coroutines launched in this scope
     * Prevents memory leaks and unnecessary background work
     */
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
