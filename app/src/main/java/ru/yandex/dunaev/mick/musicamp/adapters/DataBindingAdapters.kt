package ru.yandex.dunaev.mick.musicamp.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.dunaev.mick.musicamp.models.MusicSong

@BindingAdapter("visibility")
fun View.SetVisibitily(b: Boolean){
    visibility = if(b) View.VISIBLE else View.GONE
}

@BindingAdapter("music_list")
fun RecyclerView.setLibrary(lib: List<MusicSong>?){
    lib?: return
    adapter?: return
    (adapter as MusicListAdapter).setItems(lib)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("duration")
fun TextView.convertDuration(st: String?){
    st?: return
    val duration = st.toLong() / 1000
    val minutes = duration / 60
    val seconds = duration % 60

    text = "%d:%02d".format(minutes, seconds)
}

@BindingAdapter("font_from_assets")
fun TextView.fontFromAssets(path: String) {
    setTypeface(Typeface.createFromAsset(context.assets, path))
}

@BindingAdapter("src")
fun ImageView.setBitmap(bitmap: Bitmap?){
    bitmap?: let {
        //visibility = View.GONE
        return
    }
    setImageBitmap(bitmap)
}