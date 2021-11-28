package dadm.scaffold.counter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.PlayerAvatar;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener {

    public ImageButton playerButton;
    public ArrayList<PlayerAvatar> listAvatars = new ArrayList<>();
    public int currentAvatar = 0;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        createListSpritesPlayer();
        playerButton = view.findViewById(R.id.btn_player);
        playerButton.setImageResource(listAvatars.get(0).getIdDrawable());

        //FUNCIONALIDAD BOTON CAMBIAR NAVES
        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentAvatar+1 > listAvatars.size()-1){
                    currentAvatar = 0;
                }
                else{
                    currentAvatar++;
                }
                playerButton.setImageResource(listAvatars.get(currentAvatar).getIdDrawable());

            }
        });
    }

    @Override
    public void onClick(View v) {
        ((ScaffoldActivity)getActivity()).startGame(currentAvatar);
    }


    public void createListSpritesPlayer(){
        listAvatars.add(new PlayerAvatar(0, R.drawable.dragonite1, "Dragonite"));
        listAvatars.add(new PlayerAvatar(1, R.drawable.snorlax1, "Snorlax"));
        listAvatars.add(new PlayerAvatar(2, R.drawable.rapidash1, "Rapidash"));

    }
}
