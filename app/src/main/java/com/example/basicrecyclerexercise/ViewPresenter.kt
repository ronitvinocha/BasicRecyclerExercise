package com.example.basicrecyclerexercise

import com.example.basicrecyclerexercise.ApiResult
import org.json.JSONObject
import java.lang.Exception

class ViewPresenter(private var Apiresult: ApiResult):OnApiResponse,InterfaceViewPresenter{
    private var employeeList:MutableList<Employee> = ArrayList()
    override fun onCallComplete(success: Boolean, response: String?, callerId: Int) {
        if(success)
        {
            try {
                val json= JSONObject(response)
                val data=json.getJSONArray("data")
                for(i in 0 until data.length())
                {
                    var jsonObject=data.getJSONObject(i)
                    var employee:Employee=Employee(jsonObject.getInt("id"),jsonObject.getString("employee_name"),jsonObject.getInt("employee_salary"),jsonObject.getInt("employee_age"))
                    employeeList.add(employee)
                }
                Apiresult.onApiCallback(employeeList)
            }
            catch (ex:Exception)
            {
                println("❗️"+ex.localizedMessage)
            }
        }
    }

    override fun callApi() {
        val apiTask=APITask("http://dummy.restapiexample.com/api/v1/employees",this, SUB_REDDIT,null)
        apiTask.execute(null)
    }

    companion object {
        private const val SUB_REDDIT = 0x01
    }

}