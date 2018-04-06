package sophomoreproject.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
/**
 * Created by ZJ on 4/4/2018.
 */

public class WinScreenP2 extends AppCompatActivity{
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_for_p2);
    }
    public void onDebugClick(View view)
    {
        Intent intent = new Intent(this, Debug.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
    }
}
