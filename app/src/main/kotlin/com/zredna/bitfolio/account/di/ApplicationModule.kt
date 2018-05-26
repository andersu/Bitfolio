package com.zredna.bitfolio.account.di

import android.arch.persistence.room.Room
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.BuildConfig.DEBUG
import com.zredna.bitfolio.account.AccountViewModel
import com.zredna.bitfolio.account.AppExecutors
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.db.BitfolioDatabase
import com.zredna.bitfolio.account.repository.BalanceRepository
import com.zredna.bitfolio.account.service.BinanceBalanceService
import com.zredna.bitfolio.account.service.BinanceMarketService
import com.zredna.bittrex.apiclient.BittrexApiClient
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

// Koin module
val bitfolioModule: Module = applicationContext {
    viewModel { AccountViewModel(get()) }
    bean { BalanceRepository(get(), get(), get(), get(), get(), get()) }

    bean { AppExecutors.instance }
    bean { BtcBalanceCalculator() }

    // Room Database instance
    bean {
        Room.databaseBuilder(
                androidApplication(),
                BitfolioDatabase::class.java,
                "bitfolio.db"
        ).build()
    }
    // BalanceDAO
    bean { get<BitfolioDatabase>().balanceDao() }

    bean {
        val builder = BittrexApiClient.Builder()
        if (DEBUG) {
            builder.setLoggingEnabled()
        }
        builder.build()
    }
    bean { BittrexBalanceDtoConverter() }
    bean { BittrexMarketSummaryDtoConverter() }

    bean {
        BinanceApiClient.Builder()
                .setLoggingEnabled()
                .build()
    }
    bean { BinanceBalanceDtoConverter() }
    bean { BinanceMarketSummaryDtoConverter() }
}