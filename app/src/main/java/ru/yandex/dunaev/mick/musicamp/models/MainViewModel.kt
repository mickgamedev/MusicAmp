package ru.yandex.dunaev.mick.musicamp.models

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.yandex.dunaev.mick.musicamp.repository.Repository

class MainViewModel: ViewModel(){
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val musicList: List<MusicSong>
    val currentPlay = ObservableField<MusicSong>()

    init {
        Repository.createMusicList()
        musicList = Repository.getMusicList()
        uiScope.launch{
            Repository.getMusicList().forEach{
                it.extractMetaData()
                //delay(1000)
            }
        }
        Log.v("MVM init", "MainViewModel init complete")
    }

    fun onClickMusicSong(position: Int){
        Log.v("MVM on click","position = $position")
        musicList[position].apply {
            if(playState()) {
                modeStop()
                currentPlay.set(null)
            }
            else {
                currentPlay.get()?.modeStop()
                modePlay()
                currentPlay.set(this)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}