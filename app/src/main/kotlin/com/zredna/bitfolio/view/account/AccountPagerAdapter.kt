package com.zredna.bitfolio.view.account

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zredna.bitfolio.view.account.balances.BalancesFragment
import com.zredna.bitfolio.view.account.exchanges.ExchangesFragment

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