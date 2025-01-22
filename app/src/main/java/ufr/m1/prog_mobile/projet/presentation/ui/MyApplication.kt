package ufr.m1.prog_mobile.projet.presentation.ui

import android.app.Application
import ufr.m1.prog_mobile.projet.presentation.viewmodel.MyViewModel

class MyApplication : Application(){
    lateinit var viewModel: MyViewModel
        private set

    override fun onCreate() {
        super.onCreate()
        viewModel = MyViewModel(this)
        viewModel.initializeData()
    }
}