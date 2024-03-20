package com.music.player.domain.use_case.startingMusicList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel
import com.music.player.data.remote.PlayListRoomdatabase
import com.music.player.data.repository.MainRepository

class WelcomeFatchViewModel(application: Application):AndroidViewModel(application) {
    private lateinit var repository: MainRepository

    init {
        val database=PlayListRoomdatabase.getInstance(application.applicationContext)
        val dao=database.PlayListMusic()
        repository= MainRepository(dao)

    }

    fun insertPlayList(savingListModel: PlayListModel){
        repository.insertPlayList(savingListModel)
    }
    fun deletePlayList(savingListModel: PlayListModel){
        repository.deletePlayList(savingListModel)
    }

    fun getfavourites(): LiveData<List<PlayListModel>> {
        return repository.getPlayList()
    }



}