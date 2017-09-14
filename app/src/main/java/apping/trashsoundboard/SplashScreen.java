package apping.trashsoundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class SplashScreen extends AppCompatActivity {

    String PREF = "TrashSoundBoard";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splashscreen);

        splash = (ImageView) findViewById(R.id.img_splash);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splashscreen);
        int height = (bitmap.getHeight() * width / bitmap.getWidth());
        Bitmap scale = Bitmap.createScaledBitmap(bitmap, width, height, true);

        splash.setImageBitmap(scale);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Questo permesso è indispensabile per l'esecuzione della app", Toast.LENGTH_LONG).show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_ID_MULTIPLE_PERMISSIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            new CreateXML(getApplicationContext()).createFile();
            startApp();

        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            new CreateXML(getApplicationContext()).createFile();
            startApp();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i=0; i<grantResults.length; i++){
            Log.d("Perm", "premission: " + permissions[i] + "result:" + grantResults);
            if (grantResults[i]==PERMISSION_GRANTED){
                Log.d("Perm", "permesso accettato");
                new CreateXML(getApplicationContext()).createFile();
                startActivity(new Intent(SplashScreen.this, MyActivity.class));
            } else {
                Log.d("Perm", "permesso rifiutato");
                Toast.makeText(this, "Questo permesso è indispensabile per l'esecuzione della app", Toast.LENGTH_LONG).show();

            }


        }

    }

    void startApp(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, MyActivity.class));
            }
        }, 1300);
    }


}
