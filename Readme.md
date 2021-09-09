# UNQ » UIs » TP Spotify

[![](https://jitpack.io/v/unq-ui/unq-spotify.svg)](https://jitpack.io/#unq-ui/unq-spotify)


Spotify es una plataforma donde los usuarios pueden escuchar musica y generar sus propias playlist. Ademas, el usario puede marcar con like a las playlist de los demas usuarios.

## Especificación de Dominio

### Dependencia

Agregar el repositorio:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Agregar la dependencia:

```xml
<dependency>
    <groupId>com.github.unq-ui</groupId>
    <artifactId>unq-spotify</artifactId>
    <version>v1.0.0</version>
</dependency>
```

Toda interacción con el dominio se hace a través de la clase `SpotifyService`. La programación del dominio ya es proveído por la cátedra.

Se tiene que instanciar el sistema de la siguiente forma:

```kotlin
val spotifyService = getSpotifyService()
```

### SpotifyService

```kotlin
fun getAllSongs(): List<Song>

fun getAllPlaylists(): List<Playlist>

// @Throw PlaylistException si existe una playlist con el mismo nombre o la lista de temas es vacia
// @Throw UserException si el userId no existe
fun addPlaylist(userId: String, playListDraft: PlayListDraft): Playlist

// @Throw SongException si existe una Song con el mismo nombre
fun addSong(songDraft: SongDraft): Song

// @Throw UserException si el userId no existe
// @Throw PlaylistException si el playlistId no existe o si el usuario que quiere modificarla no es el dueño de la playlist
fun modifyPlaylist(userId: String, playlistId: String, playListDraft: PlayListDraft): Playlist

// @Throw PlaylistException si el playlistId no existe
fun getPlaylist(playlistId: String): Playlist

// @Throw UserException si el userId no existe
fun getUser(userId: String): User

// @Throw UserException si no se encuentra un email registrado
fun login(email: String, password: String): User

// @Throw UserException si el email se encuentra en uso
fun register(userDraft: UserDraft): User

// @Throw UserException si el userId no existe
// @Throw PlaylistException si el playlistId no existe
fun addOrRemoveLike(userId: String, playlistId: String)

fun searchUser(name: String): List<User>

fun searchPlaylist(name: String): List<Playlist>

fun searchSong(name: String): List<Song>

```

### Song

```kotlin
class Song(
    val id: String,
    val name: String,
    val band: String,
    val url: String,
    val duration: Int // en segundos
)
```

### Playlist

```kotlin
class Playlist(
    val id: String,
    var name: String,
    var description: String,
    var image: String,
    var songs: List<Song>,
    val author: User,
    var lastModifiedDate: LocalDateTime,
    val likes: MutableList<User>
)

fun duration(): Int // en segundos

```

### User

```kotlin
class User(
    val id: String,
    val email: String,
    var image: String,
    var password: String,
    var displayName: String,
    val myPlaylists: MutableList<Playlist>,
    val likes: MutableList<Playlist>
)
```

### SongDraft

Es la representación de una song antes de ser guardada por el sistema

```kotlin
class SongDraft(
    val name: String,
    val band: String,
    val url: String,
    val duration: Int
)
```

### PlayListDraft

Es la representación de una playlist antes de ser guardada por el sistema

```kotlin
class PlayListDraft(
    val name: String,
    val description: String,
    val image: String,
    val songs: MutableList<Song>
 )
```

### UserDraft

Es la representación de un usuario antes de ser guardado por el sistema

```kotlin
class UserDraft(
    val email: String,
    val image: String,
    val password: String,
    val displayName: String
)
```


