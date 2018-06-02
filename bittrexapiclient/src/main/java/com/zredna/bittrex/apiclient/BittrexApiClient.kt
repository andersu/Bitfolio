package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    class Builder() {
        private val bittrexAccountApiProvider = BittrexAccountApiProvider()
        private val bittrexPublicApiProvider = BittrexPublicApiProvider()

        fun setLoggingEnabled(): Builder {
            bittrexAccountApiProvider.setLoggingEnabled()
            bittrexPublicApiProvider.setLoggingEnabled()

            return this
        }

        fun build(credentialsProvider: CredentialsProvider): BittrexApiClient {
            bittrexAccountApiProvider.setCredentialsProvider(credentialsProvider)
            return BittrexApiClient(
                    bittrexAccountApiProvider.provideBittrexAccountApi(),
                    bittrexPublicApiProvider.provideBittrexPublicApi()
            )
        }
    }
}
