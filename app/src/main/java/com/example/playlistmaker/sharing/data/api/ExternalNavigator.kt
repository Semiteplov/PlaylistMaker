package com.example.playlistmaker.sharing.data.api

import com.example.playlistmaker.sharing.data.model.EmailData

interface ExternalNavigator {
    fun shareApp(link: String, title: String)
    fun openLink(link: String)
    fun sendEmail(emailData: EmailData)
}