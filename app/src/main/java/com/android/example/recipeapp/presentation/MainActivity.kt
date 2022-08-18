package com.android.example.recipeapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.example.recipeapp.R
import com.android.example.recipeapp.datastore.SettingsDatastore
import com.android.example.recipeapp.presentation.ui.util.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var datastore: SettingsDatastore

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}