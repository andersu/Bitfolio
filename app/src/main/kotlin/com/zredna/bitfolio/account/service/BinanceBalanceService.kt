package com.zredna.bitfolio.account.service

import com.zredna.binanceapiclient.BinanceApiClient
import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.converter.BinanceBalanceDtoConverter

class BinanceBalanceService(
        private val binanceApiClient: BinanceApiClient,
        private val binanceBalanceDtoConverter: BinanceBalanceDtoConverter
) : BalanceService {

    override fun getBalances(onSuccess: (List<Balance>) -> Unit, onFailure: () -> Unit) {
        binanceApiClient.getAccountInformation(
                onSuccess = {
                    val binanceBalances = binanceBalanceDtoConverter
                            .convertToModels(it.balances)
                            .filter { it.balance > 0 }
                    onSuccess(binanceBalances)
                },
                onFailure = onFailure
        )
    }
}