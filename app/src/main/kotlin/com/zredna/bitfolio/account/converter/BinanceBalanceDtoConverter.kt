package com.zredna.bitfolio.account.converter

import com.zredna.binanceapiclient.dto.BalanceDto
import com.zredna.bitfolio.account.Balance

class BinanceBalanceDtoConverter: DtoConverter<BalanceDto, Balance>() {
    override fun convertToDto(model: Balance): BalanceDto {
        throw NotImplementedError()
    }

    override fun convertToModel(dto: BalanceDto): Balance =
        Balance(dto.asset, dto.free.toDouble() + dto.locked.toDouble())
}