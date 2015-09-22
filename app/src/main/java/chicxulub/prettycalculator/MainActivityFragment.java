package chicxulub.prettycalculator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = MainActivityFragment.class.getName();
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;

    }

    public ArrayList<Button> getAllNumberButtons(View view) {
        ArrayList<Button> numPad = new ArrayList<Button>();
        for (int i = 0; i < 10; i++) {
            int id = getResources().getIdentifier("num" + i, "id", getContext().getPackageName());
            numPad.add(i, (Button) view.findViewById(id));
        }
        return numPad;
    }

    public void addNumPadClickListeners() {

    }

    public int getValuePressed() { return 0; }

}
