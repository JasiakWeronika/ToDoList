package com.example.todo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyArrayAdapter(context: Context, var data: ArrayList<ToDoList>) :
    ArrayAdapter<ToDoList>(context, R.layout.example, data) {
    private class ViewHolder(view: View){
        var descriptionExample: TextView? = null
        var priorityExample: TextView? = null
        var timeExample: TextView? = null
        var dateExample: TextView? = null
        var imageExample: ImageView? = null
        init {
            this.descriptionExample = view.findViewById(R.id.descriptionExample)
            this.priorityExample = view.findViewById(R.id.priorityExample)
            this.timeExample = view.findViewById(R.id.timeExample)
            this.dateExample = view.findViewById(R.id.dateExample)
            this.imageExample = view.findViewById(R.id.imageExample)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view = convertView
        if (view == null) {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.example, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view?.tag as ViewHolder
        }
        setDescription(viewHolder.descriptionExample, data[position].description)
        setPriority(viewHolder.priorityExample, data[position].priority)
        setTime(viewHolder.timeExample, data[position].time)
        setDate(viewHolder.dateExample, data[position].date)
        setImage(viewHolder.imageExample, data[position].image)
        return view as View
    }

    private fun setDescription(descriptionExample: TextView?, description: String) {
        if(description == "") {
            descriptionExample?.text = ("Zadanie").toEditable()

        } else {
            descriptionExample?.text = description.toEditable()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPriority(priorityExample: TextView?, priority: Int) {
        when(priority) {
            0 -> {
                priorityExample?.text = "normalny"
                priorityExample?.setTextColor(Color.parseColor("#00FF00"))
            }
            1 -> {
                priorityExample?.text = "średnioważny"
                priorityExample?.setTextColor(Color.parseColor("#FFEF00"))
            }
            2 -> {
                priorityExample?.text = "ważny"
                priorityExample?.setTextColor(Color.parseColor("#FF0000"))
            }
        }
    }

    private fun setTime(timeExample: TextView?, time: String) {
        if(time == "HH:MM") {
            timeExample?.text = ""
        } else {
            timeExample?.text = time
        }
    }

    private fun setDate(dateExample: TextView?, date: String) {
        if(date == "DD.MM.YYYY") {
            dateExample?.text = ""
        } else {
            dateExample?.text = date
        }
    }

    private fun setImage(imageExample: ImageView?, image: String) {
        when(image) {
            "star" -> imageExample?.setImageResource(R.drawable.ic_star_border_black_30dp)
            "heart" -> imageExample?.setImageResource(R.drawable.ic_favorite_border_black_30dp)
            "school" -> imageExample?.setImageResource(R.drawable.ic_notifications_none_black_30dp)
            "eat" -> imageExample?.setImageResource(R.drawable.ic_restaurant_black_30dp)
            "travel" -> imageExample?.setImageResource(R.drawable.ic_directions_car_black_30dp)
            "work" -> imageExample?.setImageResource(R.drawable.ic_business_center_black_30dp)
            "phone" -> imageExample?.setImageResource(R.drawable.ic_call_black_30dp)
            "cake" -> imageExample?.setImageResource(R.drawable.ic_cake_black_30dp)
            "buy" -> imageExample?.setImageResource(R.drawable.ic_local_atm_black_30dp)
            "nothing" -> imageExample?.setImageResource(R.drawable.ic_star_border_black_30dp)
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}
