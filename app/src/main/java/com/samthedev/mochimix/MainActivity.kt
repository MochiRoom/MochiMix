@file:OptIn(ExperimentalMaterial3Api::class)

package com.samthedev.mochimix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samthedev.mochimix.ui.theme.MochiMixTheme
import java.lang.reflect.Field

class MainActivity : ComponentActivity() {
    private var playlist = SnapshotStateList<Playlist>()
    private var selected by mutableStateOf(-1)

    private val musicPlayer = MusicPlayer(this@MainActivity)

    private lateinit var focusManager: FocusManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist.add(Playlist("Default", "MochiMix"))
        val rawClass = R.raw::class.java

        // Get all fields (resource IDs) from the R.raw class
        val rawFields: Array<Field> = rawClass.declaredFields

        // Iterate through the fields
        for (field in rawFields) {
            try {
                // Get the resource ID using reflection
                val resourceId = field.getInt(null)

                playlist[0].track.add(Track(name = field.name, id = resourceId))
                // You can access the resource using the resourceId, for example:
                // val inputStream = resources.openRawResource(resourceId)
                // Do something with the inputStream
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }

        setContent {
            focusManager = LocalFocusManager.current

            MochiMixTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = { ModalDrawerSheet { PlaylistDisplay() } },
                        content = {
                            when (selected) {
                                -1 -> AboutPage()
                                else -> {
                                    if (selected < -1 || selected > playlist.size)
                                        selected = -1
                                    else
                                        TrackDisplay()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun AboutPage() {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "About",
                fontSize = 48.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp, bottom = 64.dp)
                ) {
                    Text(
                        text = "Coming Soon!",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 12.dp)
                        .wrapContentHeight(Alignment.Bottom)
                ) {
                    Text("About Buttons Here")
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
                        text = stringResource(R.string.playlistDrawerHeading),
                        fontSize = 48.sp,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                    )

                    // Add Playlist Button

                    var showDialog by rememberSaveable { mutableStateOf(false) }

                    IconButton(
                        onClick = { showDialog = true },
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

                    // Playlist Creation Dialog
                    if (showDialog) {
                        var name by rememberSaveable { mutableStateOf("") }
                        var author by rememberSaveable { mutableStateOf("unknown") }

                        CustomDialog(
                            heading = "Add Playlist",
                            onDismiss = { showDialog = false },
                            onConfirm = {
                                playlist.add(Playlist(name, author))
                                showDialog = false
                            },
                            enableConfirm = name.isNotBlank() and author.isNotBlank()
                        ) {
                            Column {
                                val focusRequester = remember { FocusRequester() }

                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Playlist Name") },
                                    isError = name.isBlank(),
                                    supportingText = { if (name.isBlank()) Text("*Field is required") },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
                                )

                                OutlinedTextField(
                                    value = author,
                                    onValueChange = { author = it },
                                    label = { Text("Author") },
                                    isError = author.isBlank(),
                                    supportingText = { if (author.isBlank()) Text("*Field is required") },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            if (author.isNotBlank()) {
                                                focusManager.clearFocus()
                                                playlist.add(Playlist(name, author))
                                                showDialog = false
                                            }
                                        }
                                    ),
                                    modifier = Modifier.focusRequester(focusRequester)
                                )
                            }
                        }
                    }
                }
            }

            if (playlist.size == 0) {
                // No Playlists Yet!
                Text(
                    text = stringResource(R.string.noPlaylistsText),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(32.dp)
                )
            } else {
                // Display Playlist(s)
                LazyColumn(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    itemsIndexed(playlist) { id, playlist ->
                        PlaylistCard(id, playlist)
                    }
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
                .clickable(enabled = selected != id) { selected = id }
        ) {
            Box(Modifier.fillMaxSize()) {
                // Draw Text
                Column {
                    Text(
                        text = playlist.name,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    )

                    Text(
                        text = "by ${playlist.author}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }

//                if (id == 0) {
//                    Icon(
//                        painterResource(R.drawable.ic_equalizer),
//                        null,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End)
//                            .padding(16.dp)
////                            .background(gradient)
//                    )
//                }

                // Draw Cover Image
                val cover =
                    if (playlist.cover != null) playlist.cover!! else painterResource(R.drawable.mochimix)

                // TODO: move to edge, fade out left
                Image(
                    painter = cover,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.End)
                        .size(128.dp)
                )
            }
        }
    }

    @Composable
    fun TrackDisplay() {
        Box(modifier = Modifier.fillMaxSize()) {
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
                            text = playlist[selected].name,
                            fontSize = 48.sp,
                            modifier = Modifier
                                .wrapContentWidth(Alignment.Start)
                                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
                        )

                        var showDialog by rememberSaveable { mutableStateOf(false) }

                        // Add Music Button
                        IconButton(
                            onClick = { showDialog = true },
                            content = {
                                Icon(
                                    painterResource(R.drawable.ic_track_add),
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

                        // Playlist Creation Dialog
                        if (showDialog) {
                            var name by rememberSaveable { mutableStateOf("") }
                            var author by rememberSaveable { mutableStateOf("unknown") }

                            CustomDialog(
                                heading = "Add Track",
                                onDismiss = { showDialog = false },
                                onConfirm = {
                                    playlist[selected].track.add(Track(name, author))
                                    showDialog = false
                                },
                                enableConfirm = name.isNotBlank() and author.isNotBlank()
                            ) {
                                Column {
                                    val focusRequester = remember { FocusRequester() }

                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = { name = it },
                                        label = { Text("Track Name") },
                                        isError = name.isBlank(),
                                        supportingText = { if (name.isBlank()) Text("*Field is required") },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        keyboardActions = KeyboardActions(onNext = { focusRequester.requestFocus() })
                                    )

                                    OutlinedTextField(
                                        value = author,
                                        onValueChange = { author = it },
                                        label = { Text("Author") },
                                        isError = author.isBlank(),
                                        supportingText = { if (author.isBlank()) Text("*Field is required") },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                if (author.isNotBlank()) {
                                                    focusManager.clearFocus()
                                                    playlist[selected].track.add(
                                                        Track(
                                                            name,
                                                            author
                                                        )
                                                    )
                                                    showDialog = false
                                                }
                                            }
                                        ),
                                        modifier = Modifier.focusRequester(focusRequester)
                                    )
                                }
                            }
                        }
                    }
                }

                if (playlist[selected].track.size == 0) {
                    // No Tracks Yet!
                    Text(
                        text = stringResource(R.string.noTracksText),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(32.dp)
                    )
                } else {
                    // Display Tracks
                    LazyColumn(
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        itemsIndexed(playlist[selected].track) { id, track ->
                            TrackCard(id, track)
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .wrapContentHeight(Alignment.Bottom)
                    .padding(16.dp)
            ) { ControlBar() }
        }
    }

    @Composable
    fun TrackCard(id: Int, track: Track) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .clickable { musicPlayer.play(track.id) }
        ) {
            Box(Modifier.fillMaxSize()) {
                // Draw Text
                Column {
                    Text(
                        text = track.name,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                    )

                    Text(
                        text = "by ${track.author}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }

//                if (id == 0) {
//                    Icon(
//                        painterResource(R.drawable.ic_equalizer),
//                        null,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentWidth(Alignment.End)
//                            .padding(16.dp)
////                            .background(gradient)
//                    )
//                }

                // Draw Cover Image
                val cover =
                    if (track.cover != null) track.cover!! else painterResource(R.drawable.mochimix)

                // TODO: move to edge, fade out left
                Image(
                    painter = cover,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentWidth(Alignment.End)
                        .size(80.dp)
                )
            }
        }
    }

    @Composable
    fun ControlBar() {
        IconButton(
            onClick = { /* TODO */ },
            content = {
                Icon(
                    painterResource(R.drawable.ic_arrow_left),
                    null,
                    Modifier.size(64.dp)
                )
            },
            modifier = Modifier.size(80.dp)
        )

        val isPlaying by rememberUpdatedState(newValue = musicPlayer.isPlaying())

        IconButton(
            onClick = {
                if (musicPlayer.mMediaPlayer.isPlaying)
                    musicPlayer.pause()
                else
                    musicPlayer.resume()
            },
            content = {
                Icon(
                    painterResource(if (musicPlayer.mMediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    null,
                    Modifier.size(64.dp)
                )
            },
            modifier = Modifier.size(100.dp),

            )

        IconButton(
            onClick = { /* TODO */ },
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

// Create Custom Dialog
@Composable
private fun CustomDialog(
    icon: ImageVector? = null,
    heading: String,

    dismissText: String = "Dismiss",
    confirmText: String = "Confirm",

    enableDismiss: Boolean = true,
    enableConfirm: Boolean = true,

    onDismiss: () -> Unit,
    onConfirm: () -> Unit,

    content: @Composable (() -> Unit)
) {
    AlertDialog(
        icon = {
            Row {
                if (icon != null)
                    Icon(icon, null, Modifier.padding(end = 8.dp))
                Text(heading)
            }
        },
        text = content,

        confirmButton = {
            TextButton(
                content = { Text(confirmText) },
                onClick = onConfirm,
                enabled = enableConfirm
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                content = { Text(dismissText) },
                onClick = onDismiss,
                enabled = enableDismiss
            )
        }
    )
}