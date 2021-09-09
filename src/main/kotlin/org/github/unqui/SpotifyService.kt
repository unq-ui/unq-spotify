import org.github.unqui.*
import java.time.LocalDateTime

class SpotifyService(private val listSongs: MutableList<Song>, private val listPlaylist: MutableList<Playlist>, private val listUser: MutableList<User>) {

    private val idGenerator = IdGenerator()

    fun getAllSongs(): List<Song> {
        return listSongs
    }

    fun getAllPlaylists(): List<Playlist> {
        return listPlaylist
    }

    fun addPlaylist(userId: String, playListDraft: PlayListDraft): Playlist {
        if (listPlaylist.any { it.name == playListDraft.name }) throw PlaylistException("Playlist with same name")
        if (playListDraft.songs.size == 0) throw PlaylistException("Should have 1 song at least")
        val user = getUser(userId)
        val newPlaylist = Playlist(idGenerator.getPlaylistId(), playListDraft.name, playListDraft.description, playListDraft.image, playListDraft.songs, user, LocalDateTime.now(), mutableListOf())
        user.myPlaylists.add(newPlaylist)
        listPlaylist.add(newPlaylist)
        return newPlaylist
    }

    fun addSong(songDraft: SongDraft): Song {
        if (listSongs.any { it.name == songDraft.name }) throw SongException("Playlist with same name")
        val song = Song(idGenerator.getSongId(), songDraft.name, songDraft.band, songDraft.url, songDraft.duration)
        listSongs.add(song)
        return song
    }

    fun modifyPlaylist(userId: String, playlistId: String, playListDraft: PlayListDraft): Playlist {
        val user = getUser(userId)
        val playlist = getPlaylist(playlistId)
        if (playlist.author.id !== user.id) throw PlaylistException("Not permitted")
        playlist.name = playListDraft.name;
        playlist.description = playListDraft.description;
        playlist.image = playListDraft.image;
        playlist.songs = playListDraft.songs
        playlist.lastModifiedDate = LocalDateTime.now()
        return playlist
    }

    fun getPlaylist(playlistId: String): Playlist {
        return listPlaylist.find { it.id == playlistId } ?: throw PlaylistException("Not found user")
    }

    fun getUser(userId: String): User {
        return listUser.find { it.id == userId } ?: throw UserException("Not found user")
    }

    fun login(email: String, password: String): User {
        return listUser.find { it.email == email && it.password == password } ?: throw UserException("Not found user")
    }

    fun register(userDraft: UserDraft): User {
        if (listUser.any { it.email == userDraft.email }) throw UserException("Email already used")
        val user = User(idGenerator.getUserId(), userDraft.email, userDraft.image, userDraft.password, userDraft.displayName, mutableListOf(), mutableListOf())
        listUser.add(user)
        return user
    }

    fun addOrRemoveLike(userId: String, playlistId: String) {
        val user = getUser(userId)
        val playlist = getPlaylist(playlistId)
        if (user.likes.contains(playlist)) {
            user.likes.remove(playlist)
            playlist.likes.remove(user)
        } else {
            user.likes.add(playlist)
            playlist.likes.add(user)
        }
    }

    fun searchUser(name: String): List<User> {
        return listUser.filter { it.displayName.contains(name, true) }
    }

    fun searchPlaylist(name: String): List<Playlist> {
        return listPlaylist.filter { it.name.contains(name, true) }
    }

    fun searchSong(name: String): List<Song> {
        return listSongs.filter { it.name.contains(name, true) }
    }

}