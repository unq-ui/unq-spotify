package org.github.unqui

class IdGenerator() {
    var userId = 0
    var songId = 0
    var playlistId = 0

    fun getUserId(): String {
        val id = userId
        userId += 1
        return "user_$id"
    }

    fun getSongId(): String {
        val id = songId
        songId += 1
        return "song_$id"
    }

    fun getPlaylistId(): String {
        val id = playlistId
        playlistId += 1
        return "playlist_$id"
    }

}
