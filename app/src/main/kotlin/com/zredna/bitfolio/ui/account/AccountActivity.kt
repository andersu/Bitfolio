package com.zredna.bitfolio.ui.account

import android.content.Intent
import android.os.Bundle
import com.zredna.bitfolio.R
import androidx.appcompat.app.AppCompatActivity
import com.zredna.bitfolio.ui.account.balances.BalancesFragment
import com.zredna.bitfolio.ui.account.exchanges.ExchangesFragment
import com.zredna.bitfolio.ui.account.exchanges.addexchange.AddExchangeActivity
import com.zredna.bitfolio.ui.account.exchanges.addexchange.REQUEST_CODE_ADD_EXCHANGE
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
                supportFragmentManager,
                this
        )
        tabLayout.setCustomTabView(R.layout.view_tab_account, R.id.tabTitleView)
        tabLayout.setViewPager(viewPager)
    }
    // endregion
}
