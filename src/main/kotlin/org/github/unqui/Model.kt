package org.github.unqui

import java.time.LocalDateTime

class Song(val id: String, val name: String, val band: String, val url: String, val duration: Int)

class Playlist(val id: String, var name: String, var description: String, var image: String, var songs: List<Song>, val author: User, var lastModifiedDate: LocalDateTime, val likes: MutableList<User>) {
    fun duration(): Int {
        return songs.fold(0) { acc, song -> acc + song.duration }
    }
}

class User(val id: String, val email: String, var image: String, var password: String, var displayName: String, val myPlaylists: MutableList<Playlist>, val likes: MutableList<Playlist>)
