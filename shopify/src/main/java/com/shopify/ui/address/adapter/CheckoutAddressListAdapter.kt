package com.shopify.ui.address.adapter

import android.content.Context
import android.view.View
import com.domain.entity.Address
import com.shopify.ui.item.CheckoutAddressItem
import com.ui.module.address.adapter.AddressListAdapter

class CheckoutAddressListAdapter(
    dataList: List<Address>,
    private val actionListener: ActionButtonListener,
    private val addressSelectListener: AddressSelectListener,
    headerOnClickListener: View.OnClickListener
) : AddressListAdapter(dataList, actionListener, headerOnClickListener) {

    var selectedAddress: Address? = null

    override fun getItemView(context: Context, viewType: Int): View {
        val item = CheckoutAddressItem(context)
        item.actionListener = actionListener
        item.addressSelectListener = addressSelectListener
        return item
    }

    override fun bindData(itemView: View, data: Address, position: Int) {
        if (itemView is CheckoutAddressItem) {
            itemView.setAddress(data, defaultAddress, selectedAddress)
        }
    }

    interface AddressSelectListener {

        fun onAddressSelected(address: Address)
    }
}