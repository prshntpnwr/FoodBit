package com.example.foodBit.binding


import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment)  {

    @BindingAdapter("viewVisible")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        val imageUrl = if (url.isNullOrEmpty())
            "https://b.zmtcdn.com/data/reviews_photos/463/6b9cc20b7488c30b7e24ac44f637b463_1560157674.jpg?crop=1001%3A1001%3B0%2C499&fit=around%7C200%3A200"
        else
            url
        val circularProgressDrawable = CircularProgressDrawable(fragment.requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(fragment.context).load(imageUrl).placeholder(circularProgressDrawable).into(imageView)
    }

    @BindingAdapter(value = ["chipList"], requireAll = true)
    fun bindChips(group: ChipGroup, values: String?) {
        group.removeAllViews()
        if (!values.isNullOrEmpty()) {
            val list = values.split(",")
            for ((index, item) in list.withIndex()) {
                val chip = Chip(this.fragment.requireContext())
                chip.id = index
                chip.text = item
                chip.isCheckable = true
                chip.isCheckedIconVisible = true
                group.addView(chip)
            }
        } else {
            val chip = Chip(this.fragment.requireContext())
            chip.text = "NA"
            chip.isCheckable = false
            chip.isCheckedIconVisible = false
            group.addView(chip)
        }
    }

}