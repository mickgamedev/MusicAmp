package ru.yandex.dunaev.mick.musicamp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import ru.yandex.dunaev.mick.musicamp.R
import ru.yandex.dunaev.mick.musicamp.databinding.FragmentPlayerSongBinding
import ru.yandex.dunaev.mick.musicamp.models.MainViewModel

class PlayerSongFragment : Fragment() {

    private lateinit var binding: FragmentPlayerSongBinding
    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_player_song,container,false)
        model = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        binding.viewModel = model
        return binding.root
    }


}
