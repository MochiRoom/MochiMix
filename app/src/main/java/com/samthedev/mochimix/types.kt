package com.samthedev.mochimix

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.painter.Painter

data class Track(
    var name: String = "New Track",
    var author: String = "unknown",

    var id: Int = 0,

    var cover: Painter? = null,
)

data class Playlist(
    var name: String = "New Playlist",
    var author: String = "unknown",

    var cover: Painter? = null,

    var track: SnapshotStateList<Track> = SnapshotStateList<Track>()

    // TODO: functions
)