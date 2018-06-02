package com.zredna.bitfolio.converter

import com.zredna.bitfolio.Balance
import com.zredna.bittrex.apiclient.dto.BalanceDto

class BittrexBalanceDtoConverter : DtoConverter<BalanceDto, Balance>() {
    override fun convertToDto(model: Balance): BalanceDto {
        // Not needed
        throw NotImplementedError()
    }

    override fun convertToModel(dto: BalanceDto): Balance =
            Balance(dto.currency, dto.balance)
}