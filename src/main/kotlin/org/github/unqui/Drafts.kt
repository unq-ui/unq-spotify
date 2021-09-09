package org.github.unqui

class SongDraft(var name: String, var band: String, var url: String, var duration: Int)

class PlayListDraft(var name: String, var description: String, var image: String,  var songs: MutableList<Song>)

class UserDraft(var email: String, var image: String, var password: String, var displayName: String)

class EditUser(var image: String, var password: String, var displayName: String)

