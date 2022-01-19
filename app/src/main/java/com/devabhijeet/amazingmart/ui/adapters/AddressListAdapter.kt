package com.devabhijeet.amazingmart.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.devabhijeet.amazingmart.R
import com.devabhijeet.amazingmart.models.Address
import com.devabhijeet.amazingmart.ui.activities.AddEditAddressActivity
import com.devabhijeet.amazingmart.ui.activities.CheckoutActivity
import com.devabhijeet.amazingmart.utils.Constants
import kotlinx.android.synthetic.main.item_address_layout.view.*


/**
 * An adapter class for AddressList adapter.
 */
// TODO Step 8: Add the parameter to pass the value of address selection so when the user is about to add, edit or, delete the address he should not be able to select the address.
open class AddressListAdapter(private val context: Context,private var list: ArrayList<Address>,private val selectAddress: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_address_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_address_full_name_item_address.text = model.name
            holder.itemView.tv_address_type_item_address.text = model.type
            holder.itemView.tv_address_details_item_address.text = "${model.address}, ${model.zipCode}"
            holder.itemView.tv_address_mobile_number_item_address.text = model.mobileNumber

            // TODO Step 10: Assign the click event to the address item when user is about to select the address.
            // START
            if (selectAddress) {
                holder.itemView.setOnClickListener {

                    val intent = Intent(context, CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                    context.startActivity(intent)
                }
            }
            // END
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to edit the address details and pass the existing details through intent.
     *
     * @param activity
     * @param position
     */
    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])

        // TODO Step 15: Make it startActivityForResult instead of startActivity.
        // START
        // activity.startActivity (intent)

        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        // END

        notifyItemChanged(position) // Notify any registered observers that the item at position has changed.
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}