package ru.yandex.dunaev.mick.musicamp.models

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.yandex.dunaev.mick.musicamp.repository.Repository

class MainViewModel: ViewModel(){
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val musicList: List<MusicSong>
    val currentPlay = ObservableField<MusicSong>()

    val isPlayer = ObservableBoolean(false)
    val isCyclic = ObservableBoolean(true)
    val isShuffle = ObservableBoolean(false)
    val isEndPosition = ObservableBoolean(false)
    val isStartPosition = ObservableBoolean(false)

    private var position = -1

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

    fun onClickMusicSong(pos: Int){
        Log.v("MVM on click","position = $position")
        musicList[pos].apply {
            if(playState()) {
                onStopSong(this)
                position = -1
            }
            else {
                onPlaySong(this)
                position = pos
            }
        }
        testPosition()
    }

    fun onPlaySong(ms: MusicSong){
        currentPlay.get()?.modeStop()
        ms.modePlay()
        currentPlay.set(ms)
        isPlayer.set(true)

    }

    fun onStopSong(ms: MusicSong){
        ms.modeStop()
        currentPlay.set(null)
        isPlayer.set(false)
    }

    fun onStopCurrentSong(){
        currentPlay.get()?: return
        onStopSong(currentPlay.get()!!)
    }

    fun onNext(){
        if(position == -1) return
        if(position == musicList.size - 1) return
        if(isCyclic.get()) position++
        onPlaySong(musicList[position])
        testPosition()
    }

    fun onPrev(){
        if(position == -1) return
        if(position == 0) return
        if(isCyclic.get()) position--
        onPlaySong(musicList[position])
        testPosition()
    }

    private fun testPosition(){
        isStartPosition.set(position == 0)
        isEndPosition.set(position == musicList.size - 1)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}