package sophomoreproject.battleship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getString(R.string.large_text));
    }
}
