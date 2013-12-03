package pack.woolylouie.main;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PanelActivity extends Activity {
	public PanelThread panel;
    Uri imageUri;
    final int TAKE_PICTURE = 115;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
        		WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DITHER, WindowManager.LayoutParams.FLAG_DITHER);
        //takePhoto();

        panel = new PanelThread(this);
        setContentView(panel);
        Log.d("", "onCreate");
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
    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        //Bitmap picture = (Bitmap) intent.getExtras().get("data");
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        startActivityForResult(intent, TAKE_PICTURE);
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    //Uri imageUri;
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ImageView imageView = (ImageView) findViewById(R.id.IMAGE);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

                        imageView.setImageBitmap(bitmap);
                        Toast.makeText(this, "This file: " + selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }*/
}