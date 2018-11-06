package com.zredna.bitfolio.ui.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.zredna.bitfolio.R
import com.zredna.bitfolio.ui.account.exchanges.addexchange.AddExchangeActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddExchangeActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<AddExchangeActivity>(AddExchangeActivity::class.java)

    @Test
    fun clickingBittrexShowsSecretAndApiKeyViews() {
        val views = listOf(R.id.secretLabel, R.id.secretInput, R.id.apiKeyLabel, R.id.apiKeyInput)
        views.forEach {
            onView(withId(it)).check(matches(not(isDisplayed())))
        }

        onView(withId(R.id.bittrexButton)).perform(click())

        views.forEach {
            onView(withId(it)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun clickingBinanceShowsSecretAndApiKeyViews() {
        val views = listOf(R.id.secretLabel, R.id.secretInput, R.id.apiKeyLabel, R.id.apiKeyInput)
        views.forEach {
            onView(withId(it)).check(matches(not(isDisplayed())))
        }

        onView(withId(R.id.binanceButton)).perform(click())

        views.forEach {
            onView(withId(it)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun fillingApiKeyAndSecretEnablesAddExchangeButton() {
        onView(withId(R.id.addExchangeButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.bittrexButton)).perform(click())

        onView(withId(R.id.apiKeyInput)).perform(typeText("apiKey"))
        onView(withId(R.id.secretInput)).perform(typeText("secret"))

        onView(withId(R.id.addExchangeButton)).check(matches(isEnabled()))
    }
}
