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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = model
        permissions()
        setFragment(PlayerListFragment())
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
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
