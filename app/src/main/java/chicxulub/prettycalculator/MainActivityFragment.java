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

    public TextView outputAlpha;
    public TextView outputBeta;
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
        outputBeta = (TextView)rootView.findViewById(R.id.calculatorOutputTop);

        for(Button button: numberButtons) {
            // add button onclick listeners
            addNumPadClickListener(button, i, outputAlpha);
            i++;
        }

        // add click events for operations
        Button b = (Button)rootView.findViewById(R.id.backspace);
        Button c = (Button)rootView.findViewById(R.id.clear);
        Button a = (Button)rootView.findViewById(R.id.add);
        Button s = (Button)rootView.findViewById(R.id.subtract);
        Button m = (Button)rootView.findViewById(R.id.multiply);
        Button d = (Button)rootView.findViewById(R.id.divide);

        b.setOnClickListener(this);
        c.setOnClickListener(this);
        a.setOnClickListener(this);
        s.setOnClickListener(this);
        m.setOnClickListener(this);
        d.setOnClickListener(this);

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
        String textAlpha = outputAlpha.getText().toString();
        String textBeta = outputBeta.getText().toString();
        String newText = "";
        switch(v.getId()) {
            case R.id.backspace:
                if(textAlpha.length()>0) {
                    outputAlpha.setText(textAlpha.substring(0, textAlpha.length() - 1));
                } else {
                    // start deleting in beta
                    outputBeta.setText(textBeta.substring(0, textBeta.length() - 1));
                }
                break;
            case R.id.clear:
                if(textAlpha.length()==0) {
                    outputBeta.setText("");
                }
                outputAlpha.setText("");
                break;
            case R.id.add:
                if(textAlpha.length()>0) {
                    if (textBeta.length() > 0) {
                        newText = textBeta;
                    }
                    newText += textAlpha + "+";
                    outputBeta.setText(newText);
                    outputAlpha.setText("");
                }
                break;
            case R.id.subtract:
                if(textAlpha.length()>0) {
                    if (textBeta.length() > 0) {
                        newText = textBeta;
                    }
                    newText += textAlpha + "-";
                    outputBeta.setText(newText);
                    outputAlpha.setText("");
                }
                break;
            case R.id.multiply:
                if(textAlpha.length()>0) {
                    if (textBeta.length() > 0) {
                        newText = textBeta;
                    }
                    newText += textAlpha + "*";
                    outputBeta.setText(newText);
                    outputAlpha.setText("");
                }
                break;
            case R.id.divide:
                if(textAlpha.length()>0) {
                    if (textBeta.length() > 0) {
                        newText = textBeta;
                    }
                    newText += textAlpha + "/";
                    outputBeta.setText(newText);
                    outputAlpha.setText("");
                }
                break;
        }
    }

    public static double eval(final String str) {
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    StringBuilder sb = new StringBuilder();
                    while ((c >= '0' && c <= '9') || c == '.') {
                        sb.append((char)c);
                        eatChar();
                    }
                    if (sb.length() == 0) throw new RuntimeException("Unexpected: " + (char)c);
                    v = Double.parseDouble(sb.toString());
                }
                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }
}
