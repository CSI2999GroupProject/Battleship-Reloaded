package sophomoreproject.battleship;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ZJ on 3/21/2018.
 */

public class WinScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle onSaved) {
        super.onCreate(onSaved);
        setContentView(R.layout.end_game_win_screen);
    }
}
