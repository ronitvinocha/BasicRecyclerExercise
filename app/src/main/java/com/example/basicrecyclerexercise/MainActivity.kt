package com.example.basicrecyclerexercise

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),ApiResult {
    private var viewPresenter:InterfaceViewPresenter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initui()
    }

    private fun initui() {
        viewPresenter=ViewPresenter(this)
        callAPI()
    }
    private fun isNetworkConnected(): Boolean {
      val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val activeNetwork = connectivityManager.activeNetwork
      val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
      return networkCapabilities != null &&
          networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    private fun callAPI()
    {
        println("✌️ api call")
        if (isNetworkConnected()) {
          viewPresenter!!.callApi()
        } else {
          AlertDialog.Builder(this).setTitle("No Internet Connection")
              .setMessage("Please check your internet connection and try again")
              .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
        }
    }
    override fun onApiCallback(employeeList: MutableList<Employee>) {
        employeeList.map { employee -> println("✌️ ${employee.employee_name}") }
        progressbar.visibility=View.GONE
        recycler.visibility=View.VISIBLE
        recycler.adapter=EmployeeAdapter(employeeList)
    }
    inner class EmployeeAdapter internal constructor(
            private val values: List<Employee>
    ) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycelitem, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val employee = values[position]
            val font1:Typeface= Typeface.createFromAsset(assets, "fonts/roboto_medium.ttf")
            val font2:Typeface= Typeface.createFromAsset(assets, "fonts/roboto_regular.ttf")
            val employeeName= SpannableStringBuilder("Name: "+employee.employee_name)
            val employeeAge=SpannableStringBuilder("Age: "+employee.employee_age)
            val employeeSalery=SpannableStringBuilder("Salery: "+employee.employee_salary)
            employeeName.setSpan(CustomTypefaceSpan("",font1),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            employeeName.setSpan(CustomTypefaceSpan("",font2),7,employeeName.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            employeeAge.setSpan(CustomTypefaceSpan("",font1),0,5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            employeeAge.setSpan(CustomTypefaceSpan("",font2),6,employeeAge.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            employeeSalery.setSpan(CustomTypefaceSpan("",font1),0,8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            employeeSalery.setSpan(CustomTypefaceSpan("",font2),9,employeeSalery.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            holder.nameText.text = employeeName
            holder.ageText.text= employeeAge
            holder.saleryText.text=employeeSalery
        }

        override fun getItemCount(): Int = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameText: TextView = view.findViewById(R.id.employee_name)
            val ageText: TextView = view.findViewById(R.id.employee_age)
            val saleryText: TextView = view.findViewById(R.id.employee_salery)

        }
    }
}