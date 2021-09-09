package org.github.unqui

class SongDraft(val name: String, val band: String, val url: String, val duration: Int)

class PlayListDraft(val name: String, val description: String, val image: String,  val songs: MutableList<Song>)

class UserDraft(val email: String, val image: String, val password: String, val displayName: String)

