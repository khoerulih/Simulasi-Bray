package com.dicoding.courseschedule.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.*

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var themeSharedPreferences: SharedPreferences
    private lateinit var notificationSharedPreference: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        themeSharedPreferences =
            requireContext().getSharedPreferences(THEME_PREFERENCE, Context.MODE_PRIVATE)
        notificationSharedPreference =
            requireContext().getSharedPreferences(NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)
        //TODO 10 : Update theme based on value in ListPreference
        val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val value = when (themePreference.value) {
                "auto" -> 0
                "on" -> 1
                "off" -> 2
                else -> 0
            }
            themeSharedPreferences.edit().apply {
                this.putInt(DARK_MDOE, value)
                this.apply()
            }
            updateTheme(value)
            true
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val notificationPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        notificationPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val alarmReceiver = DailyReminder()
            if (newValue as Boolean) {
                alarmReceiver.setDailyReminder(requireContext())
                notificationSharedPreference.edit().apply{
                    this.putBoolean(NOTIFICATION_STATUS, newValue)
                    this.apply()
                }
            } else {
                alarmReceiver.cancelAlarm(requireContext())
                notificationSharedPreference.edit().apply{
                    this.clear()
                    this.apply()
                }
            }
            true
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}