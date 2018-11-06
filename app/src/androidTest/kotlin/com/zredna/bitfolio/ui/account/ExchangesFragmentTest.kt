package com.zredna.bitfolio.ui.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.zredna.bitfolio.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ExchangesFragmentTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<AccountActivity>(AccountActivity::class.java)

    @Before
    fun setUp() {
        clickTab(activityTestRule.activity.getString(R.string.exchanges))
    }

    @Test
    fun clickingAddExchangeButtonShowsAddExchangeActivity() {
        onView(withId(R.id.addExchangeButton)).perform(click())

        onView(withId(R.id.selectExchangeLabel)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}