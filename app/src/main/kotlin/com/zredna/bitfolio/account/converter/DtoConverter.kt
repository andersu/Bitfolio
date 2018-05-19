package com.zredna.bitfolio.account.converter

abstract class DtoConverter<T, U> {
    fun convertToDtos(models: List<U>): List<T> = models.map { convertToDto(it) }
    fun convertToModels(dtos: List<T>): List<U> = dtos.map { convertToModel(it) }

    abstract fun convertToDto(model: U): T
    abstract fun convertToModel(dto: T): U
}