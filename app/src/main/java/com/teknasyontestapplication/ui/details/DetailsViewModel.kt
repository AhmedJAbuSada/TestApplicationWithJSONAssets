package com.teknasyontestapplication.ui.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class DetailsViewModel : ViewModel(){
    val titleTextView = MutableStateFlow("")
    val dateTextView = MutableStateFlow("")
    val heightMassTextView = MutableStateFlow("")
    val costTextView = MutableStateFlow("")
    val lastPositionTextView = MutableStateFlow("")
}