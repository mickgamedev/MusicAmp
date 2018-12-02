package ru.yandex.dunaev.mick.musicamp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.florent37.runtimepermission.kotlin.askPermission
import ru.yandex.dunaev.mick.musicamp.models.MainViewModel
import ru.yandex.dunaev.mick.musicamp.R
import ru.yandex.dunaev.mick.musicamp.databinding.ActivityMainBinding
import ru.yandex.dunaev.mick.musicamp.util.toast
import ru.yandex.dunaev.mick.musicamp.AudioPlayerService
import android.content.Intent
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = model
        permissions()

        setFragment(PlayerListFragment(), R.id.listContainer)
        setFragment(PlayerSongFragment(), R.id.songContainer)

        val intent = Intent(this, AudioPlayerService::class.java)
        Util.startForegroundService(this, intent)
    }

    private fun setFragment(fragment: Fragment, container: Int){
        supportFragmentManager
            .beginTransaction()
            .replace(container, fragment)
            .commit()
    }

    private fun permissionGranted(){

    }

    private fun permissions(){
        askPermission{
            permissionGranted()
        }.onDeclined {
            toast("Permission required")
            finish()
        }
    }

}
