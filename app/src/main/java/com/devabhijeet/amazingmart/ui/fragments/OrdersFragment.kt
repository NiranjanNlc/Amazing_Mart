package com.devabhijeet.amazingmart.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devabhijeet.amazingmart.R
import com.devabhijeet.amazingmart.firestore.FirestoreClass
import com.devabhijeet.amazingmart.models.Order
import com.devabhijeet.amazingmart.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {

   // private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        val root =  inflater.inflate(R.layout.fragment_orders, container, false)

         return root
    }



    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }

    /**
     * A function to get the list of my orders.
     */
    private fun getMyOrdersList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@OrdersFragment)
    }

    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        // Hide the progress dialog.
        hideProgressDialog()

        if (ordersList.size > 0) {

            rv_my_order_items_order_fragment.visibility = View.VISIBLE
            tv_no_orders_found_order_fragment.visibility = View.GONE

            rv_my_order_items_order_fragment.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items_order_fragment.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
            rv_my_order_items_order_fragment.adapter = myOrdersAdapter
        } else {
            rv_my_order_items_order_fragment.visibility = View.GONE
            tv_no_orders_found_order_fragment.visibility = View.VISIBLE
        }
    }



}