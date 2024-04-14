package com.example.playlistmaker.root

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rootModule = module {
    viewModel<RootViewModel> {
        RootViewModel(navigationInteractor = get())
    }
}