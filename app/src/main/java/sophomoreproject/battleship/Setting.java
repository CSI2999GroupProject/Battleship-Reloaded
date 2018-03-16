package sophomoreproject.battleship;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
/**
 * Created by ZJ on 3/16/2018.
 */
public class Setting extends Activity {
    @Override
    protected void onCreate(Bundle saveInstancestats){
        super.onCreate(saveInstancestats);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new SettingPage(this));
    }
}
