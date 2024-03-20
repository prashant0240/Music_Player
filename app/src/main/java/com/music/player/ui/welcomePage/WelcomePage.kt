@file:Suppress("DEPRECATION")

package com.music.player.ui.welcomePage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.music.player.R
import com.music.player.common.MusicFromDevice
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel
//import com.music.player.presentation.components.MusicAdapater
import com.music.player.ui.components.SongFromDeviceAdapter
import com.music.player.ui.savedPlayList.SavedPlayList

//Activity for  Welcome Page
private const val STORAGE_PERMISSION_CODE = 1001
@Suppress("KotlinConstantConditions")
class WelcomePage : AppCompatActivity() {
    private lateinit var SongFromDevicedapter: SongFromDeviceAdapter
    private lateinit var songFromDevice: List<PlayListModel>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_page)

        supportActionBar?.title="Library"
        supportActionBar?.setIcon(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.baseline_menu_24))

        if (storagePermission()) {
            getSongfromdevice()
        } else {
            requestPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_welcomepage,menu)
        var menuItem=menu!!.findItem(R.id.searchSongItem)
        var searchview:SearchView=menuItem.actionView as SearchView
        val searchIcon =
            searchview.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)
        searchview.maxWidth= Int.MAX_VALUE
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                SongFromDevicedapter.musicListFilter(newText.toString())
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun storagePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favoriteSong->{
                val intent=Intent(this,SavedPlayList::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongfromdevice()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access audio files without storage permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getSongfromdevice() {
        songFromDevice = MusicFromDevice(this)
        val recyclerViewMusic = findViewById<RecyclerView>(R.id.musicRecyclerView)
        SongFromDevicedapter = SongFromDeviceAdapter(songFromDevice)
        recyclerViewMusic.adapter = SongFromDevicedapter
        recyclerViewMusic.layoutManager = LinearLayoutManager(this)

    }

}




