package com.zredna.bitfolio.view.account

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class AccountPagerAdapter(
        private val fragments: List<Fragment>,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(getItem(position)) {
            is BalancesFragment -> "Balances"
            is ExchangesFragment -> "Exchanges"
            else -> ""
        }
    }

}