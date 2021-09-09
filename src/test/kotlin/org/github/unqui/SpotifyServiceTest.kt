
import org.github.unqui.*

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SpotifyServiceTest {

    private fun registerUserJuan(spotifyService: SpotifyService): User {
        val user = spotifyService.register(UserDraft("juan@gmail.com", "image", "password", "Jota"))
        assertEquals(user.displayName, "Jota")
        assertEquals(user.email, "juan@gmail.com")
        assertEquals(user.image, "image")
        assertEquals(user.password, "password")
        return user
    }
    private fun registerUserPepe(spotifyService: SpotifyService): User {
        val user = spotifyService.register(UserDraft("pepe@gmail.com", "image", "password", "Pepe"))
        assertEquals(user.displayName, "Pepe")
        assertEquals(user.email, "pepe@gmail.com")
        assertEquals(user.image, "image")
        assertEquals(user.password, "password")
        return user
    }
    private fun addSong(spotifyService: SpotifyService, number: Int): Song {
        val songDraft = SongDraft("song_$number", "band_$number", "url", 320)
        val song = spotifyService.addSong(songDraft)
        assertEquals(song.name, "song_$number")
        assertEquals(song.band, "band_$number")
        assertEquals(song.url, "url")
        assertEquals(song.duration, 320)
        return song
    }
    private fun addPlaylist(spotifyService: SpotifyService, userId: String, number: Int, songs: MutableList<Song>): Playlist {
        val amountOfPlaylist = spotifyService.getUser(userId).myPlaylists.size

        val playlistDraft = PlayListDraft("playlist_$number", "playlist_$number", "image", songs)
        val playlist = spotifyService.addPlaylist(userId, playlistDraft)

        val updatedAmountOfPlaylist = spotifyService.getUser(userId).myPlaylists.size

        assertEquals(playlist.name, "playlist_$number")
        assertEquals(playlist.description, "playlist_$number")
        assertEquals(playlist.image, "image")
        assertEquals(playlist.songs.size, songs.size)

        assertEquals(updatedAmountOfPlaylist, amountOfPlaylist + 1)

        return playlist
    }

    @Test
    fun registerUserTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val getUser = spotifyService.getUser(user.id)
        assertEquals(getUser, user)
    }

    @Test
    fun registerUserTwoTimesTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        registerUserJuan(spotifyService)
        assertFailsWith<UserException> {
            registerUserJuan(spotifyService)
        }
    }

    @Test
    fun loginTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val loginUser = spotifyService.login("juan@gmail.com", "password")
        assertEquals(loginUser, user)
    }

    @Test
    fun loginWrongEmailTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        registerUserJuan(spotifyService)
        assertFailsWith<UserException> {
            spotifyService.login("jota@gmail.com", "password")
        }
    }

    @Test
    fun loginWrongPasswordTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        registerUserJuan(spotifyService)
        assertFailsWith<UserException> {
            spotifyService.login("juan@gmail.com", "pass")
        }
    }

    @Test
    fun addSongTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        assertEquals(spotifyService.getAllSongs().size, 0)
        addSong(spotifyService, 1)
        assertEquals(spotifyService.getAllSongs().size, 1)
    }

    @Test
    fun addSongTwoTimesTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())

        assertEquals(spotifyService.getAllSongs().size, 0)
        addSong(spotifyService, 1)
        assertEquals(spotifyService.getAllSongs().size, 1)

        assertFailsWith<SongException> {
            addSong(spotifyService, 1)
        }
    }

    @Test
    fun addPlaylistTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(user.myPlaylists.size, 0)
        assertEquals(spotifyService.getAllPlaylists().size, 0)
        addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)
    }

    @Test
    fun addPlaylistTwoTimesTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(user.myPlaylists.size, 0)
        assertEquals(spotifyService.getAllPlaylists().size, 0)
        addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)

        assertFailsWith<PlaylistException> {
            addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        }
    }

    @Test
    fun addPlaylistWithWrongUserIdTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())

        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(spotifyService.getAllPlaylists().size, 0)
        assertFailsWith<UserException> {
            addPlaylist(spotifyService, "user_id", 1, mutableListOf(song1, song2, song3))
        }
    }

    @Test
    fun addPlaylistWithoutSongsTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)

        assertEquals(user.myPlaylists.size, 0)
        assertEquals(spotifyService.getAllPlaylists().size, 0)
        assertFailsWith<PlaylistException> {
            addPlaylist(spotifyService, user.id, 1, mutableListOf())
        }
    }

    @Test
    fun modifyPlaylistTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(user.myPlaylists.size, 0)
        assertEquals(spotifyService.getAllPlaylists().size, 0)
        val playlist = addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)

        spotifyService.modifyPlaylist(user.id, playlist.id, PlayListDraft("pepe", "pepe", "pepe", mutableListOf(song1, song2)))
        assertEquals(playlist.name, "pepe")
        assertEquals(playlist.description, "pepe")
        assertEquals(playlist.image, "pepe")
        assertEquals(playlist.songs.size, 2)
        assertEquals(playlist.duration(), 640)
    }

    @Test
    fun modifyPlaylistWithInvalidUserIdTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(spotifyService.getAllPlaylists().size, 0)
        val playlist = addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)

        assertFailsWith<UserException> {
            spotifyService.modifyPlaylist("pepe", playlist.id, PlayListDraft("pepe", "pepe", "pepe", mutableListOf(song1, song2)))
        }
    }

    @Test
    fun modifyPlaylistWithInvalidPlaylistIdTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(spotifyService.getAllPlaylists().size, 0)
        addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)

        assertFailsWith<PlaylistException> {
            spotifyService.modifyPlaylist(user.id, "pepe", PlayListDraft("pepe", "pepe", "pepe", mutableListOf(song1, song2)))
        }
    }

    @Test
    fun modifyPlaylistWithAnotherUserTest() {
        val spotifyService = SpotifyService(mutableListOf(), mutableListOf(), mutableListOf())
        val user = registerUserJuan(spotifyService)
        val userPepe = registerUserPepe(spotifyService)
        val song1 = addSong(spotifyService, 1)
        val song2 = addSong(spotifyService, 2)
        val song3 = addSong(spotifyService, 3)

        assertEquals(spotifyService.getAllPlaylists().size, 0)
        val playlist = addPlaylist(spotifyService, user.id, 1, mutableListOf(song1, song2, song3))
        assertEquals(spotifyService.getAllPlaylists().size, 1)

        assertFailsWith<PlaylistException> {
            spotifyService.modifyPlaylist(userPepe.id, playlist.id, PlayListDraft("pepe", "pepe", "pepe", mutableListOf(song1, song2)))
        }
    }
}
