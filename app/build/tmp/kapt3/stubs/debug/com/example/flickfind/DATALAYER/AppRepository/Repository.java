package com.example.flickfind.DATALAYER.AppRepository;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J \u0010\f\u001a\u00020\r2\u0018\u0010\u000e\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u0010\u0012\u0004\u0012\u00020\r0\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0012"}, d2 = {"Lcom/example/flickfind/DATALAYER/AppRepository/Repository;", "", "dao", "Lcom/example/flickfind/DATALAYER/DAO/DAOMovie;", "remote", "Lcom/example/flickfind/DATALAYER/Remote/AppRemote;", "<init>", "(Lcom/example/flickfind/DATALAYER/DAO/DAOMovie;Lcom/example/flickfind/DATALAYER/Remote/AppRemote;)V", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "getDb", "()Lcom/google/firebase/firestore/FirebaseFirestore;", "getMovies", "", "onResult", "Lkotlin/Function1;", "", "Lcom/example/flickfind/DATALAYER/DataClass/DataMovieClass;", "app_debug"})
public final class Repository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.DATALAYER.DAO.DAOMovie dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.DATALAYER.Remote.AppRemote remote = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    
    public Repository(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.DAO.DAOMovie dao, @org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.Remote.AppRemote remote) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.google.firebase.firestore.FirebaseFirestore getDb() {
        return null;
    }
    
    public final void getMovies(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.example.flickfind.DATALAYER.DataClass.DataMovieClass>, kotlin.Unit> onResult) {
    }
}