package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var canAddOperation=false
    private var canAddDecimal=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                {
                    findViewById<TextView>(R.id.workingsTV).append(view.text)
                    canAddDecimal=false
                }
            }
            else
            {
                findViewById<TextView>(R.id.workingsTV).append(view.text)
            }
            canAddOperation=true
        }
    }
    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            findViewById<TextView>(R.id.workingsTV).append(view.text)
            canAddOperation=false
            canAddDecimal=true
        }
    }
    fun allClearAction(view: View)
    {
        findViewById<TextView>(R.id.workingsTV).text=""

        findViewById<TextView>(R.id.resultsTV).text =""

    }
    fun backSpaceAction(view: View) {
        val lenght =  findViewById<TextView>(R.id.workingsTV).length()
        if(lenght > 0)
        {
            findViewById<TextView>(R.id.workingsTV).text= findViewById<TextView>(R.id.workingsTV).text.subSequence(0,lenght-1)
        }
    }


    fun equalsAction(view: View)
    {
        findViewById<TextView>(R.id.resultsTV).text= calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperator=digitsOperators()
        if(digitsOperator.isEmpty()) return ""

        val timesDivision=timesDivisionCalculate(digitsOperator)
        if(timesDivision.isEmpty()) return ""

        val result=addSubstractCalculate(timesDivision)


        val d=result.toDouble()
        if((d % 1) == 0.0){
            println("Is integer")
            return result.toInt().toString()
        }else{
            println("Is not integer")
            return result.toFloat().toString()
        }
       // return result.toString()
    }

    private fun addSubstractCalculate(passedlist: MutableList<Any>): Float
    {
        var result=passedlist[0] as Float

        for(i in passedlist.indices)
        {
            if(passedlist[i] is Char && i !=passedlist.lastIndex)
            {
                val operator = passedlist[i]
                val nextDigit= passedlist[i+1] as Float
                if(operator=='+')
                    result +=nextDigit
                if(operator=='-')
                    result -=nextDigit

            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedlist: MutableList<Any>): MutableList<Any>
    {
        var list =passedlist
        while(list.contains('x') || list.contains('/'))
        {
            list =calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedlist: MutableList<Any>): MutableList<Any>
    {
        val newlist= mutableListOf<Any>()
        var restartIndex =passedlist.size

        for(i in passedlist.indices)
        {
            if(passedlist[i] is Char && i != passedlist.lastIndex && i < restartIndex)
            {
                val operator = passedlist[i]
                val prevDigit = passedlist[i-1] as Float
                val nextDigit = passedlist[i+1] as Float
                when(operator)
                {
                    'x'->
                    {
                        newlist.add(prevDigit * nextDigit)
                        restartIndex=i+1
                    }
                    '/'->
                    {
                        newlist.add(prevDigit / nextDigit)
                        restartIndex=i+1
                    }
                    else ->
                    {
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }
            }
            if(i>restartIndex)
            {
                newlist.add(passedlist[i])
            }
        }
        return newlist
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit=""
        //var character :String
        for(character in  findViewById<TextView>(R.id.workingsTV).text )
        {
            if(character.isDigit() || character == '.')
            {
                currentDigit+= character

            }
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit=""
                list.add(character)
            }
        }
        if(currentDigit !="")
        {
            list.add(currentDigit.toFloat())
        }


        return list
    }

}