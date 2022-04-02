package com.example.guessmynumber.ui.ranking

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guessmynumber.R

class RankingActivity : AppCompatActivity() {

    lateinit var rankList: RecyclerView
    lateinit var adapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        rankList = findViewById(R.id.rankList)

        val users = arrayListOf<RankingPosition>()
        users.add(RankingPosition(1,"Piotr", 100))
        users.add(RankingPosition(2,"Adam", 90))
        users.add(RankingPosition(3,"Dominik", 80))
        users.add(RankingPosition(4,"Kuba", 70))

        rankList.layoutManager = LinearLayoutManager(this);
        adapter = RankingAdapter(users)
        rankList.adapter = adapter
    }
}