package com.example.playlistmaker.sharing.domain.api

import com.example.playlistmaker.sharing.data.model.EmailData

interface SharingInteractor {
    fun shareApp(link: String, title: String)
    fun sendEmail(emailData: EmailData)
    fun openTerms(link: String)
}
