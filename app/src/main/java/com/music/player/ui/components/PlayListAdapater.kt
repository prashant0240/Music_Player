package com.music.player.ui.components

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel
import com.music.player.ui.musicPlayer.MusicPlayer

class PlayListAdapater(private val list: List<PlayListModel>) : RecyclerView.Adapter< PlayListAdapater.PlaylistViewHolder>() {
    private lateinit var mContext: Context
    private var filteredMusicList: List<PlayListModel> = list

    class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val musictitlename=view.findViewById<TextView>(R.id.songsPlayList)
        val musicartist=view.findViewById<TextView>(R.id.songArtist)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        mContext=parent.context
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.playlistitem, parent, false))
    }

    override fun getItemCount(): Int {
        return filteredMusicList.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val musiclistname=filteredMusicList[position]
        holder.musictitlename.text=musiclistname.PlayListtitle
        holder.musicartist.text=musiclistname.PlayListartist
        holder.itemView.setOnClickListener {
            val intent= Intent(mContext, MusicPlayer::class.java)
            intent.putExtra("Music_list",musiclistname)
            mContext.startActivity(intent)
        }

    }
}