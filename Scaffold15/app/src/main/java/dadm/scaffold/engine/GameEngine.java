package dadm.scaffold.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;
import dadm.scaffold.sound.SoundManager;

public class GameEngine {

    private List<GameObject> gameObjects = new ArrayList<GameObject>();
    private List<GameObject> objectsToAdd = new ArrayList<GameObject>();
    private List<GameObject> objectsToRemove = new ArrayList<GameObject>();
    private List<Collision> detectedCollisions = new ArrayList<Collision>();
    private QuadTree quadTree = new QuadTree();

    private UpdateThread theUpdateThread;
    private DrawThread theDrawThread;
    public InputController theInputController;
    private final GameView theGameView;

    public Random random = new Random();

    public int vidasPlayer = 0;
    public int enemigosEliminados = 0;
    public int puntos = 0;
    public int indexPlayer;
    public boolean win = false;

    public ArrayList<PlayerAvatar> listAvatars = new ArrayList<>();

    private SoundManager soundManager;

    public int width;
    public int height;
    public double pixelFactor;

    private Activity mainActivity;

    public GameEngine(Activity activity, GameView gameView) {
        mainActivity = activity;

        theGameView = gameView;
        theGameView.setGameObjects(this.gameObjects);

        QuadTree.init();

        this.width = theGameView.getWidth()
                - theGameView.getPaddingRight() - theGameView.getPaddingLeft();
        this.height = theGameView.getHeight()
                - theGameView.getPaddingTop() - theGameView.getPaddingTop();

        quadTree.setArea(new Rect(0, 0, width, height));

        this.pixelFactor = this.height / 400d;

        createListSpritesPlayer();
    }

    public void setTheInputController(InputController inputController) {
        theInputController = inputController;
    }

    public void startGame() {
        // Stop a game if it is running
        stopGame();

        // Setup the game objects
        int nugameObjects = gameObjects.size();
        for (int i = 0; i < nugameObjects; i++) {
            gameObjects.get(i).startGame();
        }

        // Start the update thread
        theUpdateThread = new UpdateThread(this);
        theUpdateThread.start();

        // Start the drawing thread
        theDrawThread = new DrawThread(this);
        theDrawThread.start();


    }

    public void stopGame() {
        if (theUpdateThread != null) {
            theUpdateThread.stopGame();
        }
        if (theDrawThread != null) {
            theDrawThread.stopGame();
        }
    }

    public void pauseGame() {
        if (theUpdateThread != null) {
            theUpdateThread.pauseGame();
        }
        if (theDrawThread != null) {
            theDrawThread.pauseGame();
        }
    }

    public void resumeGame() {
        if (theUpdateThread != null) {
            theUpdateThread.resumeGame();
        }
        if (theDrawThread != null) {
            theDrawThread.resumeGame();
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (isRunning()) {
            objectsToAdd.add(gameObject);
        } else {
            addGameObjectNow(gameObject);
        }
        mainActivity.runOnUiThread(gameObject.onAddedRunnable);
    }

    public void removeGameObject(GameObject gameObject) {
        objectsToRemove.add(gameObject);
        mainActivity.runOnUiThread(gameObject.onRemovedRunnable);
    }

    public void onUpdate(long elapsedMillis) {
        int nugameObjects = gameObjects.size();
        for (int i = 0; i < nugameObjects; i++) {
            GameObject go =  gameObjects.get(i);
            go.onUpdate(elapsedMillis, this);
            if(go instanceof ScreenGameObject) {
                ((ScreenGameObject) go).onPostUpdate(this);
            }
        }
        checkCollisions();
        synchronized (gameObjects) {
            while (!objectsToRemove.isEmpty()) {
                GameObject objectToRemove = objectsToRemove.remove(0);
                gameObjects.remove(objectToRemove);
                if (objectToRemove instanceof  ScreenGameObject) {
                    quadTree.removeGameObject((ScreenGameObject) objectToRemove);
                }
            }
            while (!objectsToAdd.isEmpty()) {
                GameObject gameObject = objectsToAdd.remove(0);
                addGameObjectNow(gameObject);
            }
        }
    }

    public void onDraw() {
        theGameView.draw();
    }

    public boolean isRunning() {
        return theUpdateThread != null && theUpdateThread.isGameRunning();
    }

    public boolean isPaused() {
        return theUpdateThread != null && theUpdateThread.isGamePaused();
    }

    public Context getContext() {
        return theGameView.getContext();
    }

    private void checkCollisions() {
        // Release the collisions from the previous step
        while (!detectedCollisions.isEmpty()) {
            Collision.release(detectedCollisions.remove(0));
        }
        quadTree.checkCollisions(this, detectedCollisions);
    }

    private void addGameObjectNow (GameObject object) {
        gameObjects.add(object);
        if (object instanceof ScreenGameObject) {
            ScreenGameObject sgo = (ScreenGameObject) object;

            quadTree.addGameObject(sgo);
        }
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    public void onGameEvent (GameEvent gameEvent) {
        // We notify all the GameObjects
        // Also the sound manager
        soundManager.playSoundForGameEvent(gameEvent);
    }


    public void createListSpritesPlayer(){
        listAvatars.add(new PlayerAvatar(0, R.drawable.dragonite1, "Dragonite"));
        listAvatars.add(new PlayerAvatar(1, R.drawable.snorlax1, "Snorlax"));
        listAvatars.add(new PlayerAvatar(2, R.drawable.rapidash1, "Rapidash"));
    }


    //METODOS PARA EL PINTADO DE LA UI.
    //Llama al runnable
    public void llamarRunnableVidas(int v){
        vidasPlayer = v;
        mainActivity.runOnUiThread(drawTextLifes);
    }
    public void llamarRunnableEnemigos(int e){
        enemigosEliminados = e;
        mainActivity.runOnUiThread(drawTextEnemies);
    }
    public void llamarRunnablePuntos(int p){
        puntos = p;
        mainActivity.runOnUiThread(drawTextPoints);
    }
    public void llamarRunnableGameOver(boolean wins){
        win = wins;
        mainActivity.runOnUiThread(gameOver);
    }


    //Llama al m??todo correspondiente del Scaffold Activity
    public final Runnable drawTextLifes = new Runnable() {
        @Override
        public void run() {
            ((ScaffoldActivity) mainActivity).printLifes(vidasPlayer);
        }
    };
    public final Runnable drawTextEnemies = new Runnable() {
        @Override
        public void run() {
            ((ScaffoldActivity) mainActivity).printEnemies(enemigosEliminados);
        }
    };
    public final Runnable drawTextPoints = new Runnable() {
        @Override
        public void run() {
            ((ScaffoldActivity) mainActivity).printPoints(puntos);
        }
    };

    public final Runnable gameOver = new Runnable() {
        @Override
        public void run() {
            ((ScaffoldActivity) mainActivity).endScreen(puntos, enemigosEliminados, win);
        }
    };

}
