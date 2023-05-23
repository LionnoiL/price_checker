package ua.gaponov.pricecheker.main;

import static ua.gaponov.pricecheker.main.Helpers.IDM_OPTIONS;
import static ua.gaponov.pricecheker.main.Helpers.getOptions;
import static ua.gaponov.pricecheker.main.Helpers.useVideo;
import static ua.gaponov.pricecheker.main.Helpers.videoPauseTime;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MyReceiver myBroadcastReceiver;
    public TextView barcodeTV;
    public TextView nameTV;
    public TextView priceTV;
    public TextView stockTV;
    public Timer timer;
    public Timer timer_video;
    public ImageView imageView;
    public String barcode = "";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, IDM_OPTIONS, Menu.NONE, "Налаштування");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case IDM_OPTIONS:
                stopTimer(timer);
                stopTimer(timer_video);
                Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        stopTimer(timer);
        stopTimer(timer_video);
        getBarcode(keyCode, event);
        return false;
    }

    public void getBarcode(int keyCode, KeyEvent event){
        char unicodeChar = (char)event.getUnicodeChar();
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            barcode = barcode.replaceAll("[^A-Za-zА-Яа-я0-9]", "");
            barcodeTV.setText(barcode);

            MyReceiver.GetHttpData getHttpData = new MyReceiver.GetHttpData();
            getHttpData.barcode = barcodeTV.getText().toString();
            getHttpData.setRequestListener(() -> {
                try {
                    nameTV.setText(getHttpData.res);
                    JSONObject json = new JSONObject(getHttpData.res);
                    String productName = json.getString("name");
                    if (productName.equals("")){
                        nameTV.setText("Товар не знайдений");
                        priceTV.setText("");
                        stockTV.setText("");
                    } else {
                        nameTV.setText(productName);
                        priceTV.setText(json.getString("price") + " грн");
                        stockTV.setText(json.getString("stock"));
                    }
                    setTimer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            getHttpData.execute();
            barcode = "";
        } else {
            barcode += String.valueOf(unicodeChar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myBroadcastReceiver = new MyReceiver(MainActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("scan.rcv.message");
        registerReceiver(myBroadcastReceiver, intentFilter);
        setTimer();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helpers.context = getApplicationContext();
        getOptions();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle("Прайс-чекер");

        barcodeTV = findViewById(R.id.barcode);
        nameTV = findViewById(R.id.nameTV);
        priceTV = findViewById(R.id.priceTV);
        stockTV = findViewById(R.id.stockTV);
        imageView = findViewById(R.id.imageView);

        registerForContextMenu(imageView);

        barcode = "";
        timer_video = new Timer();
        timer_video.schedule(new MainActivity.UpdateTimeTaskVideo(), 50000);
    }

    public void stopTimer(Timer t){
        if (t!=null) {
            t.cancel();
        }
    }

    public void setTimer(){
        stopTimer(timer);
        stopTimer(timer_video);

        timer = new Timer();
        timer.schedule(new MainActivity.UpdateTimeTask(), 10000);

        timer_video = new Timer();
        timer_video.schedule(new MainActivity.UpdateTimeTaskVideo(), videoPauseTime * 1000);
    }

    class UpdateTimeTask extends TimerTask {
        public void run() {
            timerTick();
        }
    }

    private void timerTick() {
        timer.cancel();
        timer = null;
        this.runOnUiThread(doTask);
    }

    private Runnable doTask = new Runnable() {
        public void run() {
            barcodeTV.setText("");
            nameTV.setText(R.string.scan);
            priceTV.setText("");
            stockTV.setText("");
        }
    };

    class UpdateTimeTaskVideo extends TimerTask {
        public void run() {
            timerTickVideo();
        }
    }

    private void timerTickVideo() {
        stopTimer(timer_video);
        if (timer==null) {
            this.runOnUiThread(doTaskVideo);
        }
    }

    private Runnable doTaskVideo = () -> {
        if (useVideo) {
            Intent intent = new Intent(MainActivity.this, VideoActivity.class);
            startActivity(intent);
        }
    };
}
