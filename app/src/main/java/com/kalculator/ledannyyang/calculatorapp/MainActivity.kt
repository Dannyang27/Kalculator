package com.kalculator.ledannyyang.calculatorapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    var currentHistory : String = ""
    var currentResult : String = ""
    var currentOperator : String = ""

    var firstOperand: Double? = null
    var secondOperand: Double? = null

    var operatorFlag : Boolean = false
    var operandFlag : Boolean = false
    var divideByZeroFlag: Boolean = false

    var calculated = false

    var ans : Double? = null

    val operatorList = listOf("+/-", "%", "/", "x", "-", "+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        initNumberPad()
        initOperatorPad()
    }

    fun initNumberPad() : Unit{

        zero.setOnClickListener{
            putNumber("0")
        }
        one.setOnClickListener{
            putNumber("1")
        }
        two.setOnClickListener{
            putNumber("2")
        }
        three.setOnClickListener{
            putNumber("3")
        }
        four.setOnClickListener{
            putNumber("4")
        }
        five.setOnClickListener{
            putNumber("5")
        }
        six.setOnClickListener{
            putNumber("6")
        }
        seven.setOnClickListener{
            putNumber("7")
        }
        eight.setOnClickListener{
            putNumber("8")
        }
        nine.setOnClickListener{
            putNumber("9")
        }
        dot.setOnClickListener{
            putDot(".")
        }
    }

    fun initOperatorPad() : Unit{
        clear.setOnClickListener {
            clear()
        }

        plus_minus.setOnClickListener{
            doPlusMinus()
        }

        percentage.setOnClickListener{
            doPercentage()
        }

        divide.setOnClickListener{
            putOperator("/")
        }

        multiply.setOnClickListener{
            putOperator("x")
        }

        minus.setOnClickListener{
            putOperator("-")
        }

        plus.setOnClickListener{
            putOperator("+")
        }

        answer.setOnClickListener{
            doAnswer()
        }

        equal.setOnClickListener{
            doCalculateResult()
        }
    }

    fun putNumber(number : String){

        if(divideByZeroFlag){
            setDefault()
        }

        for (oper in operatorList){
            if(currentHistory.contains(oper)){
                operatorFlag = true
            }
        }

        if( currentResult.length < 10)
        {
            currentResult += number
            result.text = currentResult
        }
        else{
            toast("Number limit reached")
        }
    }

    fun putDot(dot: String){

        if(divideByZeroFlag){
            setDefault()
        }
        if(!currentResult.contains(dot))
        {
            currentResult += dot
            result.text = currentResult
        }
    }

    fun doAnswer(){
        if(ans != null){
            currentResult = ans.toString()
            result.text = currentResult
        }
    }

    fun putOperator(operator : String){

        if(divideByZeroFlag){
            setDefault()
        }

        if(calculated){
            if(currentResult.isNotEmpty())
                firstOperand = currentResult.toDouble()

            currentOperator = operator
            currentHistory = "( $firstOperand $currentOperator"
            history.text = currentHistory
            clearResult()
        } else if(firstOperand != null || result.text.isNotEmpty()){

            if(!operatorFlag){

                currentOperator = operator

                if(currentHistory.isEmpty()){
                    firstOperand = currentResult.toDouble()
                    currentHistory = "( $firstOperand $currentOperator"
                    history.text = currentHistory
                }
                else{
                    currentHistory = "${currentHistory.dropLast(2)} $currentOperator"
                    history.text = currentHistory
                }
                clearResult()
            }
        }
        else{
            toast("Operands needed")
        }
    }

    fun doCalculateResult(){

        if( firstOperand != null && result.text.any()){
            secondOperand = currentResult.toDouble()
            currentHistory = "$currentHistory $secondOperand ) ="
            history.text = currentHistory

            ans = calculate(currentOperator)?.toBigDecimal()?.setScale(2, RoundingMode.CEILING)?.toDouble()

            if(ans == null){
                currentResult = "Cannot divide by zero"
            }else{
                currentResult = ans.toString()
            }

            result.text = currentResult

            calculated = true
            operatorFlag = false
            firstOperand = null
            secondOperand = null
        }
    }

    fun calculate(oper : String) : Double?{

        var res : Double? = null

        when(oper){
            "/" -> {
                if(secondOperand == 0.0){
                    result.text = "Division By Zero"
                    divideByZeroFlag = true
                }else{
                    res = firstOperand!!.div(secondOperand!!)
                }
            }
            "x" -> res = firstOperand!! * secondOperand!!
            "+" -> res = firstOperand!!.plus(secondOperand!!)
            "-" -> res = firstOperand!!.minus(secondOperand!!)
            else -> res = null
        }

        return res
    }

    fun doPlusMinus(){

        if(divideByZeroFlag){
            setDefault()
        }

        if(result.text.any()){
            var res : Double = result.text.toString().toDouble()
            currentResult = (0 - res).toString()
            result.text = currentResult
        }
    }

    fun doPercentage(){

        if(divideByZeroFlag){
            setDefault()
        }

        if(result.text.any()){
            var res : Double = result.text.toString().toDouble()
            currentResult = (res / 100.0).toString()
            result.text = currentResult
        }
    }

    fun clear(){
        setDefault()
        result.text = currentResult
        history.text = currentHistory
    }

    fun clearResult(){
        currentResult = ""
        result.text = currentResult
    }

    fun setDefault(){

        currentHistory = ""
        currentResult = ""
        currentOperator = ""
        result.text = ""

        firstOperand = null
        secondOperand = null

        operatorFlag = false
        operandFlag = false
        calculated = false
        divideByZeroFlag = false
    }

    fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
