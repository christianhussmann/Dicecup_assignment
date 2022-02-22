package easv.oe.dicecup2

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val HISTORY_NAME = "history"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRoll.setOnClickListener { v -> onClickRoll() }
        btnClear.setOnClickListener { v -> onClickClear() }
        Log.d(TAG, "OnCreate")

        //<editor-fold desc="Restore history">
        val orientation = this.getResources().getConfiguration().orientation
        val message = if (orientation == Configuration.ORIENTATION_PORTRAIT) "Portrait" else "Landscape"
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()

        if (savedInstanceState != null)
        {
            Log.d(TAG, "saved state NOT null")
            val history = savedInstanceState.getSerializable(HISTORY_NAME) as Array<Triple<Int,Int,Int>>
            history.forEach { p -> mHistory.add(p) }
            updateHistory()
            if (mHistory.size > 0)
                updateDicesWith(mHistory[mHistory.size-1])
        }
        //</editor-fold>
    }

    private fun onClickRoll(){
        val e1 = mRandomGenerator.nextInt(6) + 1
        val e2 = mRandomGenerator.nextInt(6) + 1
        val e3 = mRandomGenerator.nextInt(6) + 1

        val p = Triple(e1,e2,e3)
        //update history
        mHistory.add(p)

        // set dices
        updateDicesWith(p)
        if (mHistory.size > 5) mHistory.removeAt(0)
        updateHistory()
        Log.d(TAG, "Roll")
    }

    private fun onClickClear() {
        Log.d(TAG, "Clear")
        mHistory.clear()
        updateHistory()
    }

    // ensures that the history text aligns the history object
    private fun updateHistory() {
        var s = ""
        mHistory.forEach { p ->  val e1 = p.first; val e2 = p.second;val e3 = p.third; s += "$e1 - $e2 - $e3 \n" }
        tvHistory.text = s
    }

    private fun updateDicesWith(p: Triple<Int, Int, Int>) {
        imgDice1.setImageResource( diceId[p.first] )
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

}