package com.zredna.bitfolio.service

/*class BittrexBalanceService(
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
}*/