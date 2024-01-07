package com.example.playlistmaker.sharing.data.impl

import android.content.Context
import androidx.annotation.StringRes
import com.example.playlistmaker.sharing.domain.utils.ResourceProvider

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }
}