package com.example.flickfind.testTinhNang.dataLayerTest.DAO;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\tH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006\u000e\u00c0\u0006\u0003"}, d2 = {"Lcom/example/flickfind/testTinhNang/dataLayerTest/DAO/DAOTest;", "", "insertMovie", "", "movie", "Lcom/example/flickfind/testTinhNang/dataLayerTest/ROOM/UNITYTest;", "(Lcom/example/flickfind/testTinhNang/dataLayerTest/ROOM/UNITYTest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMovies", "list", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllMovies", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAll", "app_debug"})
@androidx.room.Dao()
public abstract interface DAOTest {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovie(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest movie, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMovies(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest> list, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllMovies(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest>> $completion);
    
    @androidx.room.Query(value = "DELETE FROM movieData")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}