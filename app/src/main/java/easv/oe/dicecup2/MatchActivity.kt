package easv.oe.dicecup2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_matchactivity.*

class MatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matchactivity)

        val h = intent.extras?.getString("history")
        matchHistory.text = h.toString()
    }

    fun onClickCancel(view: View) {
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onClickClearHistory(view: View) {
        Log.d("CLEAR", "Clear")
        matchHistory.text = ""
        val intent = Intent()
        intent.putExtra("cleared", true)
        setResult(RESULT_OK, intent)
    }
}