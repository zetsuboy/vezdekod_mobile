package com.example.vezdekod

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import android.graphics.Paint
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams

data class Categorie(var id: Int, var name: String) {
}
data class Tag(val id: Int, val name: String) {
}
data class Product(val id: Int, val category_id: Int, val name: String, val description: String, val image: String, val price_current: Int,
                   val price_old: Int, val measure: Int, val measure_unit: String, val energy_per_100_grams: Float, val proteins_per_100_grams: Float,
                   val fats_per_100_grams: Float, val carbohydrates_per_100_grams: Float, val tag_ids: List<Int>) {
}

class OrderMenu : AppCompatActivity() {
    var selected_button_id : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_menu)

        var gson : Gson = Gson()
        val json_tags: String = applicationContext.assets.open("tags.json").bufferedReader().readText()
        val json_categories: String = applicationContext.assets.open("categories.json").bufferedReader().readText()
        val json_products: String = applicationContext.assets.open("products.json").bufferedReader().readText()

        var tags : List<Tag> =  GsonBuilder().create().fromJson(json_tags, Array<Tag>::class.java).toList()
        var categories : List<Categorie> =  GsonBuilder().create().fromJson(json_categories, Array<Categorie>::class.java).toList()
        var products : List<Product> =  GsonBuilder().create().fromJson(json_products, Array<Product>::class.java).toList()

        val scrollView : HorizontalScrollView = findViewById<HorizontalScrollView>(R.id.TypesOfFoodHorizontalScrollView)
        var linear_layout = LinearLayout(this)

        var i = 1
        categories.forEach{
            var btn = Button(this)
            btn.id = i++
            var btn_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            btn_lp.setMargins(7,0,0,0)
            btn.layoutParams = btn_lp
            btn.text = it.name
            btn.setBackgroundResource(R.drawable.button_type_states)
            btn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    if (view.isSelected != true) {
                        selected_button_id = btn.id
                        view.isSelected = true
                    }
                    else {
                        selected_button_id = 0
                        view.isSelected = false
                    }
                    for (j in 1..categories.size) {
                        if (j != view.id) {
                            findViewById<Button>(j).isSelected = false
                        }
                    }
                }
            })

            linear_layout.addView(btn)
        }
        scrollView.addView(linear_layout)

        var table: LinearLayout = findViewById<LinearLayout>(R.id.food_table)
        var tableRow : LinearLayout = LinearLayout(this)
        var tableRow_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.FILL_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tableRow_lp.bottomMargin = 10
        tableRow.layoutParams = tableRow_lp
        val food_title_font_size = 16
        var table_counter : Int = 0

        products.forEach{
            if (table_counter == 1) {
                var space : Space = Space(this)
                var space_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                space_lp.weight = (0.05).toFloat()
                space.layoutParams = space_lp
                tableRow.addView(space)
            }
            else if (table_counter == 2) {
                table_counter = 0
                table.addView(tableRow)
                tableRow = LinearLayout(this)
                tableRow_lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tableRow_lp.bottomMargin = 10
                tableRow.layoutParams = tableRow_lp
            }
            var item_body : LinearLayout = LinearLayout(this)
            var item_body_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                0,
                900
            )
            item_body.setPadding(10, 10, 10, 8)
            item_body_lp.weight = (0.75).toFloat()
            item_body.layoutParams = item_body_lp
            item_body.orientation = LinearLayout.VERTICAL
            item_body.setBackgroundResource(R.drawable.food_layout_border)

            var food_picture : ImageView = ImageView(this)
            var foor_picture_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            food_picture.layoutParams = foor_picture_lp
            food_picture.setBackgroundResource(R.drawable.food_picture)

            item_body.addView(food_picture)

            var food_title : TextView = TextView(this)
            var food_title_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            food_title_lp.setMargins(10,10,10,10)
            food_title.layoutParams = food_title_lp
            food_title.text = it.name
            food_title.textSize = food_title_font_size.toFloat()

            item_body.addView(food_title)

            var food_mass : TextView = TextView(this)
            var food_mass_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            food_mass_lp.setMargins(10,0,0,5)
            food_mass.layoutParams = food_mass_lp
            food_mass.text = it.measure.toString() + " " + it.measure_unit
            food_mass.textSize = food_title_font_size.toFloat()

            item_body.addView(food_mass)

            var button_panel : LinearLayout = LinearLayout(this)
            var button_panel_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(320, 80)
            button_panel_lp.gravity = Gravity.CENTER
            button_panel_lp.setMargins(0,0,0,5)
            button_panel.layoutParams = button_panel_lp
            button_panel.isClickable = true
            button_panel.orientation = LinearLayout.HORIZONTAL
            button_panel.setBackgroundResource(R.drawable.food_price_button)
//            var item_body_id : Int = item_body.id
//            var constraintLayout : ConstraintLayout = item_body as ConstraintLayout;
//            var constraintSet : ConstraintSet = ConstraintSet();
//            constraintSet.clone(constraintLayout);
//            constraintSet.connect(button_panel.id,
//                ConstraintSet.BOTTOM,item_body_id, ConstraintSet.BOTTOM,12);
//            constraintSet.applyTo(constraintLayout);

            var food_current_price : TextView = TextView(this)
            var food_current_price_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            food_current_price_lp.gravity=Gravity.CENTER
            food_current_price_lp.weight = (1).toFloat()
            food_current_price.layoutParams = food_current_price_lp
            food_current_price.text = it.price_current.toString()
            food_current_price.setTextAppearance(com.google.android.material.R.style.TextAppearance_AppCompat_Body2)
            button_panel.addView(food_current_price)

            if (it.price_old != 0) {
                var food_old_price : TextView = TextView(this)
                var food_old_price_lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                food_old_price_lp.gravity=Gravity.CENTER
                food_old_price_lp.weight = (1).toFloat()
                food_old_price.layoutParams = food_old_price_lp
                food_old_price.text = it.price_old.toString()
                food_old_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                button_panel.addView(food_old_price)
            }

            item_body.addView(button_panel)
            tableRow.addView(item_body)
            table_counter++
        }
    }
}