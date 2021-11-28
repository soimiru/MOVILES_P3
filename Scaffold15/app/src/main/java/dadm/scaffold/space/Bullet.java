package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    private double speedFactor;

    private SpaceShipPlayer parent;
    protected double speedX;
    protected double speedY;
    public int puntosAux, enemigosAux = 0;

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.bullet);

        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedY * elapsedMillis;
        positionX += speedX * elapsedMillis;
        //positionX += speedFactor * elapsedMillis;
        if (positionY < -height) {
            gameEngine.removeGameObject(this);
            // And return it to the pool
            parent.releaseBullet(this);
        }
    }


    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY, int tipoFuego) {
        switch (tipoFuego){
            case 0:
                speedX = speedFactor * 0;
                speedY = speedFactor * 0.5;
                break;
            case 1:
                speedX = speedFactor * -0.5;
                speedY = speedFactor * 0.5;
                break;
            case 2:
                speedX = speedFactor * 0.5;
                speedY = speedFactor * 0.5;
                break;
        }
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;

    }

    private void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
        // And return it to the pool
        parent.releaseBullet(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)

            //AUMENTAMOS LOS PUNTOS Y LOS ENEMIGOS ELIMINADOS
            puntosAux = parent.points+50;
            enemigosAux = parent.enemiesDown+1;
            parent.points = puntosAux;
            parent.enemiesDown = enemigosAux;

            gameEngine.llamarRunnablePuntos(puntosAux);
            gameEngine.llamarRunnableEnemigos(enemigosAux);

            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            //Si matas 20 enemigos llama a la pantalla de final mandándole un True ya que has ganado
            if (enemigosAux>=20){
                boolean win = true;
                gameEngine.llamarRunnableGameOver(win);
            }
        }
    }
}
