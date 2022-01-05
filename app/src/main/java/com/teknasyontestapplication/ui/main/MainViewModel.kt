package com.teknasyontestapplication.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel(){
    val searchStateFlow = MutableStateFlow("")
}