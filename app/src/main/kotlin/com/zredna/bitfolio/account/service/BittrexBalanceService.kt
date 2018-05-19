package com.zredna.bitfolio.account.service

import com.zredna.bitfolio.account.Balance
import com.zredna.bitfolio.account.converter.BittrexBalanceDtoConverter
import com.zredna.bittrex.apiclient.BittrexApiClient

class BittrexBalanceService(
        private val bittrexApiClient: BittrexApiClient,
        private val bittrexBalanceDtoConverter: BittrexBalanceDtoConverter
) : BalanceService {

    override fun getBalances(onSuccess: (List<Balance>) -> Unit, onFailure: () -> Unit) {
        bittrexApiClient.getBalances(
                onSuccess = {
                    val bittrexBalances = bittrexBalanceDtoConverter
                            .convertToModels(it.result)
                            .filter { it.balance > 0 }
                    onSuccess(bittrexBalances)
                },
                onFailure = onFailure
        )
    }
}