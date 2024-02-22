package in.innovateria.whatsappstatus.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import in.innovateria.whatsappstatus.R;

public class ShowImageActivity extends AppCompatActivity {
    ImageView imageView, playButtonFull, pauseButtonFull;
    VideoView mainVideoPlay;
    Button downloadButton, shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // making notification bar transparent
        changeStatusBarColor();

        Intent intent = getIntent();
        Uri source = null;

        imageView = findViewById(R.id.imageView);
        playButtonFull = findViewById(R.id.playButtonFull);
        pauseButtonFull = findViewById(R.id.pauseButtonFull);
        mainVideoPlay = findViewById(R.id.mainVideoPlay);
        downloadButton = findViewById(R.id.downloadButton);
        shareButton = findViewById(R.id.shareButton);

        if(intent.hasExtra("Data"))
        {
            source = (Uri) Objects.requireNonNull(intent.getExtras()).get("Data");
            Glide.with(this).load(source).into(imageView);
        }
        if(source.toString().endsWith(".mp4"))
        {
            playButtonFull.setVisibility(View.VISIBLE);

            mainVideoPlay.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            pauseButtonFull.setVisibility(View.GONE);

            mainVideoPlay.setVideoURI(source);

            playButtonFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainVideoPlay.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    playButtonFull.setVisibility(View.GONE);
                    mainVideoPlay.start();
                    pauseButtonFull.setVisibility(View.VISIBLE);
                }
            });

            pauseButtonFull.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playButtonFull.setVisibility(View.VISIBLE);
                    mainVideoPlay.pause();
                    pauseButtonFull.setVisibility(View.GONE);

                    mainVideoPlay.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        }
        else
        {
            playButtonFull.setVisibility(View.INVISIBLE);
        }


        Uri finalSource = source;
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Main program.
                Toast.makeText(ShowImageActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();
                startCopyFiles(finalSource);
                Toast.makeText(ShowImageActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowImageActivity.this, "You can share wherever you want", Toast.LENGTH_SHORT).show();

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri uri = finalSource;

                try {
                    if(uri.toString().endsWith(".jpg"))
                        shareImage(uri.getPath());
                    else
                        shareVideo(uri.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void shareImage(String filepath) throws FileNotFoundException {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/*");

        File filesDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filepath);
        String mediaPath = MediaStore.Images.Media.insertImage(getContentResolver(), filepath, filesDir.getName(), null);
        Uri uri = Uri.parse(mediaPath);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(shareIntent);
        }
        catch (android.content.ActivityNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void shareVideo(String filepath){
        Intent intent=new Intent("android.intent.action.SEND");
        intent.setType("video/mp4");
        intent.putExtra("android.intent.extra.STREAM", Uri.parse(filepath));
        startActivity(Intent.createChooser(intent,"share"));

    }

    public static Uri startCopyFiles(@NonNull Uri finalSource)
    {
        String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        File file = new File(destination);

        String lastName = (new File(Objects.requireNonNull(finalSource.getPath()))).getName();

        if(!file.exists())
            file.mkdir();

        destination = destination + lastName;
        File[] allfiles = file.listFiles();
        boolean found=true;
        for(File data:allfiles)
        {
            if(data.getName().equals(lastName)){
                found = false;
                break;
            }
        }
        if(found)
            copyFile(finalSource.getPath(),destination);

        return Uri.fromFile(new File(destination));
    }

    public static void copyFile(String inputPath, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);


            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            Log.d("Aryan","Error ");
        } catch (Exception e) {
            Log.d("Aryan", Objects.requireNonNull(e.getMessage()));
        }
    }

    public final void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}