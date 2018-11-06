package com.zredna.bitfolio.ui.account

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.zredna.bitfolio.R
import com.zredna.bitfolio.ui.account.balances.BalancesFragment
import com.zredna.bitfolio.ui.account.exchanges.ExchangesFragment

class AccountPagerAdapter(
        private val fragments: List<Fragment>,
        fragmentManager: FragmentManager,
        private val context: Context
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val fragment = getItem(position)
        return when(fragment) {
            is BalancesFragment -> context.getString(R.string.balances)
            is ExchangesFragment -> context.getString(R.string.exchanges)
            else -> ""
        }
    }

}