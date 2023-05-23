package ua.gaponov.pricecheker.main;

import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class VideoActivity extends AppCompatActivity {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_video);

        File dir = new File(getSDcardPath() + "/Movies/");
        File f = new File(dir.toString());
        File[] list = f.listFiles((dir1, name) -> name.toLowerCase().endsWith(".mp4"));

        if (list.length > 0) {
            int n = (int) Math.floor(Math.random() * list.length);
            System.out.println(list[n].getPath());
            String videoSource = list[n].getPath();
            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            videoView.setOnCompletionListener(mp -> finish());

            videoView.setOnTouchListener((view, event) -> {
                finish();
                return true;
            });

            videoView.setVideoPath(videoSource);
            videoView.setMediaController(null);
            videoView.start(); // начинаем воспроизведение автоматически
        } else {
            finish();
        }
    }

    private String getSDcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }
}
