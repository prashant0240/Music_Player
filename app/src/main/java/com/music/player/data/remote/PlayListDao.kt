package com.music.player.data.remote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayList(savingListModel: PlayListModel)

    @Delete
    fun DeletePlayList(savingListModel: PlayListModel)

    @Query("SELECT * FROM PlayList")
    fun getSavingList():LiveData<List<PlayListModel>>
}


