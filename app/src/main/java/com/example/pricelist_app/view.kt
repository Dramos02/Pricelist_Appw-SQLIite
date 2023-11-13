package com.example.pricelist_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.pricelist_app.databinding.ActivityViewBinding

class view : AppCompatActivity() {

    private lateinit var binding: ActivityViewBinding
    private lateinit var viewList: ListView
    private val productlist = ArrayList<String>()
    private lateinit var prodListAdapter: ArrayAdapter<String>

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db: SQLiteDatabase = openOrCreateDatabase("dbaseprod", Context.MODE_PRIVATE, null)

        viewList = binding.listId

        binding.backBtn.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        val category = intent.getStringExtra("category")

        val query = if (category != null) {
            "SELECT * FROM tblproduct WHERE f_category = ? ORDER BY f_prodname"
        } else {
            "SELECT * FROM tblproduct ORDER BY f_prodname"
        }

        val tmptable: Cursor = db.rawQuery(query, if (category != null) arrayOf(category) else null)
        val id: Int = tmptable.getColumnIndex("id")
        val prodname: Int = tmptable.getColumnIndex("f_prodname")
        val prodprice: Int = tmptable.getColumnIndex("f_prodprice")
        val categories: Int = tmptable.getColumnIndex("f_category")

        productlist.clear()

        prodListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productlist)
        viewList.adapter = prodListAdapter


        val productdetails = ArrayList<product>()

        if (tmptable.moveToFirst()) {
            do {
                val prod = product()
                prod.refid = tmptable.getString(id)
                prod.productname = tmptable.getString(prodname)
                prod.productprice = tmptable.getString(prodprice)
                prod.categories = tmptable.getString(categories)
                productdetails.add(prod)

                val formattedProduct =
                    "${tmptable.getString(prodname)} - ₱${tmptable.getString(prodprice)} - ${
                        tmptable.getString(categories)
                    }"
                productlist.add(formattedProduct)
            } while (tmptable.moveToNext())
            prodListAdapter.notifyDataSetChanged()
            viewList.invalidateViews()
        }

        viewList.setOnItemClickListener { adapterView, view, i, l ->
            val prod = productdetails[i]
            val i_edel = Intent(applicationContext, Edit::class.java)

            i_edel.putExtra("id", prod.refid)
            i_edel.putExtra("prod", prod.productname)
            i_edel.putExtra("price", prod.productprice)
            finish()
            startActivity(i_edel)
        }
    }
}