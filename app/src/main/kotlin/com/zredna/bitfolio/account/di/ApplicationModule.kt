package com.zredna.bitfolio.account.di

import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.account.AccountViewModel
import com.zredna.bitfolio.account.BalanceRepository
import com.zredna.bitfolio.account.BtcBalanceCalculator
import com.zredna.bitfolio.account.converter.BinanceBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BinanceMarketSummaryDtoConverter
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bitfolio.account.converter.BittrexMarketSummaryDtoConverter
import com.zredna.bitfolio.account.service.BinanceBalanceService
import com.zredna.bitfolio.account.service.BinanceMarketService
import com.zredna.bitfolio.account.service.BittrexBalanceService
import com.zredna.bitfolio.account.service.BittrexMarketService
import com.zredna.bittrex.apiclient.BittrexApiClient
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

// Koin module
val bitfolioModule: Module = applicationContext {
    viewModel { AccountViewModel(get()) } // get() will resolve Repository instance
    bean {
        BalanceRepository(
                get<BittrexBalanceService>(),
                get<BittrexMarketService>(),
                get<BinanceBalanceService>(),
                get<BinanceMarketService>(),
                get()
        )
    }
    bean { BittrexBalanceService(get(), get()) }
    bean { BittrexMarketService(get(), get()) }
    bean { BinanceBalanceService(get(), get()) }
    bean { BinanceMarketService(get(), get()) }
    bean { BtcBalanceCalculator() }

    bean {
        BittrexApiClient.Builder()
                .setLoggingEnabled()
                .build()
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