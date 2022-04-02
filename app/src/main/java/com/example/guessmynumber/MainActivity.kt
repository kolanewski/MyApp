package com.example.guessmynumber

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.guessmynumber.ui.ranking.RankingActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var pref : SharedPreferences
    lateinit var prefEdit : SharedPreferences.Editor

    lateinit var input: EditText
    lateinit var button: Button
    lateinit var ranking: Button
    lateinit var new: Button
    lateinit var attemptText: TextView
    lateinit var pointsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getPreferences(Context.MODE_PRIVATE)
        prefEdit = pref.edit()


        input = findViewById(R.id.input)
        button = findViewById(R.id.button)
        ranking = findViewById(R.id.rankingButton)
        new = findViewById(R.id.newButton)
        attemptText = findViewById(R.id.attemptText)
        pointsText = findViewById(R.id.pointsText)

        newGame()
        button.setOnClickListener {
            checkNumber()
        }
        ranking.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            startActivity(intent)
        }
        new.setOnClickListener {
            gameOver()
        }
    }

    private fun checkNumber(){
        val errorCheck = input.text.toString()
        if(errorCheck=="" || errorCheck.toInt()>20 || errorCheck.toInt()<0){
            Toast.makeText(applicationContext, ERROR, Toast.LENGTH_SHORT).show()
            return
        }
        val guess = errorCheck.toInt()
        if(guess>pref.getInt(TO_GUESS, DEF)){
            Toast.makeText(applicationContext, BIG, Toast.LENGTH_SHORT).show()
            saveState()
        }else if(guess<pref.getInt(TO_GUESS, DEF)){
            Toast.makeText(applicationContext, SMALL, Toast.LENGTH_SHORT).show()
            saveState()
        }else{
            val attempt = pref.getInt(ATTEMPTS, DEF)
            val points = points(attempt)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Zgadłeś!")
            builder.setMessage("Udało Ci się trafić za " + attempt.toString() + " razem! Zdobywasz " + points.toString() + " punktów.")

            builder.setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()

            pointsText.text = "Punkty: "+(pref.getInt(POINTS, DEF)+points).toString()
            prefEdit.putInt(POINTS, pref.getInt(POINTS, DEF)+points)
            prefEdit.apply()
            if(pref.getInt(POINTS, DEF)>pref.getInt(RECORD, DEF)){
                prefEdit.putInt(POINTS, pref.getInt(POINTS, DEF))
                prefEdit.apply()
            }

            newGame()
        }

    }

    private fun saveState(){
        prefEdit.putInt(ATTEMPTS, pref.getInt(ATTEMPTS, DEF)+1)
        attemptText.text = "Próby: "+ pref.getInt(ATTEMPTS, DEF)
        prefEdit.apply()
        if(pref.getInt(ATTEMPTS, DEF) > 10){
            val attempt = pref.getInt(ATTEMPTS, DEF)
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Koniec gry!")
            builder.setMessage(OVER)

            builder.setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()

            gameOver()
            return
        }
    }

    private fun points(at : Int) : Int{
        when {
            at<=1 -> {
                return 5
            }
            at in 2..4 -> {
                return 3
            }
            at in 5..6 -> {
                return 2
            }
            at in 7..10 -> {
                return 1
            }
            else -> return 0
        }
    }

    private fun resetPoints(){
        prefEdit.putInt(POINTS, 0)
        prefEdit.apply()
        pointsText.text = "Punkty: 0"
    }

    private fun newGame(){
        prefEdit.putInt(TO_GUESS, Random.nextInt(0,20))
        prefEdit.putInt(ATTEMPTS, 1)
        prefEdit.apply()
        attemptText.text = "Próby: 0"
        pointsText.text = "Punkty: "+ pref.getInt(POINTS, DEF).toString()
    }

    private fun gameOver(){
        prefEdit.putInt(POINTS, 0)
        prefEdit.putInt(ATTEMPTS, 1)
        prefEdit.putInt(TO_GUESS, Random.nextInt(0,20))
        prefEdit.apply()
        attemptText.text = "Próby: 0"
        pointsText.text = "Punkty: 0"
    }

    companion object{
        private val TO_GUESS = "toBeGuessed"
        private val ATTEMPTS = "attemptsDone"
        private val POINTS = "points"
        private val RECORD = "record"

        private val DEF = 420

        private val ERROR = "Najpierw wpisz liczbę z przedziału 0-20"
        private val SMALL = "Szukana liczba jest większa"
        private val BIG = "Szukana liczba jest mniejsza"
        private val OVER = "Przekroczyłeś 10 prób."
    }
}

