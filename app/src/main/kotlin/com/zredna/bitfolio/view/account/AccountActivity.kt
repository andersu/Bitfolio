package com.zredna.bitfolio.view.account

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.zredna.bitfolio.R
import com.zredna.bitfolio.view.addexchange.AddExchangeActivity
import com.zredna.bitfolio.view.addexchange.REQUEST_CODE_ADD_EXCHANGE
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {

    // region Setup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initView()
    }

    private fun initView() {
        supportActionBar?.elevation = 0f

        val exchangesFragment = ExchangesFragment()
        exchangesFragment.onAddExchangeClicked = {
            startActivityForResult(
                    Intent(this, AddExchangeActivity::class.java),
                    REQUEST_CODE_ADD_EXCHANGE
            )
        }

        viewPager.adapter = AccountPagerAdapter(
                listOf(BalancesFragment(), exchangesFragment),
                supportFragmentManager
        )
        tabLayout.setCustomTabView(R.layout.view_tab_account, R.id.tabTitleView)
        tabLayout.setViewPager(viewPager)
    }
    // endregion
}
