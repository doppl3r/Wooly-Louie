package pack.virga.main;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class PanelActivity extends Activity {
	public PanelThread panel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        		WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFormat(PixelFormat.RGBA_8888);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_DITHER, WindowManager.LayoutParams.FLAG_DITHER);
        panel = new PanelThread(this);
        setContentView(panel);
        Log.d("","onCreate");
    }
	@Override
	public void onResume(){
		super.onResume();
		Log.d("","onResume");
	}
	@Override
    public void onPause(){
    	super.onPause();
    	Log.d("","onPause");
    }
	@Override
    public void onStop() {
    	finish();
		super.onStop();
		Log.d("","onStop");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("","onDestroy");
	}
}