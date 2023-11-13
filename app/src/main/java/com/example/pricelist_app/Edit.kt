package com.example.pricelist_app

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pricelist_app.databinding.ActivityEditBinding

class Edit : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private var originalName = ""
    private var originalPrice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val i_edel = intent
        val col1 = i_edel.getStringExtra("id").toString()
        val col2 = i_edel.getStringExtra("prod").toString()
        val col3 = i_edel.getStringExtra("price").toString()

        binding.recordId.text = col1
        binding.editProdId.setText(col2)
        binding.editPriceId.setText(col3)


        originalName = binding.editProdId.text.toString()
        originalPrice = binding.editPriceId.text.toString()

        binding.backBtn2.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnEditId.setOnClickListener {
            editrec()
        }

        binding.btnDeleteId.setOnClickListener {
            deleterec()
        }
    }
    private fun editrec() {
        val newName = binding.editProdId.text.toString()
        val newPrice = binding.editPriceId.text.toString()

        if (newName != originalName || newPrice != originalPrice) {
            if (newName.isNotEmpty() && newPrice.isNotEmpty()) {
                try {
                    val recnum = binding.recordId.text.toString()
                    val db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE, null)

                    val mysql = "update tblproduct set f_prodname = ?, f_prodprice = ? where id = ?"
                    val statement: SQLiteStatement = db.compileStatement(mysql)
                    statement.bindString(1, newName)
                    statement.bindString(2, newPrice)
                    statement.bindString(3, recnum)
                    statement.execute()

                    Toast.makeText(
                        applicationContext,
                        "Record Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                    val intentback = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intentback)
                } catch (exception: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Record Saved Unsuccessfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please Fill up all the Fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Please Edit the Product Name or the Product Price",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun deleterec(){
        val recnum = binding.recordId.text.toString()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this Product Data?")

        builder.setPositiveButton("Yes") { _, _ ->
            try {
                val db = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE, null)

                val mysql = "delete from tblproduct where id = ?"
                val statement: SQLiteStatement = db.compileStatement(mysql)
                statement.bindString(1, recnum)
                statement.execute()

                Toast.makeText(applicationContext, "Record Deleted Successfully", Toast.LENGTH_SHORT).show()
                finish()
                val intentback = Intent(applicationContext, MainActivity::class.java)
                startActivity(intentback)
            } catch (exception: Exception) {
                Toast.makeText(applicationContext, "Record Deleted Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
