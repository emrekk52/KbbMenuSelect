package com.emrek.kbbmenuselect.activitys

import android.app.Activity
import android.content.Context
import android.gesture.Gesture
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.SparseArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.databinding.ActivityStoryBinding
import com.emrek.kbbmenuselect.models.StoryModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaExtractor
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util.SDK_INT
import com.squareup.picasso.Picasso
import okhttp3.internal.Util
import java.lang.Exception
import java.util.*

class StoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityStoryBinding
    var simplePlayer: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playBackPosition: Long = 0
    var storyUrl = ""
    var frame: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<StoryModel>("storyObject")
        storyUrl = story?.brandContent!!

        if (story.isTick == true)
            binding.tick.visibility = View.VISIBLE

        binding.brandName.text = story?.brandName
        Picasso.get().load(story?.brandPicture).placeholder(R.drawable.question)
            .into(binding.brandPicture)

        initPlayer()


    }


    private fun initPlayer() {


        simplePlayer = SimpleExoPlayer.Builder(this@StoryActivity).build()
        binding.exoPlayer.player = simplePlayer

        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {

                if (ytFiles != null) {
                    val itag = 137
                    val audioTag = 140
                    val videoUrl = ytFiles[itag].url
                    val audioUrl = ytFiles[audioTag].url

                    val audioSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(
                                MediaItem.fromUri(audioUrl)
                            )

                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(
                                MediaItem.fromUri(videoUrl)
                            )

                    simplePlayer!!.setMediaSource(
                        MergingMediaSource(
                            true,
                            videoSource,
                            audioSource
                        ), true
                    )
                    simplePlayer!!.prepare()
                    simplePlayer!!.playWhenReady = playWhenReady
                    simplePlayer!!.seekTo(currentWindow, playBackPosition)

                }


            }


        }.extract(storyUrl, false, true)

        simplePlayer!!.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {

                    }
                    Player.STATE_BUFFERING -> {
                        binding.loadingBar.pauseAnimation()
                        binding.loadingCircle.visibility = View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        binding.loadingBar.playAnimation()
                        binding.loadingPlane.visibility = View.GONE
                        binding.loadingCircle.visibility = View.GONE
                        binding.exoPlayer.visibility = View.VISIBLE
                    }
                    Player.STATE_ENDED -> {
                        finish()
                    }
                }
            }
        })

        binding.cons.setOnTouchListener { v, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    simplePlayer?.playWhenReady = false
                    simplePlayer?.playbackState
                    frame = binding.loadingBar.progress
                    binding.loadingBar.cancelAnimation()

                }

                MotionEvent.ACTION_UP -> {
                    simplePlayer?.playWhenReady = true
                    simplePlayer?.playbackState
                    binding.loadingBar.setMinAndMaxProgress(frame, 1f)
                    binding.loadingBar.resumeAnimation()
                }

            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 24)
            initPlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 24 || simplePlayer == null) {
            initPlayer()
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        binding.exoPlayer.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onPause() {
        if (Build.VERSION.SDK_INT >= 24) releasePlayer()
        super.onPause()
    }

    private fun releasePlayer() {
        if (simplePlayer != null) {
            playWhenReady = simplePlayer!!.playWhenReady
            playBackPosition = simplePlayer!!.currentPosition
            currentWindow = simplePlayer!!.currentWindowIndex
            simplePlayer!!.release()
            simplePlayer = null
        }
    }

    override fun onStop() {
        if (Build.VERSION.SDK_INT >= 24) releasePlayer()
        super.onStop()
    }
}




