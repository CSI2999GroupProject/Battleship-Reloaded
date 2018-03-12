package sophomoreproject.battleship;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements
        View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button tutorialButton = (Button) findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);

        Log.i(getLocalClassName(), "oncREATE");
    }

    public void onDebugClick(View view)
    {
        startActivity(new Intent(this, Debug.class));
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tutorialButton:
                startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
                break;
        }
    }
}
