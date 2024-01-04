package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.data.model.EmailData
import com.example.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp(link: String, title: String) {
        externalNavigator.shareApp(link, title)
    }

    override fun sendEmail(emailData: EmailData) {
        externalNavigator.sendEmail(emailData)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }
}
