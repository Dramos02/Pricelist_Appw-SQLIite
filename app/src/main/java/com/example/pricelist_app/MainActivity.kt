package com.example.pricelist_app

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pricelist_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.foodBtn.setOnClickListener {
            binding.drinkBtn.isChecked = false
            binding.otherBtn.isChecked = false
        }

        binding.drinkBtn.setOnClickListener {
            binding.foodBtn.isChecked = false
            binding.otherBtn.isChecked = false
        }

        binding.otherBtn.setOnClickListener {
            binding.foodBtn.isChecked = false
            binding.drinkBtn.isChecked = false
        }

        binding.addBtn.setOnClickListener {
            if (!binding.foodBtn.isChecked && !binding.drinkBtn.isChecked && !binding.otherBtn.isChecked) {
                Toast.makeText(
                    applicationContext,
                    "Please Don't Leave it Blank/Empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val category = when {
                    binding.foodBtn.isChecked -> "Food"
                    binding.drinkBtn.isChecked -> "Drink"
                    binding.otherBtn.isChecked -> "Other"
                    else -> ""
                }

                if (category.isNotEmpty()) {
                    addRec(category)
                    binding.foodBtn.isChecked = false
                    binding.drinkBtn.isChecked  = false
                    binding.otherBtn.isChecked = false
                }
            }
        }

        binding.viewBtn.setOnClickListener {
            val intentview = Intent(applicationContext, view::class.java)
            Toast.makeText(applicationContext, "ALL PRODUCTS", Toast.LENGTH_SHORT).show()
            startActivity(intentview)
        }

        binding.foodFilterBtn.setOnClickListener {
            viewCategory("Food")
            Toast.makeText(applicationContext, "FOOD PRODUCTS", Toast.LENGTH_SHORT).show()
        }

        binding.drinkFilterBtn.setOnClickListener {
            viewCategory("Drink")
            Toast.makeText(applicationContext, "DRINK PRODUCTS", Toast.LENGTH_SHORT).show()
        }

        binding.otherFilterBtn.setOnClickListener {
            viewCategory("Other")
            Toast.makeText(applicationContext, "OTHER PRODUCTS", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addRec(category: String){
        val nameofProduct = binding.productId.text.toString()
        val priceofProduct = binding.priceId.text.toString()

        if (nameofProduct.isNotEmpty() && priceofProduct.isNotEmpty()) {
            try {
                val db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE, null)
                db.execSQL("CREATE TABLE IF NOT EXISTS tblproduct(id INTEGER PRIMARY KEY AUTOINCREMENT,f_prodname VARCHAR, f_prodprice VARCHAR, f_category VARCHAR)")

                val mysql = "INSERT INTO tblproduct(f_prodname, f_prodprice, f_category) VALUES(?, ?, ?)"
                val statement: SQLiteStatement = db.compileStatement(mysql)
                statement.bindString(1, nameofProduct)
                statement.bindString(2, priceofProduct)
                statement.bindString(3, category)
                statement.execute()

                Toast.makeText(applicationContext, "Record Added Successfully", Toast.LENGTH_SHORT).show()
                binding.productId.text.clear()
                binding.priceId.text.clear()
                binding.productId.requestFocus()

            } catch (exception: Exception) {
                Toast.makeText(applicationContext, "Record Added Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Please Fill up all the Fields", Toast.LENGTH_SHORT).show()
        }
    }
    private fun viewCategory(category: String) {
        val intent = Intent(applicationContext, view::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }
}
