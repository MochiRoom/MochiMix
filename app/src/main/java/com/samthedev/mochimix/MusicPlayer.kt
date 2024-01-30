package com.samthedev.mochimix

import android.media.MediaPlayer

class MusicPlayer(private val mainActivity: MainActivity) {
    var mMediaPlayer = MediaPlayer()

    fun play(id: Int) {
        mMediaPlayer.release()
        mMediaPlayer = MediaPlayer.create(mainActivity, id)
        mMediaPlayer.start()
    }

    fun pause() {
        mMediaPlayer.pause()
    }

    fun resume() {
        mMediaPlayer.start()
    }

    fun isPlaying(): Boolean {
        return mMediaPlayer.isPlaying
    }
}