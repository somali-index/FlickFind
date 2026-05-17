package com.example.flickfind.testTinhNang.UITest;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\u001d\u001a\u00020\u001eJ\u0006\u0010\u001f\u001a\u00020\u001eJ\u000e\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u0015R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R7\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u00142\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u00148F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u001b\u0010\u001c\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001a\u00a8\u0006\""}, d2 = {"Lcom/example/flickfind/testTinhNang/UITest/ViewModelTest;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "db", "Lcom/example/flickfind/testTinhNang/dataLayerTest/AppDatabase;", "dao", "Lcom/example/flickfind/testTinhNang/dataLayerTest/DAO/DAOTest;", "repository", "Lcom/example/flickfind/testTinhNang/dataLayerTest/Repository/RepositoryTest;", "savedMovies", "Landroidx/compose/runtime/snapshots/SnapshotStateList;", "Lcom/example/flickfind/testTinhNang/dataLayerTest/ROOM/UNITYTest;", "getSavedMovies", "()Landroidx/compose/runtime/snapshots/SnapshotStateList;", "setSavedMovies", "(Landroidx/compose/runtime/snapshots/SnapshotStateList;)V", "<set-?>", "", "Lcom/example/flickfind/testTinhNang/dataLayerTest/dataClassTest;", "movieList", "getMovieList", "()Ljava/util/List;", "setMovieList", "(Ljava/util/List;)V", "movieList$delegate", "Landroidx/compose/runtime/MutableState;", "loadMovies", "", "loadSavedMovies", "saveMovie", "movie", "app_debug"})
public final class ViewModelTest extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.testTinhNang.dataLayerTest.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.testTinhNang.dataLayerTest.DAO.DAOTest dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.testTinhNang.dataLayerTest.Repository.RepositoryTest repository = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.compose.runtime.snapshots.SnapshotStateList<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest> savedMovies;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState movieList$delegate = null;
    
    public ViewModelTest(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.runtime.snapshots.SnapshotStateList<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest> getSavedMovies() {
        return null;
    }
    
    public final void setSavedMovies(@org.jetbrains.annotations.NotNull()
    androidx.compose.runtime.snapshots.SnapshotStateList<com.example.flickfind.testTinhNang.dataLayerTest.ROOM.UNITYTest> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest> getMovieList() {
        return null;
    }
    
    public final void setMovieList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest> p0) {
    }
    
    public final void loadMovies() {
    }
    
    public final void loadSavedMovies() {
    }
    
    public final void saveMovie(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.testTinhNang.dataLayerTest.dataClassTest movie) {
    }
}