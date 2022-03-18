package com.example.animething.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.animething.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}