package com.zredna.bitfolio.view.account

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zredna.bitfolio.R
import com.zredna.bitfolio.view.addexchange.AddExchangeActivity
import com.zredna.bitfolio.view.addexchange.REQUEST_CODE_ADD_EXCHANGE
import kotlinx.android.synthetic.main.fragment_exchanges.*

class ExchangesFragment: Fragment() {

    lateinit var onAddExchangeClicked: () -> Unit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exchanges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addExchangeButton.setOnClickListener {
            onAddExchangeClicked()
        }
    }
}