package com.zredna.bitfolio.ui.account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.zredna.bitfolio.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class AccountActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<AccountActivity>(AccountActivity::class.java)

    @Test
    fun clickingExchangesTabShowsExchangesFragment() {
        clickTab(activityTestRule.activity.getString(R.string.exchanges))

        onView(withId(R.id.recyclerViewExchanges)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingBalancesTabShowsBalancesFragment() {
        // First click exchanges so we are not already on the balances tab
        clickTab(activityTestRule.activity.getString(R.string.exchanges))

        clickTab(activityTestRule.activity.getString(R.string.balances))

        onView(withId(R.id.recyclerViewBalances)).check(matches(isDisplayed()))
    }
}