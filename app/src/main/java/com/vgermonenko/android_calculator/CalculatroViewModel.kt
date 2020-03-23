package com.vgermonenko.android_calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val operationSet = setOf("+", "-", "*", "/")
private val geoSet = setOf("sin", "cos")
private val numbSet = setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".")


class CalculatorViewModel : ViewModel() {

    private val expression = StringBuilder("")
    val calculatorField = MutableLiveData<String>("")
    val incorrectExpression = MutableLiveData<Boolean>(false)


    private var memory : Double = 0.0
    private var isClearMode = false

    private fun updateField() {
        calculatorField.value = expression.toString()
    }

    fun clear() {
        expression.clear()
        updateField()
    }

    fun onMemoryClick(){
        if (isClearMode){
            isClearMode = false
            memory = 0.toDouble()
        }else{
            isClearMode = true
            expression.clear()
            expression.append(memory.toString())
            updateField()
        }
    }

    fun onMemoryMinusClick(){
        try {
            val list = parseField()
            val res = calculate(list)
            memory -= res
            isClearMode = false
        } catch (e: Exception) {
            incorrectExpression.value = true
            expression.clear()
            updateField()
        }
    }

    fun onMemoryPlusClick(){
        try {
            val list = parseField()
            val res = calculate(list)
            memory += res
            isClearMode = false
        } catch (e: Exception) {
            incorrectExpression.value = true
            expression.clear()
            updateField()
        }
    }

    private fun checkPriority(oper: String): Int {
        return when (oper) {
            "(" -> 0
            "*" -> 3
            "/" -> 3
            "+" -> 2
            "-" -> 2
            "sin" -> 4
            "cos" -> 4
            else -> throw IllegalArgumentException()
        }
    }

    private fun calculate(inverse: List<String>): Double {
        val stack = Stack<String>()
        for (i in inverse) {
            if (i in operationSet + geoSet) {
                val arg = stack.pop().toDouble()
                when (i) {
                    "+" -> {
                        val arg2 = stack.pop().toDouble()
                        stack.push((arg + arg2).toString())
                    }
                    "-" -> {
                        val arg2 = stack.pop().toDouble()
                        stack.push((arg2 - arg).toString())
                    }
                    "*" -> {
                        val arg2 = stack.pop().toDouble()
                        stack.push((arg * arg2).toString())
                    }
                    "/" -> {
                        val arg2 = stack.pop().toDouble()
                        val result = arg2 / arg;
                        if (result.isInfinite())
                            throw Exception("Division by zero!")

                        stack.push(result.toString())
                    }
                    "sin" ->{
                        val sin = sin(arg / 360 * 2 * PI)
                        stack.push(sin.toString())
                    }
                    "cos" ->{
                        val cos = cos(arg / 360 * 2 * PI)
                        stack.push(cos.toString())
                    }
                    else -> throw IllegalArgumentException()
                }
            } else {
                stack.push(i)
            }
        }
        return stack.pop().toDouble()
    }

    private fun parseField(): List<String> {
        val exp = expression.toString()
        val numbBuilder = StringBuilder()
        val trigBuilder = StringBuilder()
        val stack = Stack<String>()
        val inverse = mutableListOf<String>()
        for (i in exp) {
            if (i.toString() in numbSet) {
                numbBuilder.append(i)
            } else {
                if (i.toString() in operationSet + setOf("(", ")")) {
                    if (numbBuilder.isNotEmpty()) {
                        inverse.add(numbBuilder.toString())
                        numbBuilder.clear()
                    }
                    if (trigBuilder.isNotEmpty()) {
                        stack.add(trigBuilder.toString())
                        trigBuilder.clear()
                    }
                    if (i == '(') {
                        stack.push("(")
                    }
                    if (i == ')') {
                        while (stack.peek() != "(") {
                            inverse.add(stack.pop())
                        }
                        stack.pop()
                    }
                    if (i.toString() in operationSet) {
                        if (stack.isEmpty() || checkPriority(i.toString()) > checkPriority(stack.peek())) {
                            stack.push(i.toString())
                        } else {
                            while (stack.isNotEmpty() && checkPriority(stack.peek()) > checkPriority(
                                    i.toString()
                                )
                            ) {
                                inverse.add(stack.pop())
                            }
                            stack.push(i.toString())
                        }
                    }
                }else{
                    trigBuilder.append(i)
                }
            }
        }
        if (numbBuilder.isNotEmpty()) {
            inverse.add(numbBuilder.toString())
            numbBuilder.clear()
        }
        while (stack.isNotEmpty()) {
            inverse.add(stack.pop())
        }
        return inverse
    }

    fun onEqualClick() {
        try {
            val list = parseField()
            val res = calculate(list)
            expression.clear()
            expression.append(res.toString())
            updateField()
        } catch (e: Exception) {
            incorrectExpression.value = true
            expression.clear()
            updateField()
        }
    }

    fun onDeleteClick() {
        val length = expression.length
        if (length != 0) {
            if (length >= 4) {
                val sub = expression.substring(length - 4)
                if (sub == "cos(" || sub == "sin(") {
                    expression.delete(length - 4, length)
                } else {
                    expression.delete(length - 1, length)
                }
            } else {
                expression.delete(length - 1, length)
            }
        }
        updateField()
    }

    fun onDotClick() {
        expression.append(".")
        updateField()
    }

    fun onOpenClick() {
        expression.append("(")
        updateField()
    }

    fun onCloseClick() {
        expression.append(")")
        updateField()
    }

    fun onCosCLick() {
        expression.append("cos(")
        updateField()
    }

    fun onSinClick() {
        expression.append("sin(")
        updateField()
    }

    fun onDivClick() {
        expression.append("/")
        updateField()
    }

    fun onMultiClick() {
        expression.append("*")
        updateField()
    }

    fun onMinusClick() {
        expression.append("-")
        updateField()
    }

    fun onPlusClick() {
        expression.append("+")
        updateField()
    }

    fun on0Click() {
        expression.append("0")
        updateField()
    }

    fun on1Click() {
        expression.append("1")
        updateField()
    }

    fun on2Click() {
        expression.append("2")
        updateField()
    }

    fun on3Click() {
        expression.append("3")
        updateField()
    }

    fun on4Click() {
        expression.append("4")
        updateField()
    }

    fun on5Click() {
        expression.append("5")
        updateField()
    }

    fun on6Click() {
        expression.append("6")
        updateField()
    }

    fun on7Click() {
        expression.append("7")
        updateField()
    }

    fun on8Click() {
        expression.append("8")
        updateField()
    }

    fun on9Click() {
        expression.append("9")
        updateField()
    }
}
