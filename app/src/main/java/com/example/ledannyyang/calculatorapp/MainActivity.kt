package com.example.ledannyyang.calculatorapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    val NULL_SAFE_VALUE : Double = 123456789.9
    var currentHistory : String = ""
    var currentResult : String = ""
    var currentOperator : String = ""

    var firstOperand: Double = NULL_SAFE_VALUE
    var secondOperand: Double = NULL_SAFE_VALUE

    var operatorFlag : Boolean = false
    var operandFlag : Boolean = false
    var calculated = false

    var ans : Double = 0.0

    val operatorList = listOf("+/-", "%", "/", "x", "-", "+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // hideNavigationBar()

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

        if(!currentResult.contains(dot))
        {
            currentResult += dot
            result.text = currentResult
        }
    }

    fun doAnswer(){
        currentResult = ans.toString()
        result.text = currentResult
    }

    fun putOperator(operator : String)
    {
        if(calculated){
            firstOperand = currentResult.toDouble()
            currentOperator = operator
            currentHistory = "( ${firstOperand} ${currentOperator}"
            history.text = currentHistory
            clearResult()
        }
        else if(firstOperand != NULL_SAFE_VALUE || result.text.length != 0){

            if(!operatorFlag){

                currentOperator = operator

                if(currentHistory.length == 0){
                    firstOperand = currentResult.toDouble()
                    currentHistory = "( ${firstOperand} ${currentOperator}"
                    history.text = currentHistory
                }
                else{
                    currentHistory = "${currentHistory.dropLast(2)} ${currentOperator}"
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

        if( firstOperand != NULL_SAFE_VALUE && result.text.any()){
            secondOperand = currentResult.toDouble()
            currentHistory = "${currentHistory} ${secondOperand} ) ="
            history.text = currentHistory

            ans = calculate(currentOperator).toBigDecimal().setScale(2, RoundingMode.CEILING).toDouble()
            currentResult = ans.toString()

            if(ans == NULL_SAFE_VALUE){
                result.text = "Error"
            }

            result.text = currentResult

            calculated = true
            operatorFlag = false
            firstOperand = NULL_SAFE_VALUE
            secondOperand = NULL_SAFE_VALUE
        }
    }

    fun calculate(oper : String) : Double{

        var res = 0.0
        when(oper){
            "/" -> res = firstOperand / secondOperand
            "x" -> res = firstOperand * secondOperand
            "+" -> res = firstOperand + secondOperand
            "-" -> res = firstOperand - secondOperand
            else -> res = NULL_SAFE_VALUE
        }
        return res
    }

    fun doPlusMinus(){

        if(result.text.any()){
            var res : Double = result.text.toString().toDouble()
            currentResult = (0 - res).toString()
            result.text = currentResult
        }
    }

    fun doPercentage(){

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

        firstOperand = NULL_SAFE_VALUE
        secondOperand = NULL_SAFE_VALUE

        operatorFlag = false
        operandFlag = false
        calculated = false
    }

    fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    fun hideNavigationBar(){
                window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
