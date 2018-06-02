package com.zredna.bitfolio

import android.app.Application
import com.zredna.bitfolio.di.bitfolioModule
import org.koin.android.ext.android.startKoin

class BitfolioApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(bitfolioModule))
    }
}