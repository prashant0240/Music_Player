package com.music.player.ui.components

import android.annotation.SuppressLint
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
import java.util.Locale

class SongFromDeviceAdapter(private val musicSongFromDevice: List<PlayListModel>) : RecyclerView.Adapter<SongFromDeviceAdapter.ViewHolder>() {
    private var filteredMusicList: List<PlayListModel> = musicSongFromDevice
    private lateinit var mcontext:Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.songTitle)
        val musicartist: TextView = itemView.findViewById(R.id.songArtist)
        val duration: TextView = itemView.findViewById(R.id.songDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.musicfromdevice, parent, false)
        mcontext=parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = filteredMusicList[position]
        holder.titleTextView.text = track.PlayListtitle
        val minutes=track.PlayListduration/1000/60
        val seconds=(track.PlayListduration/1000)%60
        val currenttime=String.format("%02d:%02d",minutes,seconds)
        holder.duration.text=currenttime
        holder.musicartist.text=track.PlayListartist
        holder.itemView.setOnClickListener {
            val  intent=Intent(mcontext,MusicPlayer::class.java)
            intent.putExtra("Music_list",track)
            mcontext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredMusicList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun musicListFilter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredMusicList = if (query.isBlank()) {
            musicSongFromDevice
        } else {
            musicSongFromDevice.filter{ musicItem ->
                musicItem.PlayListtitle.lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        musicItem.PlayListartist.lowercase(Locale.getDefault())
                            .contains(lowerCaseQuery)
            }
        }
        notifyDataSetChanged()
    }
    fun milliSecondsFormater(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "$minutes:$seconds"
    }

}