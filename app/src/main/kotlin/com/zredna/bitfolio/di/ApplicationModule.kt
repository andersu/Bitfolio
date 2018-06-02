package com.zredna.bitfolio.di

import android.arch.persistence.room.Room
import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.BuildConfig.DEBUG
import com.zredna.bitfolio.view.account.AccountViewModel
import com.zredna.bitfolio.AppExecutors
import com.zredna.bitfolio.BtcBalanceCalculator
import com.zredna.bitfolio.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.db.BitfolioDatabase
import com.zredna.bitfolio.repository.BalanceRepository
import com.zredna.bitfolio.view.addexchange.AddExchangeViewModel
import com.zredna.bittrex.apiclient.BittrexApiClient
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val bitfolioModule: Module = applicationContext {
    viewModel { AccountViewModel(get()) }
    viewModel { AddExchangeViewModel() }

    bean { BalanceRepository(get(), get(), get(), get(), get(), get(), get()) }

    bean { AppExecutors.instance }
    bean { BtcBalanceCalculator() }

    bean {
        Room.databaseBuilder(
                androidApplication(),
                BitfolioDatabase::class.java,
                "bitfolio.db"
        ).build()
    }

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