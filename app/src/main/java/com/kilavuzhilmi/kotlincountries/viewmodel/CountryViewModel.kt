/**
 * CountryViewModel.kt
 * 
 * ViewModel for the CountryFragment that manages country detail data.
 * 
 * Purpose:
 * - Provides country detail data to the UI (CountryFragment)
 * - Handles database operations to retrieve country data by ID
 * - Maintains UI state during configuration changes
 * - Separates UI logic from data operations
 * 
 * Technologies used:
 * - ViewModel: Android Architecture Component for UI data management
 * - LiveData: Observable data holder that respects Android lifecycle
 * - Room Database: Local data storage
 * - Coroutines: Manages background database operations
 */
package com.kilavuzhilmi.kotlincountries.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kilavuzhilmi.kotlincountries.model.Country
import com.kilavuzhilmi.kotlincountries.service.CountryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for displaying a single country's details
 * Extends BaseViewModel to inherit application context and coroutine functionality
 * 
 * @param application The application instance used to access the Room database
 */
class CountryViewModel(application: Application) : BaseViewModel(application = application) {
    /**
     * LiveData for country details
     * Observed by the UI to update when data changes
     * MutableLiveData allows changing the value from within the ViewModel
     */
    val countryLiveData = MutableLiveData<Country>()
    
    /**
     * Retrieves country data from the Room database by its UUID
     * Uses coroutines to perform database operations off the main thread
     * Updates the LiveData with the retrieved country
     * 
     * @param uuid The unique identifier for the country to retrieve
     */
    fun getDataFromRoom(uuid : Int) {
        // Launch coroutine in IO dispatcher for database operations
        launch(Dispatchers.IO) {
            try {
                // Access the Room database using the application context
                val dao = CountryDatabase(getApplication()).countryDao()
                // Get the country by ID from the database
                val country = dao.getCountryById(uuid)
                
                // Switch to Main dispatcher to update LiveData
                withContext(Dispatchers.Main) {
                    // Update the LiveData with the retrieved country
                    countryLiveData.value = country
                }
            } catch (e: Exception) {
                Log.e("CountryViewModel", "Error loading country: ${e.message}")
                withContext(Dispatchers.Main) {
                    // Handle error case if needed
                }
            }
        }
    }
}