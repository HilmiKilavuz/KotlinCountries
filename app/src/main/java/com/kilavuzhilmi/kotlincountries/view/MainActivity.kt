/**
 * MainActivity.kt
 * 
 * Main activity that hosts all fragments in the application.
 * 
 * Purpose:
 * - Acts as the container for all fragments
 * - Configures the system UI (edge-to-edge display)
 * - Serves as the entry point for the application
 * 
 * Technologies used:
 * - AppCompatActivity: Base activity with backward compatibility
 * - Navigation Component: Manages fragment transactions (used implicitly)
 * - Edge-to-edge display: Modern Android UI paradigm that extends content to screen edges
 * - ViewCompat: Utility for backward-compatible view operations
 * - WindowInsets: Controls system UI elements like status and navigation bars
 */
package com.kilavuzhilmi.kotlincountries.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kilavuzhilmi.kotlincountries.R

/**
 * Main activity that serves as the container for all fragments
 * Uses Navigation Component to handle fragment transactions
 */
class MainActivity : AppCompatActivity() {
    /**
     * Called when the activity is first created
     * Initializes the activity, sets up the layout, and configures edge-to-edge display
     * 
     * @param savedInstanceState Contains data about the previous state if activity is being recreated
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display (content extends behind system bars)
        enableEdgeToEdge()
        
        // Set the content view to the activity_main layout
        setContentView(R.layout.activity_main)
        
        // Configure insets to handle system UI elements properly
        // This ensures proper padding around content when using edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}