package com.music.player.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.music.player.domain.use_case.startingMusicList.viewModel.PlayListModel

@Database(entities = [PlayListModel::class], version = 1, exportSchema = false)
abstract class PlayListRoomdatabase():RoomDatabase() {
    abstract fun PlayListMusic():PlayListDao

    companion object {
        @Volatile
        private var INSTANCE: PlayListRoomdatabase? = null
        fun getInstance(context: Context): PlayListRoomdatabase{
            val temp= INSTANCE
            synchronized(this) {
                var instance = INSTANCE
                if(temp !=null){
                    return temp
                }
                synchronized(this){
                    val instance= Room.databaseBuilder(
                        context.applicationContext,
                        PlayListRoomdatabase::class.java,
                        "PlayList_Database"
                    ).build()

                    INSTANCE=instance
                    return instance
                }
            }
        }
    }
}







