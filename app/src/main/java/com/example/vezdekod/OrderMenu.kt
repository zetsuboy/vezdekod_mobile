package com.example.vezdekod

import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.view.View
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_menu)
        val scrollView : HorizontalScrollView = findViewById<HorizontalScrollView>(R.id.TypesOfFoodHorizontalScrollView)
        var linear_layout = LinearLayout(this)

        val food_types = arrayOf("Роллы", "Суши", "Наборы", "Горячие блюда", "Пицца", "Бургеры", "Напитки")
        var i = 1
        food_types.forEach{
            var btn = Button(this)
            btn.id = i++
            btn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            btn.text = it
            btn.setBackgroundResource(R.drawable.button_type_states)
            btn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    if (view.isSelected != true) {
                        view.isSelected = true
                    }
                    else {
                        view.isSelected = false
                    }
                    for (j in 1..food_types.size) {
                        if (j != view.id) {
                            findViewById<Button>(j).isSelected = false
                        }
                    }
                }
            })

            linear_layout.addView(btn)
        }
        scrollView.addView(linear_layout)
    }
}