package ru.yandex.dunaev.mick.musicamp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

class MusicSong(val path: String, val filename: String) {
    val album = ObservableField<String>()
    val albumartist = ObservableField<String>()
    val artist = ObservableField<String>()
    val author = ObservableField<String>()
    val bitrate = ObservableField<String>()
    val cdTrackNumber = ObservableField<String>()
    val compilation = ObservableField<String>()
    val composer = ObservableField<String>()
    val date = ObservableField<String>()
    val discNumber = ObservableField<String>()
    val duration = ObservableField<String>()
    val genre = ObservableField<String>()
    val imageCount = ObservableField<String>()
    val imageHeight = ObservableField<String>()
    val imagePrimary = ObservableField<String>()
    val imageRotation = ObservableField<String>()
    val imageWidth = ObservableField<String>()
    val location = ObservableField<String>()
    val mimetype = ObservableField<String>()
    val numTracks = ObservableField<String>()
    val title = ObservableField<String>(filename)
    val writer = ObservableField<String>()
    val year = ObservableField<String>()

    val isPlay = ObservableBoolean(false)
    val cover = ObservableField<Bitmap>()

    val progressMax = ObservableInt(0)
    val progressCurrent = ObservableInt(0)

    suspend fun extractMetaData() {
        Log.v("ExtractMD", filename)
        MediaMetadataRetriever().apply {
            setDataSource(path)
            album.set(extractMetadata(METADATA_KEY_ALBUM))
            albumartist.set(extractMetadata(METADATA_KEY_ALBUMARTIST))
            artist.set(extractMetadata(METADATA_KEY_ARTIST))
            author.set(extractMetadata(METADATA_KEY_AUTHOR)?: albumartist.get())
            bitrate.set(extractMetadata(METADATA_KEY_BITRATE))
            cdTrackNumber.set(extractMetadata(METADATA_KEY_CD_TRACK_NUMBER))
            compilation.set(extractMetadata(METADATA_KEY_COMPILATION))
            composer.set(extractMetadata(METADATA_KEY_COMPOSER))
            date.set(extractMetadata(METADATA_KEY_DATE))
            discNumber.set(extractMetadata(METADATA_KEY_DISC_NUMBER))
            duration.set(extractMetadata(METADATA_KEY_DURATION))
            genre.set(extractMetadata(METADATA_KEY_GENRE))
            imageCount.set(extractMetadata(METADATA_KEY_IMAGE_COUNT))
            imageHeight.set(extractMetadata(METADATA_KEY_IMAGE_HEIGHT))
            imagePrimary.set(extractMetadata(METADATA_KEY_IMAGE_PRIMARY))
            imageRotation.set(extractMetadata(METADATA_KEY_IMAGE_ROTATION))
            imageWidth.set(extractMetadata(METADATA_KEY_IMAGE_WIDTH))
            location.set(extractMetadata(METADATA_KEY_LOCATION))
            mimetype.set(extractMetadata(METADATA_KEY_MIMETYPE))
            numTracks.set(extractMetadata(METADATA_KEY_NUM_TRACKS))
            title.set(extractMetadata(METADATA_KEY_TITLE)?: filename)
            writer.set(extractMetadata(METADATA_KEY_WRITER))
            year.set(extractMetadata(METADATA_KEY_YEAR))
            embeddedPicture?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                cover.set(bitmap)
            }
            duration.get()?.let{
                progressMax.set(it.toInt())
            }
            release()
        }
    }

    fun modePlay(){
        isPlay.set(true)
    }

    fun modeStop(){
        isPlay.set(false)
    }

    fun playState() = isPlay.get()
}