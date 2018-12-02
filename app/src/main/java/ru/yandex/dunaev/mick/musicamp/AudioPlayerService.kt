package ru.yandex.dunaev.mick.musicamp

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util.getUserAgent
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import ru.yandex.dunaev.mick.musicamp.repository.Repository
import java.io.File
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.Player
import android.graphics.Bitmap
import android.app.PendingIntent
import android.support.v4.media.MediaDescriptionCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

val PLAYBACK_CHANNEL_ID = "playback_channel"
val PLAYBACK_NOTIFICATION_ID = 1
val MEDIA_SESSION_TAG = "audio_demo"
val DOWNLOAD_CHANNEL_ID = "download_channel"
val DOWNLOAD_NOTIFICATION_ID = 2

class AudioPlayerService : Service() {

    private lateinit var player: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    override fun onCreate() {
        super.onCreate()
        val context = this

        player = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector())
        val dataSourceFactory = DefaultDataSourceFactory(
            context, Util.getUserAgent(context, getString(R.string.app_name))
        )
        val concatenatingMediaSource = ConcatenatingMediaSource()
        for (sample in Repository.getMusicList()) {
            val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.fromFile(File(sample.path)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        player.prepare(concatenatingMediaSource)
        player.setPlayWhenReady(true)

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            context,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            PLAYBACK_NOTIFICATION_ID,
            object : MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return Repository.getMusicList()[player.currentWindowIndex].title.get() ?: "not found"
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): String? {
                    return Repository.getMusicList()[player.currentWindowIndex].author.get() ?: "not found"
                }

                override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
                    return Repository.getMusicList()[player.currentWindowIndex].cover.get()
                }
            }
        )
        playerNotificationManager.setNotificationListener(object : NotificationListener {
            override fun onNotificationStarted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }
        })
        playerNotificationManager.setPlayer(player);

        mediaSession = MediaSessionCompat (context, MEDIA_SESSION_TAG)
        mediaSession.setActive(true)
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken())

        mediaSessionConnector = MediaSessionConnector (mediaSession)
        mediaSessionConnector.setQueueNavigator(object: TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(player: Player?, windowIndex: Int): MediaDescriptionCompat {
                val song = Repository.getMusicList()[windowIndex]
                return MediaDescriptionCompat.Builder()
                    .setMediaId(song.cdTrackNumber.get())
                    .setIconBitmap(song.cover.get())
                    .setTitle(song.title.get())
                    .setDescription(song.author.get())
                    .build()
                }
            })
        mediaSessionConnector.setPlayer(player, null);


    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        mediaSession.release()
        mediaSessionConnector.setPlayer(null, null)
        playerNotificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }
}