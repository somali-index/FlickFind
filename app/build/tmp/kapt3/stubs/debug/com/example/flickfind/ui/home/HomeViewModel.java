package com.example.flickfind.ui.home;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0011J\b\u0010\u0015\u001a\u00020\u0013H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/example/flickfind/ui/home/HomeViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "repository", "Lcom/example/flickfind/DATALAYER/AppRepository/Repository;", "_homeUiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/flickfind/ui/home/HomeUiState;", "homeUiState", "Lkotlinx/coroutines/flow/StateFlow;", "getHomeUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "allMoviesHome", "", "Lcom/example/flickfind/DATALAYER/DataClass/DataMovie;", "saveMovie", "", "movie", "getMovieListHome", "app_debug"})
public final class HomeViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.flickfind.DATALAYER.AppRepository.Repository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.flickfind.ui.home.HomeUiState> _homeUiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.example.flickfind.ui.home.HomeUiState> homeUiState = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.flickfind.DATALAYER.DataClass.DataMovie> allMoviesHome;
    
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.example.flickfind.ui.home.HomeUiState> getHomeUiState() {
        return null;
    }
    
    public final void saveMovie(@org.jetbrains.annotations.NotNull()
    com.example.flickfind.DATALAYER.DataClass.DataMovie movie) {
    }
    
    private final void getMovieListHome() {
    }
}