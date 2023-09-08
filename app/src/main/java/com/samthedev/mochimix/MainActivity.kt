package com.samthedev.mochimix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samthedev.mochimix.ui.theme.MochiMixTheme

data class Track(
    var name: String = "New Track",
    var author: String = "unknown",

    var cover: Painter? = null,
)

data class Playlist(
    var name: String = "New Playlist",
    var author: String = "unknown",

    var tracks: SnapshotStateList<Track> = SnapshotStateList<Track>()
)

class MainActivity : ComponentActivity() {
    private var playlists = SnapshotStateList<Playlist>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MochiMixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = { ModalDrawerSheet { PlaylistDisplay() } },
                        content = { MusicDisplay() }
                    )
                }
            }
        }
    }

    @Composable
    fun PlaylistDisplay() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // "Playlists" Text
            OutlinedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Playlists",
                        fontSize = 48.sp,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                    )

                    // Add Playlist Button
                    // TODO: playlist create screen
                    IconButton(
                        onClick = { playlists.add(Playlist()) },
                        content = {
                            Icon(
                                painterResource(R.drawable.ic_playlist_add),
                                null,
                                Modifier.size(64.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .size(64.dp)
                            .padding(8.dp)
                    )
                }
            }

            // Display Playlists
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                itemsIndexed(playlists) { id, playlist ->
                    PlaylistCard(id, playlist)
                }
            }
        }
    }

    @Composable
    fun PlaylistCard(id: Int, playlist: Playlist) {

//        val gradient = Brush.verticalGradient(
//                colors = listOf(Color.Transparent, Color.Transparent),
//                startY = 0F,
//                endY = 1F
//                )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .clickable { }
        ) {
            Row {
                Column {
                    Text(
                        text = playlist.name,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    )

                    Text(
                        text = "by unknown",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }

                if (id == 0) {
                    Icon(
                        painterResource(R.drawable.ic_equalizer),
                        null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .padding(16.dp)
//                            .background(gradient)
                    )
                }

                // TODO: cover image ?
            }
        }
    }

    @Composable
    fun MusicDisplay() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Playlist Name
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedCard {
                        Text(
                            text = "Playlist",
                            fontSize = 48.sp,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                        )
                    }

                    // Add Music Button
                    IconButton(
                        onClick = {},
                        content = {
                            Icon(
                                painterResource(R.drawable.ic_track_add),
                                null,
                                Modifier.size(64.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(1.0f)
                            .wrapContentWidth(Alignment.End)
                            .size(64.dp)
                    )
                }

                // Display Musics
//                LazyColumn {
//                    items(musics) { track ->
//                        Text(track)
//                    }
//                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .wrapContentHeight(Alignment.Bottom)
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {},
                    content = {
                        Icon(
                            painterResource(R.drawable.ic_arrow_left),
                            null,
                            Modifier.size(64.dp)
                        )
                    },
                    modifier = Modifier.size(80.dp)
                )

                IconButton(
                    onClick = {},
                    content = {
                        Icon(
                            painterResource(R.drawable.ic_play),
                            null,
                            Modifier.size(64.dp)
                        )
                    },
                    modifier = Modifier.size(80.dp)
                )

                IconButton(
                    onClick = {},
                    content = {
                        Icon(
                            painterResource(R.drawable.ic_arrow_right),
                            null,
                            Modifier.size(64.dp)
                        )
                    },
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}