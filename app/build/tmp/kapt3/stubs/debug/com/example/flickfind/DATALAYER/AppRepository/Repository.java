package com.example.flickfind.DATALAYER.AppRepository;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J \u0010\f\u001a\u00020\r2\u0018\u0010\u000e\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u0010\u0012\u0004\u0012\u00020\r0\u000fJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u0011J \u0010\u0014\u001a\u00020\r2\u0018\u0010\u000e\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u0010\u0012\u0004\u0012\u00020\r0\u000fJF\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u001826\u0010\u000e\u001a2\u0012\u0013\u0012\u00110\u0018\u00a2\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001b\u0012\u0013\u0012\u00110\u0018\u00a2\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\u0004\u0012\u00020\r0\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001d"}, d2 = {"Lcom/example/flickfind/DATALAYER/AppRepository/Repository;", "", "remote", "Lcom/example/flickfind/DATALAYER/Remote/AppRemote;", "movieDao", "Lcom/example/flickfind/DATALAYER/DAO/DAOMovie;", "<init>", "(Lcom/example/flickfind/DATALAYER/Remote/AppRemote;Lcom/example/flickfind/DATALAYER/DAO/DAOMovie;)V", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "getDb", "()Lcom/google/firebase/firestore/FirebaseFirestore;", "getMovies", "", "onResult", "Lkotlin/Function1;", "", "Lcom/example/flickfind/DATALAYER/DataClass/DataMovie;", "saveMovieToLocal", "movie", "getSavedMovies", "Lcom/example/flickfind/DATALAYER/Room/RoomMovies;", "getUserProfile", "email", "", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "avatar", "app_debug"})
public final class Repository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.DATALAYER.Remote.AppRemote remote = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.DATALAYER.DAO.DAOMovie movieDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    
    public Repository(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.Remote.AppRemote remote, @org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.DAO.DAOMovie movieDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.firestore.FirebaseFirestore getDb() {
        return null;
    }
    
    public final void getMovies(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.example.flickfind.DATALAYER.DataClass.DataMovie>, kotlin.Unit> onResult) {
    }
    
    public final void saveMovieToLocal(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.DataClass.DataMovie movie) {
    }
    
    public final void getSavedMovies(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.example.flickfind.DATALAYER.Room.RoomMovies>, kotlin.Unit> onResult) {
    }
    
    public final void getUserProfile(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.String, kotlin.Unit> onResult) {
    }
}