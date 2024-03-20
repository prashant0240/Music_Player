@file:OptIn(DelicateCoroutinesApi::class)

package com.music.player.ui.savedPlayList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.domain.use_case.startingMusicList.WelcomeFatchViewModel
import com.music.player.ui.components.PlayListAdapater
import kotlinx.coroutines.DelicateCoroutinesApi


class SavedPlayList : AppCompatActivity() {
    private lateinit var viewmodel:WelcomeFatchViewModel
    private lateinit var noItemsTextView:TextView
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saved_playlist)

    supportActionBar?.title="PlayLists"
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.baseline_menu_24))

    viewmodel= ViewModelProvider(this).get(WelcomeFatchViewModel::class.java)
            viewmodel.getfavourites().observe(this, Observer {
                recyclerView = findViewById<RecyclerView>(R.id.songsRecyclerView)
                val adapater = PlayListAdapater(it)
                recyclerView.adapter = adapater
            })
            noItemsTextView = findViewById(R.id.noItemsText)
        musicList()
    }

    private fun musicList() {
        recyclerView = findViewById<RecyclerView>(R.id.songsRecyclerView)
        viewmodel= ViewModelProvider(this).get(WelcomeFatchViewModel::class.java)
        viewmodel.getfavourites().observe(this, Observer {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
                noItemsTextView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        })
   }
}

