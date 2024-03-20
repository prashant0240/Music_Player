@file:Suppress("UNUSED_EXPRESSION")

package com.music.player.ui.musicPlayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.music.player.R
import com.music.player.common.MusicFromDevice
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel
import com.music.player.domain.use_case.startingMusicList.WelcomeFatchViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MusicPlayer : AppCompatActivity() {
    private lateinit var welcomeFatchViewModel:WelcomeFatchViewModel
    private var seletedMusic: PlayListModel?=null
    private lateinit var getMusicFromDevice:List<PlayListModel>
    private lateinit var onfarward: ImageView
    private lateinit var onrewind: ImageView
    private lateinit var onstart: ImageView
    private lateinit var onmediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var countdownTextView: TextView
    private var isFavorite: Boolean = false

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player)

        supportActionBar?.title="Music Player"

        val intent=intent
        seletedMusic=intent.getParcelableExtra("Music_list")

        onmediaPlayer = MediaPlayer()
        onmediaPlayer.setDataSource(seletedMusic?.PlayListfilepath.toString())
        onmediaPlayer.prepare()
        onmediaPlayer.start()



        getMusicFromDevice = MusicFromDevice(this)
        welcomeFatchViewModel=ViewModelProvider(this).get(WelcomeFatchViewModel::class.java)

        countdownTextView = findViewById(R.id.songDecreaseTime)
        val minutes=onmediaPlayer.duration/1000/60
        val seconds=(onmediaPlayer.duration/1000)%60
        val currenttime=String.format("%02d:%02d",minutes,seconds)
        findViewById<TextView>(R.id.songTotalTime).text=currenttime
        findViewById<TextView>(R.id.songTitle).text=seletedMusic?.PlayListtitle.toString()

        seekBar = findViewById(R.id.songSeekbar)
        seekBar.max =onmediaPlayer.duration

        onmediaPlayer.setOnCompletionListener {
            seekBar.progress = 0
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    onmediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        setupToolbar()
        playBackMusicOperator()

        welcomeFatchViewModel.getfavourites().observe(this, Observer {
            isFavorite=  it.any{
                it.PlayListtitle==seletedMusic?.PlayListtitle.toString() && it.PlayListartist==seletedMusic?.PlayListartist.toString()
            }
        })

    }

    private fun playBackMusicOperator() {
        onstart=findViewById<ImageView>(R.id.songPlay)
        onstart.setOnClickListener {
            if (onmediaPlayer.isPlaying){
                pausePlaybackMusic()
            }else{
                initiatePlaybackMusic()
            }
        }
        onrewind=findViewById<ImageView>(R.id.songRewind)
        onrewind.setOnClickListener {
            progressingBackward()
        }
        onfarward=findViewById<ImageView>(R.id.songForword)
        onfarward.setOnClickListener {
            progressingForward()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.title="Music Player"
    }

    private fun initiatePlaybackMusic() {
        onmediaPlayer.start()
        onstart.setImageResource(R.drawable.stop)
    }
    private fun progressingForward() {
        val currentSongIndex = getMusicFromDevice.indexOf(seletedMusic)
        if (currentSongIndex < getMusicFromDevice.size - 1) {
            val nextMusicItem = getMusicFromDevice[currentSongIndex + 1]
            onmediaPlayer.reset()
            onmediaPlayer.setDataSource(nextMusicItem.PlayListfilepath)
            onmediaPlayer.prepare()
            initiatePlaybackMusic()
            seletedMusic = nextMusicItem
            seekBar.progress=0
            val minutes=nextMusicItem.PlayListduration/1000/60
            val seconds=(nextMusicItem.PlayListduration/1000)%60
            val currenttime=String.format("%02d:%02d",nextMusicItem.PlayListduration/1000/60,(nextMusicItem.PlayListduration/1000)%60)
            findViewById<TextView>(R.id.songTotalTime).text=currenttime
            findViewById<TextView>(R.id.songTitle).text=nextMusicItem.PlayListtitle.toString()
        } else {
            Toast.makeText(this, "No more songs to play", Toast.LENGTH_SHORT).show()

        }
    }

    private fun pausePlaybackMusic() {
        onmediaPlayer.pause()
        onstart.setImageResource(R.drawable.play)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (onmediaPlayer != null){
            onmediaPlayer.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_musicplayer,menu)
        var fvarate=menu!!.findItem(R.id.favoriteSong)
        if (isFavorite) {
            fvarate.setIcon(R.drawable.favorite)
        }else{
            fvarate.setIcon(R.drawable.favorite_unfilled)
        }
        return super.onCreateOptionsMenu(menu)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favoriteSong->{
                val icon=if (isFavorite) R.drawable.favorite else R.drawable.favorite_unfilled
                item.setIcon(icon)
                true
                GlobalScope.launch {
                    if (isFavorite) {
                        welcomeFatchViewModel.deletePlayList(
                            PlayListModel(
                                seletedMusic?.PlayListId.toString().toLong(),
                                seletedMusic?.PlayListtitle.toString(),
                                seletedMusic?.PlayListartist.toString(),
                                seletedMusic?.PlayListduration.toString().toLong(),
                                seletedMusic?.PlayListfilepath.toString()
                            )
                        )
                    }else{
                        welcomeFatchViewModel.insertPlayList(
                            PlayListModel(
                                seletedMusic?.PlayListId.toString().toLong(),
                                seletedMusic?.PlayListtitle.toString(),
                                seletedMusic?.PlayListartist.toString(),
                                seletedMusic?.PlayListduration.toString().toLong(),
                                seletedMusic?.PlayListfilepath.toString()
                            )
                        )

                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun progressingBackward() {
        val currentSongIndex = getMusicFromDevice.indexOf(seletedMusic)
        if (currentSongIndex > 0) {
            val previousMusicItem = getMusicFromDevice[currentSongIndex - 1]
            onmediaPlayer.reset()
            onmediaPlayer.setDataSource(previousMusicItem.PlayListfilepath)
            onmediaPlayer.prepare()
            initiatePlaybackMusic()
            seletedMusic = previousMusicItem
            seekBar.progress=0
            val currenttime=String.format("%02d:%02d",previousMusicItem.PlayListduration/1000/60,(previousMusicItem.PlayListduration/1000)%60)
            findViewById<TextView>(R.id.songTotalTime).text=currenttime
            findViewById<TextView>(R.id.songTitle).text=previousMusicItem.PlayListtitle.toString()

        } else {
            Toast.makeText(this, "No previous songs", Toast.LENGTH_SHORT).show()
            
        }
    }
}