package in.innovateria.whatsappstatus.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.File;
import java.util.ArrayList;

import in.innovateria.whatsappstatus.Adapters.RecycleAdapter;
import in.innovateria.whatsappstatus.Models.ItemModel;
import in.innovateria.whatsappstatus.R;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // making notification bar transparent
        changeStatusBarColor();

        recyclerView = findViewById(R.id.recyclerView);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        layoutManager.scrollToPosition(0);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if(Environment.isExternalStorageManager())
                checkForLocation();
            else
            {
                try
                {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 101);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 101);
                }
            }
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                checkForLocation();
            } else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Integer.parseInt(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        }
        else
            checkForLocation();
    }

    private void checkForLocation()
    {
        String address = "/Whatsapp/Media/.Statuses";

        String source = Environment.getExternalStorageDirectory().toString()+address;

        File checkAddress = new File(source);
        if(!checkAddress.exists()){
            Log.d("Aryan","First one is not present");
            address = "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
            source = Environment.getExternalStorageDirectory().toString()+address;
            checkAddress = new File(source);
        }

        if(!checkAddress.exists())
        {
            Log.d("Aryan",address);
            Toast.makeText(this,"There is some error try again",Toast.LENGTH_SHORT).show();
            return;
        }

        File sourceFile = new File(source);
        File[] name = sourceFile.listFiles();
        ArrayList<ItemModel> items = new ArrayList<ItemModel>();

        for(File child:name){
            if(child.getName().equals(".nomedia"))
                continue;
            ItemModel data = new ItemModel(Uri.fromFile(child),child);
            data.image= child.getName().contains(".jpg");
            items.add(data);
        }

        Log.d("Aryan","Size = " + items.size());
        RecycleAdapter adapter = new RecycleAdapter(this,items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            checkForLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        if (item.getItemId() == R.id.b2) {
            Intent intent2 = new Intent(Intent.ACTION_SEND);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            intent2.putExtra(Intent.EXTRA_TEXT, "https://github.com/Vnjvibhash/WhatsApp-Status-Downloader");
            intent2.setType("text/plain");
            startActivity(Intent.createChooser(intent2, "Share APP"));
        }
        return super.onOptionsItemSelected(item);
    }

    public final void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
