package com.music.player.domain.use_case.startingMusicList.viewModel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "PlayList")
data class PlayListModel(
    @PrimaryKey(autoGenerate = true)
    val PlayListId :Long,
    val PlayListtitle: String,
    val PlayListartist:String,
    val PlayListduration: Long,
    val PlayListfilepath:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readLong(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(PlayListId)
        parcel.writeString(PlayListtitle)
        parcel.writeString(PlayListartist)
        parcel.writeLong(PlayListduration)
        parcel.writeString(PlayListfilepath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayListModel> {
        override fun createFromParcel(parcel: Parcel): PlayListModel {
            return PlayListModel(parcel)
        }

        override fun newArray(size: Int): Array<PlayListModel?> {
            return arrayOfNulls(size)
        }
    }
}