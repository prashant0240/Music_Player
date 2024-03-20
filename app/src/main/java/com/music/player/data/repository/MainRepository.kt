package com.music.player.data.repository

import androidx.lifecycle.LiveData
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel
import com.music.player.data.remote.PlayListDao

class MainRepository (private val dao: PlayListDao) {
   fun insertPlayList(savingListModel: PlayListModel) {
        dao.insertPlayList(savingListModel)
    }

   fun deletePlayList(savingListeModel: PlayListModel) {
        dao.DeletePlayList(savingListeModel)
    }
    fun getPlayList(): LiveData<List<PlayListModel>> {
        return dao.getSavingList()
    }
}