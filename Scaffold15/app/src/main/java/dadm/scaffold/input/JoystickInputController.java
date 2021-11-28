package dadm.scaffold.input;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import dadm.scaffold.R;

public class JoystickInputController extends InputController {

    private ImageView joystickInterno, joystickMini;
    private ImageButton fireButton;


    private float startingPositionX;
    private float startingPositionY;


    private final double maxDistance;

    public JoystickInputController(View view) {
        view.findViewById(R.id.joystick_main).setOnTouchListener(new JoystickTouchListener());
        view.findViewById(R.id.joyStick_Touch).setOnTouchListener(new FireButtonTouchListener());
        joystickInterno = view.findViewById(R.id.imageJoystick);
        joystickMini = view.findViewById(R.id.joyStick_Mini);
        fireButton = view.findViewById(R.id.joyStick_Touch);
        double pixelFactor = view.getHeight() / 400d;
        maxDistance = 50*pixelFactor;
        tipoFuego = 0;
    }

    private class JoystickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                joystickInterno.setVisibility(View.VISIBLE);
                startingPositionX = event.getX(0);
                startingPositionY = event.getY(0);
                joystickInterno.setX(startingPositionX - 50);
                joystickInterno.setY(startingPositionY - 50);
            }
            else if (action == MotionEvent.ACTION_UP) {
                horizontalFactor = 0;
                verticalFactor = 0;
                joystickInterno.setVisibility(View.INVISIBLE);
                joystickMini.setVisibility(View.INVISIBLE);
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                // Get the proportion to the max
                joystickMini.setVisibility(View.VISIBLE);
                horizontalFactor = (event.getX(0) - startingPositionX) / maxDistance;
                verticalFactor = (event.getY(0) - startingPositionY) / maxDistance;
                double movedX = event.getX(0);
                double movedY = event.getY(0);

                if (movedX > startingPositionX){
                    joystickMini.setX(startingPositionX + 50);
                }else if(movedX < startingPositionX){
                    joystickMini.setX(startingPositionX - 100);
                }
                //else if (movedX == startingPositionX){
                 //   joystickMini.setX(startingPositionX);
                //}
                if (movedY > startingPositionY){
                    joystickMini.setY(startingPositionY + 50);
                }else if(movedY < startingPositionY){
                    joystickMini.setY(startingPositionY - 100);
                }

                if((movedX - startingPositionX) > - 30 && (movedX - startingPositionX) < 30){
                    joystickMini.setX(startingPositionX - 25);
                }
                if((movedY -startingPositionY)> - 30 && (movedY - startingPositionY) < 30){
                    joystickMini.setY(startingPositionY - 25);
                }


                if (horizontalFactor > 1) {
                    horizontalFactor = 1;
                }
                else if (horizontalFactor < -1) {
                    horizontalFactor = -1;
                }
                verticalFactor = (event.getY(0) - startingPositionY) / maxDistance;
                if (verticalFactor > 1) { //Abajo
                    verticalFactor = 1;

                }
                else if (verticalFactor < -1) { //Arriba
                    verticalFactor = -1;

                }
            }
            return true;
        }
    }

    private class FireButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                isFiring = true;
            }
            else if (action == MotionEvent.ACTION_UP) {
                tipoFuego++;
                if (tipoFuego % 3== 0){
                    fireButton.setImageResource(R.drawable.fire_up);
                }else if(tipoFuego%3 == 1){
                    fireButton.setImageResource(R.drawable.fire_right);
                }else if(tipoFuego%3 == 2){
                    fireButton.setImageResource(R.drawable.fire_left);
                }
                isFiring = false;
            }
            return true;
        }
    }
}

