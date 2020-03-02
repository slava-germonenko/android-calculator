package com.vgermonenko.android_calculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vgermonenko.android_calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.deleteButton.setOnLongClickListener {
            viewModel.clear()
            true
        }
        viewModel.incorrectExpression.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Incorrect expression", Toast.LENGTH_SHORT).show()
                viewModel.incorrectExpression.value = false
            }
        })
    }
}
