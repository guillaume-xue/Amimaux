package ufr.m1.prog_mobile.projet.presentation.ui

import android.app.Application
import android.content.Context
import ufr.m1.prog_mobile.projet.presentation.viewmodel.AddAniViewModel
import ufr.m1.prog_mobile.projet.presentation.viewmodel.InfoViewModel
import ufr.m1.prog_mobile.projet.presentation.viewmodel.MainViewModel

class MyApplication : Application() {
    lateinit var viewModel: MainViewModel
    lateinit var infoViewModel: InfoViewModel
    lateinit var addAniViewModel: AddAniViewModel
    private val PREFS_NAME = "AppPrefs"
    private val IS_FIRST_RUN = "isFirstRun"

    override fun onCreate() {
        super.onCreate()
        viewModel = MainViewModel(this)
        infoViewModel = InfoViewModel(this)
        addAniViewModel = AddAniViewModel(this)
        
        // Vérifier si c'est la première exécution
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean(IS_FIRST_RUN, true)

        if (isFirstRun) {
            // Initialiser la base de données avec les données XML
            viewModel.initializeData()

            // Marquer que ce n'est plus la première exécution
            prefs.edit().putBoolean(IS_FIRST_RUN, false).apply()
        }
    }
}