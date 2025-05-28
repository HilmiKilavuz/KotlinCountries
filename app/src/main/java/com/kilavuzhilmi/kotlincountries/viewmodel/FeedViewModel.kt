/**
 * FeedViewModel.kt
 * 
 * ViewModel for the FeedFragment that manages the list of countries data.
 * 
 * Purpose:
 * - Provides country list data to the UI (FeedFragment)
 * - Handles data operations (API requests and database operations)
 * - Implements caching strategy with time-based refresh
 * - Manages loading, error, and data states
 * - Separates UI logic from data operations
 * 
 * Technologies used:
 * - ViewModel: Android Architecture Component for UI data management
 * - LiveData: Observable data holder that respects Android lifecycle
 * - Room Database: Local data storage and caching
 * - Retrofit: Network requests
 * - RxJava: Reactive programming for network operations
 * - Coroutines: Asynchronous programming for database operations
 * - SharedPreferences: Storing cache timestamps
 */
package com.kilavuzhilmi.kotlincountries.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kilavuzhilmi.kotlincountries.model.Country
import com.kilavuzhilmi.kotlincountries.service.CountryAPIServices
import com.kilavuzhilmi.kotlincountries.service.CountryDatabase
import com.kilavuzhilmi.kotlincountries.util.CustomSharedPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for displaying the list of countries
 * Extends BaseViewModel to inherit application context and coroutine functionality
 * 
 * @param application The application instance used to access system services
 */
class FeedViewModel(application: Application) : BaseViewModel(application) {
    /**
     * API service instance for making network requests
     */
    val countryApiServices = CountryAPIServices()
    
    /**
     * CompositeDisposable to manage RxJava subscriptions
     * Prevents memory leaks by disposing subscriptions when ViewModel is cleared
     */
    private val disposable = CompositeDisposable()
    
    /**
     * Shared preferences wrapper for storing and retrieving cache timestamps
     */
    private var customPreferences = CustomSharedPreferences(getApplication())
    
    /**
     * Cache refresh time interval in nanoseconds
     * 10 minutes = 10 * 60 * 1000 * 1000 * 1000 nanoseconds
     */
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    /**
     * LiveData for countries list
     * Observed by the UI to update the RecyclerView
     */
    val countries = MutableLiveData<List<Country>>()
    
    /**
     * LiveData for error state
     * Observed by the UI to show error message when true
     */
    val countryError = MutableLiveData<Boolean>()
    
    /**
     * LiveData for loading state
     * Observed by the UI to show/hide loading indicator
     */
    val countryLoading = MutableLiveData<Boolean>()

    /**
     * Refreshes country data based on caching strategy
     * Checks if cached data is still valid, otherwise fetches from API
     * 
     * @param forceRefresh If true, bypasses cache and fetches from API
     */
    fun refreshData(forceRefresh: Boolean = false) {
        val updateTime = customPreferences.getTime()
        if (!forceRefresh && updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            // Use cached data if it's still valid and force refresh is not requested
            getDataFromSqllite()
        } else {
            // Fetch fresh data from API if cache is expired or force refresh is requested
            getDataFromApi()
        }
    }
    
    /**
     * Resets all data in the database and preferences
     * Clears the cache and triggers a fresh API request
     * Used to recover from errors or when user wants to refresh all data
     */
    fun resetData() {
        launch(Dispatchers.IO) {
            try {
                // Delete all countries from database
                val dao = CountryDatabase(getApplication()).countryDao()
                dao.deleteAllCountries()
                // Clear cache timestamp
                customPreferences.resetPreferences()
                
                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    countryError.value = false
                    countryLoading.value = false
                    countries.value = listOf()
                    // Force refresh data from API
                    refreshData(true)
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "Reset error: ${e.message}")
            }
        }
    }
    
    /**
     * Retrieves country data from local SQLite database
     * Uses coroutines to perform database operations off the main thread
     */
    fun getDataFromSqllite(){
        launch(Dispatchers.IO) {
            try {
                // Get all countries from database
                val countryList = CountryDatabase(getApplication()).countryDao().getAllCountries()
                
                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    showCountries(countryList)
                    Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "SQLite error: ${e.message}")
                
                // Handle error on main thread
                withContext(Dispatchers.Main) {
                    countryError.value = true
                    countryLoading.value = false
                }
            }
        }
    }

    /**
     * Fetches country data from the API
     * Uses RxJava for reactive handling of network requests
     */
    fun getDataFromApi() {
        // Show loading indicator
        countryLoading.value = true
        
        // Make API request using RxJava
        disposable.add(
            countryApiServices.getData()
                // Run on background thread
                .subscribeOn(Schedulers.newThread())
                // Observe results on main thread
                .observeOn(AndroidSchedulers.mainThread())
                // Handle response
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    // Handle successful response
                    override fun onSuccess(t: List<Country>) {
                        // Store retrieved data in database
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_LONG).show()
                    }

                    // Handle error response
                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    /**
     * Updates UI with countries data
     * Sets LiveData values to update the UI state
     * 
     * @param countryList List of countries to display
     */
    private fun showCountries(countryList : List<Country>){
        countries.value = countryList
        countryLoading.value = false
        countryError.value = false
    }

    /**
     * Stores countries in the local SQLite database
     * Uses coroutines to perform database operations off the main thread
     * 
     * @param list List of countries to store in the database
     */
    private fun storeInSQLite(list: List<Country>){
        launch(Dispatchers.IO) {
            try {
                // Get database access
                val dao = CountryDatabase(getApplication()).countryDao()
                // Clear existing data
                dao.deleteAllCountries()
                // Insert new data and get generated IDs
                val listLong = dao.insertAll(*list.toTypedArray())
                
                // Assign generated IDs to country objects
                var i = 0
                while (i < list.size) {
                    list[i].uuid = listLong[i].toInt()
                    i = i + 1
                }
                
                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    showCountries(list)
                    // Save current time as cache timestamp
                    customPreferences.saveTime(System.nanoTime())
                }
            } catch (e: Exception) {
                Log.e("FeedViewModel", "SQLite store error: ${e.message}")
                
                // Handle error on main thread
                withContext(Dispatchers.Main) {
                    countryError.value = true
                    countryLoading.value = false
                }
            }
        }
    }

    /**
     * Called when ViewModel is destroyed
     * Cleans up RxJava subscriptions to prevent memory leaks
     */
    override fun onCleared() {
        super.onCleared()
        // Dispose all RxJava subscriptions
        disposable.clear()
    }
}