package sophomoreproject.battleship;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainMenu extends Activity implements
        View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        Button tutorialButton = (Button) findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
    }

    public void onDebugClick(View view)
    {
        Intent intent = new Intent(this, Debug.class);
        startActivity(intent);
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
