package dadm.scaffold;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import dadm.scaffold.counter.FinalFragment;
import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.counter.MainMenuFragment;
import dadm.scaffold.sound.SoundManager;

public class ScaffoldActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaffold);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundManager = new SoundManager(getApplicationContext());
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void startGame(int indexPlayer) {
        //IndexPlayer nos sirve para cambiar el sprite del jugador
        // Navigate the the game fragment, which makes the start automatically
        GameFragment gf = new GameFragment();
        gf.indexAvatar = indexPlayer;
        navigateToFragment(gf);
    }

    //CARGA EL FRAGMENTO CON LA INFORMACIÓN DE LA PARTIDA
    public void endScreen(int po, int en, boolean win){

        //Paramos el juego
        GameFragment fragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        fragment.invokeStopGame();

        //Creamos el fragmento de la pantalla final
        FinalFragment fg = new FinalFragment();
        fg.win = win;
        fg.puntos = po;
        fg.enemigos = en;
        navigateToFragment(fg);
    }

    //CARGA EL MENÚ PRINCIPAL
    public void exitFinal(){
        navigateToFragment( new MainMenuFragment());
    }

    private void navigateToFragment(BaseFragment dst) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    //METODOS PARA PINTAR EL TEXTO DE LA UI. LLaman a los métodos que hay en GameFragment
    public void printLifes(int v){
        GameFragment fragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        fragment.changeTextLifes(v);
    }

    public void printEnemies(int e){
        GameFragment fragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        fragment.changeTextEnemies(e);
    }

    public void printPoints(int p){
        GameFragment fragment = (GameFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        fragment.changeTextPoints(p);
    }

    //FIN MÉTODOS PINTAR UI


    @Override
    public void onBackPressed() {
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }


    //a
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }


}
