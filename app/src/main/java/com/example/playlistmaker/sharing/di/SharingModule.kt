package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.utils.ResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {
    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }
    single<ResourceProvider> { ResourceProviderImpl(androidContext()) }

    single<SharingInteractor> { SharingInteractorImpl(get()) }
}