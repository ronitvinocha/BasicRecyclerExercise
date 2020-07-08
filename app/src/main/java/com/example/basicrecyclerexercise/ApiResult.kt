package com.example.basicrecyclerexercise

interface ApiResult{
    fun onApiCallback(employeeList: MutableList<Employee>)
}