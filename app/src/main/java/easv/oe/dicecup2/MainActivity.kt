package easv.oe.dicecup2

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_matchactivity.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.tvHistory
import java.util.*

class MainActivity : AppCompatActivity() {

    private val HISTORY_NAME = "history"

    private val REQUEST_CODE_ANSWER = 12
    private val TAG: String = "xyz"

    // mapping from 1..6 to drawables, the first index is unused
    private val diceId = intArrayOf(0, R.drawable.dice1,
        R.drawable.dice2,
        R.drawable.dice3,
        R.drawable.dice4,
        R.drawable.dice5,
        R.drawable.dice6)

    private val mRandomGenerator = Random()

    private val mHistory = mutableListOf<Triple<Int, Int, Int>>()

    private val mT = Texts()

    private val dicesNumber = mutableListOf<Int>();

    var dices: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRoll.setOnClickListener { v -> onClickRoll() }
        btnHistory.setOnClickListener{ v -> onClickSwitch()}
        Log.d(TAG, "OnCreate")

        val spinner = findViewById<Spinner>(R.id.spinner1)
        val numberOfDices = arrayOf("1", "2", "3", "4", "5", "6");

        val arrayAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, numberOfDices);
        spinner.adapter = arrayAdapter;

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@MainActivity, "You have selected ${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                dicesNumber.clear();
                dices = adapterView?.getItemAtPosition(position).toString().toInt();
            }

        }

        //<editor-fold desc="Restore history">
        val orientation = this.getResources().getConfiguration().orientation
        val message = if (orientation == Configuration.ORIENTATION_PORTRAIT) "Portrait" else "Landscape"
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()

        if (savedInstanceState != null)
        {
            Log.d(TAG, "saved state NOT null")
            val history = savedInstanceState.getSerializable(HISTORY_NAME) as Array<Triple<Int,Int,Int>>
            history.forEach { p -> mHistory.add(p) }
            if (mHistory.size > 0)
                updateDicesWith(mHistory[mHistory.size-1])
        }
        //</editor-fold>
    }

    private fun onClickSwitch() {
        val i = Intent(this, MatchActivity::class.java)
        i.putExtra("history", getHistory())
        startActivityForResult(i, REQUEST_CODE_ANSWER)
    }


    private fun onClickRoll(){
        dicesNumber.clear();

        val e1 = mRandomGenerator.nextInt(6) + 1
        val e2 = mRandomGenerator.nextInt(6) + 1
        val e3 = mRandomGenerator.nextInt(6) + 1
        val e4 = mRandomGenerator.nextInt(6) + 1
        val e5 = mRandomGenerator.nextInt(6) + 1
        val e6 = mRandomGenerator.nextInt(6) + 1

        when (dices) {
            1 -> dicesNumber.add(e1);
            2 -> dicesNumber.addAll(listOf(e1, e2));
            3 -> dicesNumber.addAll(listOf(e1, e2, e3));
            4 -> dicesNumber.addAll(listOf(e1, e2, e3, e4));
            5 -> dicesNumber.addAll(listOf(e1, e2, e3, e4, e5));
            6 -> dicesNumber.addAll(listOf(e1, e2, e3, e4, e5, e6));
        }

        val p = Triple(e1,e2,e3)
        //update history
        mHistory.add(p)

        // set dices
        updateDicesWith(p)
        if (mHistory.size > 5) mHistory.removeAt(0)
        Log.d(TAG, "Roll")
    }


    // ensures that the history text aligns the history object
    private fun getHistory(): String {
        var s = ""
        mHistory.forEach { p ->  val e1 = p.first; val e2 = p.second;val e3 = p.third; s += "$e1 - $e2 - $e3 \n" }
        return s
    }

    private fun updateDicesWith(p: Triple<Int, Int, Int>) {
        when (dices) {
            1 -> imgDice1.setImageResource(diceId[dicesNumber.first()]);
        }
        //imgDice1.setImageResource( diceId[p.first] )
        imgDice2.setImageResource( diceId[p.second] )
        imgDice3.setImageResource( diceId[p.third] )
    }

    //<editor-fold desc="onSaveInstanceState">
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "History saved")

        outState.putSerializable(HISTORY_NAME, mHistory.toTypedArray())
    }
    //</editor-fold>

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val clearPressed = data?.extras?.getBoolean("cleared")
        if (clearPressed == true) {
            mHistory.clear()
        }
    }
}