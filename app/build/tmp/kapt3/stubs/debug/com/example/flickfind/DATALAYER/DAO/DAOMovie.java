package com.example.flickfind.DATALAYER.DAO;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u00020\u00032\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0010\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0018H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001b0\tH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\u001c\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001d\u001a\u00020\u001eH\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0014\u0010 \u001a\b\u0012\u0004\u0012\u00020!0\tH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\"\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006#\u00c0\u0006\u0003"}, d2 = {"Lcom/example/flickfind/DATALAYER/DAO/DAOMovie;", "", "insertMovie", "", "movie", "Lcom/example/flickfind/DATALAYER/Room/RoomMovies;", "(Lcom/example/flickfind/DATALAYER/Room/RoomMovies;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMovies", "list", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllMovies", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGenres", "genres", "Lcom/example/flickfind/DATALAYER/Room/RoomGenre;", "insertStudios", "studios", "Lcom/example/flickfind/DATALAYER/Room/RoomStudio;", "insertMovieGenreCrossRef", "crossRef", "Lcom/example/flickfind/DATALAYER/Room/MovieGenreCrossRef;", "(Lcom/example/flickfind/DATALAYER/Room/MovieGenreCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMovieStudioCrossRef", "Lcom/example/flickfind/DATALAYER/Room/MovieStudioCrossRef;", "(Lcom/example/flickfind/DATALAYER/Room/MovieStudioCrossRef;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMoviesWithGenres", "Lcom/example/flickfind/DATALAYER/DAO/MovieWithGenres;", "getMovieWithGenresById", "movieId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMoviesWithStudios", "Lcom/example/flickfind/DATALAYER/DAO/MovieWithStudios;", "clearAll", "app_debug"})
@androidx.room.Dao()
public abstract interface DAOMovie {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovie(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.Room.RoomMovies movie, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovies(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.flickfind.DATALAYER.Room.RoomMovies> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllMovies(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.flickfind.DATALAYER.Room.RoomMovies>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGenres(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.flickfind.DATALAYER.Room.RoomGenre> genres, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertStudios(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.flickfind.DATALAYER.Room.RoomStudio> studios, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovieGenreCrossRef(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.Room.MovieGenreCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovieStudioCrossRef(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.Room.MovieStudioCrossRef crossRef, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoviesWithGenres(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.flickfind.DATALAYER.DAO.MovieWithGenres>> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM movieData WHERE IDMovie = :movieId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMovieWithGenresById(@org.jetbrains.annotations.NotNull()
    java.lang.String movieId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.flickfind.DATALAYER.DAO.MovieWithGenres> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMoviesWithStudios(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.flickfind.DATALAYER.DAO.MovieWithStudios>> $completion);
    
    @androidx.room.Query(value = "DELETE FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}