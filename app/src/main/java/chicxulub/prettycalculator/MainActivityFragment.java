package chicxulub.prettycalculator;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {

    public TextView outputAlpha, outputBeta;
    public static final String TAG = MainActivityFragment.class.getName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // insert numbers into text view
        ArrayList<Button> numberButtons = getAllNumberButtons(rootView);
        int i = 0;
        outputAlpha = (TextView)rootView.findViewById(R.id.calculatorOutputBottom);
        for(Button button: numberButtons) {
            // add button onclick listeners
            addNumPadClickListener(button, i, outputAlpha);
            i++;
        }

        // add click events for operations
        Button b = (Button)rootView.findViewById(R.id.backspace);
        Button c = (Button)rootView.findViewById(R.id.clear);

        c.setOnClickListener(this);
        b.setOnClickListener(this);

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

    public void addNumPadClickListener(Button button, int value, TextView appendTo) {
        final int i = value;
        final TextView a = appendTo;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, String.valueOf(i));
                a.append(String.valueOf(i));
            }
        });
    }

    public void onClick(View v){
        String text = outputAlpha.getText().toString();
        switch(v.getId()) {
            case R.id.backspace:
                if(text.length() > 0) {
                    outputAlpha.setText(text.substring(0, text.length() - 1));
                }
                break;
            case R.id.clear:
                if(text.length() > 0) {
                    outputAlpha.setText("");
                }
                break;
        }
    }


}
