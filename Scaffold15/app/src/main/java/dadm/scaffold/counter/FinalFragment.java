package dadm.scaffold.counter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;

public class FinalFragment extends BaseFragment implements View.OnClickListener {

    public TextView resultados;
    public int puntos, enemigos = 0;

    public FinalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_final, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_aceptar).setOnClickListener(this);
        resultados = view.findViewById(R.id.text_resultados);

        resultados.setText("RESULTADOS:\nHas eliminado "+ enemigos+" enemigos.\nHas conseguido "+puntos+" puntos.");
    }

    @Override
    public void onClick(View v) {
        ((ScaffoldActivity)getActivity()).exitFinal();
    }
}
