package easv.oe.dicecup2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MatchHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matchhistory)

        val q = intent.extras?.getString("history").toString()
        Log.d("HISTORY", q)
    }
}