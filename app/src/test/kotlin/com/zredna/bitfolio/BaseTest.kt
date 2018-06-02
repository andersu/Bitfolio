package com.zredna.bitfolio

import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

open class BaseTest {
    @Before
    open fun init() {
        MockitoAnnotations.initMocks(this)
    }

    protected fun <T> eqKotlin(value: T): T {
        Mockito.eq<T>(value)
        return uninitialized()
    }

    protected fun <T> anyKotlin(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}