package ru.yandex.dunaev.mick.musicamp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ru.yandex.dunaev.mick.musicamp.adapters.MusicListAdapter
import ru.yandex.dunaev.mick.musicamp.R
import ru.yandex.dunaev.mick.musicamp.databinding.FragmentPlayerListBinding
import ru.yandex.dunaev.mick.musicamp.models.MainViewModel

class PlayerListFragment : Fragment() {

    private lateinit var binding: FragmentPlayerListBinding
    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_player_list, container, false)
        model = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        binding.viewModel = model

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MusicListAdapter().apply {
                onItemClick = {v -> model.onClickMusicSong(v)}
            }
        }

        return binding.root
    }
}


