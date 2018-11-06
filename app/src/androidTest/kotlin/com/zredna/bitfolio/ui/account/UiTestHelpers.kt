package com.zredna.bitfolio.ui.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.zredna.bitfolio.R
import org.hamcrest.Matchers

fun clickTab(text: String) {
    val exchangesMatcher = Matchers.allOf(withText(text), isDescendantOfA(withId(R.id.tabLayout)))
    onView(exchangesMatcher).perform(click())
}