package com.example.qalqulator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import org.mariuszgromada.math.mxparser.Expression
import java.text.DecimalFormat


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchTheme: Switch
    private lateinit var input: TextView
    private lateinit var output: TextView
    private lateinit var clear: ImageButton
    private lateinit var brackets: ImageButton
    private lateinit var percent: ImageButton
    private lateinit var divide: ImageButton
    private lateinit var num1: ImageButton
    private lateinit var num2: ImageButton
    private lateinit var num3: ImageButton
    private lateinit var num4: ImageButton
    private lateinit var num5: ImageButton
    private lateinit var num6: ImageButton
    private lateinit var num7: ImageButton
    private lateinit var num8: ImageButton
    private lateinit var num9: ImageButton
    private lateinit var num0: ImageButton
    private lateinit var multi: ImageButton
    private lateinit var minus: ImageButton
    private lateinit var plus: ImageButton
    private lateinit var negative: ImageButton
    private lateinit var point: ImageButton
    private lateinit var equals: ImageButton
    private lateinit var delete: ImageButton


    private fun initComponent() {
        switchTheme = findViewById(R.id.switchTheme)
        delete = findViewById(R.id.delete)
        input = findViewById(R.id.calculate)
        output = findViewById(R.id.sum)
        clear = findViewById(R.id.clear)
        brackets = findViewById(R.id.brackets)
        percent = findViewById(R.id.percent)
        divide = findViewById(R.id.divide)
        num7 = findViewById(R.id.num7)
        num8 = findViewById(R.id.num8)
        num9 = findViewById(R.id.num9)
        multi = findViewById(R.id.multi)
        num4 = findViewById(R.id.num4)
        num5 = findViewById(R.id.num5)
        num6 = findViewById(R.id.num6)
        minus = findViewById(R.id.minus)
        num1 = findViewById(R.id.num1)
        num2 = findViewById(R.id.num2)
        num3 = findViewById(R.id.num3)
        plus = findViewById(R.id.plus)
        negative = findViewById(R.id.negative)
        num0 = findViewById(R.id.nol)
        point = findViewById(R.id.point)
        equals = findViewById(R.id.equals)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        initComponent()

        switchTheme.isChecked = isDarkMode

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putBoolean("DARK_MODE", true).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putBoolean("DARK_MODE", false).apply()
            }
            finish()
            startActivity(Intent(this, javaClass))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


        delete.setOnClickListener {
            if (input.text.isNotEmpty()) {
                input.text = input.text.substring(0, input.text.length - 1)
            }
        }

        clear.setOnClickListener {
            input.text = ""
            output.text = ""
        }
        brackets.setOnClickListener {
            toggleBrackets()
        }
        num0.setOnClickListener {
            input.text = addToInputText("0")
        }
        num1.setOnClickListener {
            input.text = addToInputText("1")
        }
        num2.setOnClickListener {
            input.text = addToInputText("2")
        }
        num3.setOnClickListener {
            input.text = addToInputText("3")
        }
        num4.setOnClickListener {
            input.text = addToInputText("4")
        }
        num5.setOnClickListener {
            input.text = addToInputText("5")
        }
        num6.setOnClickListener {
            input.text = addToInputText("6")
        }
        num7.setOnClickListener {
            input.text = addToInputText("7")
        }
        num8.setOnClickListener {
            input.text = addToInputText("8")
        }
        num9.setOnClickListener {
            input.text = addToInputText("9")
        }
        percent.setOnClickListener {
            percent()
        }
        divide.setOnClickListener {
            divide()
        }
        multi.setOnClickListener {
            multiply()
        }
        plus.setOnClickListener {
            addition()
        }
        minus.setOnClickListener {
            subtraction()
        }
        negative.setOnClickListener {
            toggleNegative()
        }
        point.setOnClickListener {
            input.text = addToInputText(".")
        }
        equals.setOnClickListener {
            showResult()
        }
    }


    private fun addToInputText(imageButtonValue: String): String {
        return "${input.text}${imageButtonValue}"
    }

    private fun getInputExpression(): String {
        var expression = input.text.replace(Regex("÷"), "/")
        expression = expression.replace(Regex("×"), "*")
        return expression
    }

    private fun toggleBrackets() {

        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()
        val openBrackets = inputText.count { it == '(' }
        val closeBrackets = inputText.count { it == ')' }

        when {
            lastChar == null -> input.text = addToInputText("(")
            lastChar in listOf('+', '-', '×', '÷') -> {
                input.text = addToInputText("(")
            }

            openBrackets == closeBrackets -> input.text = addToInputText("×(")
            lastChar == '(' -> input.text = addToInputText("(")
            lastChar == ')' -> input.text = addToInputText("×(")
            lastChar.isDigit() -> input.text = addToInputText(")")
            else -> input.text = addToInputText(")")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mathSign(sign: Char) {
        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()

        if (lastChar != null && lastChar in listOf('+', '-', '×', '÷')) {
            // Remove the last character if it's a different Sign
            input.text = inputText.dropLast(1) + sign
        } else {
            // Add the new Sign if last character is not a math Sign
            input.text = addToInputText(sign.toString())
        }
    }


    @SuppressLint("SetTextI18n")
    private fun toggleNegative() {
        if (input.text.isNotEmpty()) {
            if (input.text.startsWith("(-")) {
                input.text = input.text.substring(2, input.text.length - 1)
            } else {
                input.text = "(-${input.text})"
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showResult() {
        try {
            val inputText = input.text.toString()
            val lastChar = inputText.lastOrNull()
            val expression = getInputExpression()
            val result = Expression(expression).calculate()
            if (lastChar == null) {
                Toast.makeText(this, "Input expression.", Toast.LENGTH_SHORT).show()
            } else if (result.isNaN()) {
                output.text = "Error"
                output.setTextColor(Color.parseColor("#9e1522"))
            } else {
                output.text = DecimalFormat("0.######").format(result).toString()
                output.setTextColor(Color.parseColor("#208A9F"))
            }
        } catch (e: Exception) {
            output.text = "Error"
            output.setTextColor(Color.parseColor("#9e1522"))
        }
    }

    private fun percent() {
        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()

        Log.d("Debug", "InputText: $inputText, LastChar: $lastChar")
        when {
            lastChar == null || lastChar in listOf('(', '-', '+', '×', '÷') -> {
                Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
            }

            lastChar.isDigit() -> {
                mathSign('%')
            }

            else -> {
                mathSign('%')
            }
        }
    }

    private fun divide() {

        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()
        when {
            lastChar == null || lastChar == '(' -> {
                Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
            }

            lastChar.isDigit() -> {
                mathSign('÷')
            }

            else -> {
                mathSign('÷')
            }
        }
    }

    private fun multiply() {
        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()
        when {
            lastChar == null || lastChar == '(' -> {
                Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
            }

            lastChar.isDigit() -> {
                mathSign('×')
            }

            else -> {
                mathSign('×')
            }
        }
    }

    private fun addition() {
        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()

        if (lastChar == null) {
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
        } else {
            mathSign('+')
        }
    }

    private fun subtraction() {
        val inputText = input.text.toString()
        val lastChar = inputText.lastOrNull()

        if (lastChar == null) {
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
        } else {
            mathSign('-')
        }
    }
}