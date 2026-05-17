package com.example.flickfind.testTinhNang.dataLayerTest.Repository;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J \u0010\b\u001a\u00020\t2\u0018\u0010\n\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f\u0012\u0004\u0012\u00020\t0\u000bJ\u000e\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\rJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\fH\u0086@\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/example/flickfind/testTinhNang/dataLayerTest/Repository/RepositoryTest;", "", "dao", "Lcom/example/flickfind/testTinhNang/dataLayerTest/DAO/DAOTest;", "<init>", "(Lcom/example/flickfind/testTinhNang/dataLayerTest/DAO/DAOTest;)V", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "getMovies", "", "onResult", "Lkotlin/Function1;", "", "Lcom/example/flickfind/testTinhNang/dataLayerTest/dataClassTest;", "saveMovie", "movie", "getSavedMovies", "Lcom/example/flickfind/testTinhNang/dataLayerTest/ROOM/UNITYTest;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RepositoryTest {
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    
    public RepositoryTest(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest dao) {
        super();
    }
    
    public final void getMovies(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest>, kotlin.Unit> onResult) {
    }
    
    public final void saveMovie(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest movie) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSavedMovies(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest>> $completion) {
        return null;
    }
}