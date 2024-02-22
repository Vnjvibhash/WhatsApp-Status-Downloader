package in.innovateria.whatsappstatus.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import in.innovateria.whatsappstatus.R;

public class SplashActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    ImageView splashLogo;
    Animation animZoomIn;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // making notification bar transparent
        changeStatusBarColor();

        splashLogo = findViewById(R.id.splash_logo);
        welcome = findViewById(R.id.welcome);
        animZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        splashLogo.startAnimation(animZoomIn);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            proceedWithApp();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    private void proceedWithApp() {
        Toast.makeText(this, "Permission granted. Proceeding with the app.", Toast.LENGTH_SHORT).show();
        long timeLeftInMillis = 5000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, timeLeftInMillis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedWithApp();
            } else {
                Toast.makeText(this, "Permission denied. Closing the app.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public final void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}