package com.zredna.bitfolio.account

import android.app.Application
import com.zredna.bitfolio.account.di.bitfolioModule
import org.koin.android.ext.android.startKoin

class BitfolioApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(bitfolioModule))
    }
}