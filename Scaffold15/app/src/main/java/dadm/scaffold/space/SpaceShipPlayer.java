package dadm.scaffold.space;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.counter.MainMenuFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.PlayerAvatar;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 500;
    List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;

    public int lifes = 3;
    public int enemiesDown = 0;
    public int points = 0;

    private int tipoFuego = 0;
    private int maxX;
    private int maxY;
    private double speedFactor;


    public SpaceShipPlayer(GameEngine gameEngine){
        //super(gameEngine, R.drawable.dragonite1);
        super(gameEngine, gameEngine.listAvatars.get(gameEngine.indexPlayer).getIdDrawable());  //Cogemos el avatar seleccionado
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height/2;    //CAMBIADO

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY -50;   //CAMBIADO
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }
//gameEngine.theInputController.isFiring &&
    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            tipoFuego = gameEngine.theInputController.tipoFuego;
            if (tipoFuego >2){
                tipoFuego = tipoFuego - 3;
            }
            //bullet.init(this, positionX + width/2, positionY);
            bullet.init(this, positionX + (width/2)/2, positionY, tipoFuego);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            if (lifes > 1){
                lifes--;
            }
            else{
                boolean win = false;
                gameEngine.llamarRunnableGameOver(win);
                gameEngine.removeGameObject(this);
            }
            gameEngine.llamarRunnableVidas(lifes);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }
        if(otherObject instanceof Revive){
            lifes++;
            gameEngine.llamarRunnableVidas(lifes);
            Revive r = (Revive) otherObject;
            r.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.RevivirHit); //CAMBIAR POR SONIDO CURACIÓN
        }
    }

}
