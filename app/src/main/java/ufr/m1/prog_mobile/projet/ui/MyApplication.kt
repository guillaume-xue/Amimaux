package ufr.m1.prog_mobile.projet.ui

import android.app.Application

class MyApplication : Application(){
    lateinit var viewModel: MyViewModel
        private set

    override fun onCreate() {
        super.onCreate()
        viewModel = MyViewModel(this)
        viewModel.initializeData()
    }
}