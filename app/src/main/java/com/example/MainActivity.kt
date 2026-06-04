package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.screens.SulukMainApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SulukViewModel
import com.example.ui.viewmodel.SulukViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: SulukViewModel by viewModels {
        SulukViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // SulukMainApp handles its own internal scaffold structure
                    // referencing window insets seamlessly
                    SulukMainApp(viewModel = viewModel)
                }
            }
        }
    }
}
