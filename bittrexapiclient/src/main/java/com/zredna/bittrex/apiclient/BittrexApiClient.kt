package com.zredna.bittrex.apiclient

import com.zredna.bittrex.apiclient.dto.GetBalancesResponseDto
import com.zredna.bittrex.apiclient.dto.GetMarketSummariesResponseDto
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

private const val API_KEY = "d89007c1259747ecb8ec3f7dca70a976"

class BittrexApiClient private constructor(
        private val bittrexAccountApi: BittrexAccountApi,
        private val bittrexPublicApi: BittrexPublicApi
) {

    fun getBalances(): Single<GetBalancesResponseDto> {
        val nonce = Date().time
        return bittrexAccountApi.getBalances(API_KEY, nonce.toString())
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

        fun build(): BittrexApiClient {
            return BittrexApiClient(
                    bittrexAccountApiProvider.provideBittrexAccountApi(),
                    bittrexPublicApiProvider.provideBittrexPublicApi()
            )
        }
    }
}
