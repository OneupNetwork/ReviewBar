package com.baha.reviewbar.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import com.baha.reviewbar.IReviewBarListener
import com.baha.reviewbar.ReviewBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val reviewBar = findViewById<ReviewBar>(R.id.reviewBarOne)
        val scoreText = findViewById<AppCompatTextView>(R.id.score)
        reviewBar.setListener(object :IReviewBarListener{
            override fun scoreChange(score: Float) {
                scoreText.text = score.toString()
            }
        })
    }
}