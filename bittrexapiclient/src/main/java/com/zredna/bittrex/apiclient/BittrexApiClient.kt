package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import io.reactivex.Single
import java.util.*

class BittrexApiClient private constructor(
        private val bittrexAccountApi: BittrexAccountApi,
        private val bittrexPublicApi: BittrexPublicApi
) {
    fun getBalances(): Single<GetBalancesResponseDto> {
        val nonce = Date().time
        return bittrexAccountApi.getBalances(nonce.toString())
    }

    fun getMarketSummaries(): Single<GetMarketSummariesResponseDto> {
        return bittrexPublicApi.getMarketSummaries()
    }

    class Builder {
        private val bittrexAccountApiProvider = BittrexAccountApiProvider()
        private val bittrexPublicApiProvider = BittrexPublicApiProvider()

        fun setLoggingEnabled(): Builder {
            bittrexAccountApiProvider.setLoggingEnabled()
            bittrexPublicApiProvider.setLoggingEnabled()

            return this
        }

        fun build(bittrexCredentialsProvider: BittrexCredentialsProvider): BittrexApiClient {
            bittrexAccountApiProvider.setCredentialsProvider(bittrexCredentialsProvider)
            return BittrexApiClient(
                    bittrexAccountApiProvider.provideBittrexAccountApi(),
                    bittrexPublicApiProvider.provideBittrexPublicApi()
            )
        }
    }
}
