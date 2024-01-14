package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.data.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareApp(link: String, title: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            link
        )
        val chooser = Intent.createChooser(shareIntent, title)
        /*
            TODO: Question ???
             Needed to add FLAG_ACTIVITY_NEW_TASK to avoid the error
             "Calling startActivity() from outside of an Activity context
             requires the FLAG_ACTIVITY_NEW_TASK flag."
             That's because I pass application context to ExternalNavigator in koin's
             settingModule and sharingModule instead of passing Settings activity context
             Is there a better way to fix this error?
         */
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override fun openLink(link: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }

    override fun sendEmail(emailData: EmailData) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            emailData.subject
        )
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            emailData.text
        )
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(emailIntent)
    }
}
