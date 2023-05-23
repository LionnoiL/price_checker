package ua.gaponov.pricecheker.main;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import static ua.gaponov.pricecheker.main.Helpers.getOptions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    EditText editTextIp;
    EditText editTextUser;
    EditText editTextPass;
    EditText editTextTime;
    CheckBox checkBoxVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SharedPreferences mSharedPref = getDefaultSharedPreferences(this);

        editTextIp = (EditText) findViewById(R.id.editTextIp);
        editTextUser = (EditText) findViewById(R.id.editTextUser);
        editTextPass = (EditText) findViewById(R.id.editTextPass);
        editTextTime = (EditText) findViewById(R.id.editTextTime);
        checkBoxVideo = (CheckBox) findViewById(R.id.checkBoxVideo);

        String savedHost = mSharedPref.getString("HOSTNAME", "");
        String savedUser = mSharedPref.getString("USERNAME", "");
        String savedPass = mSharedPref.getString("USERPASS", "");
        String savedTime = mSharedPref.getString("VIDEOPAUSETIME", "60");
        boolean useVideo = mSharedPref.getBoolean("USEVIDEO", false);

        editTextIp.setText(savedHost);
        editTextUser.setText(savedUser);
        editTextPass.setText(savedPass);
        editTextTime.setText(savedTime);
        checkBoxVideo.setChecked(useVideo);
    }

    public void onbtnSaveClick(View view)
    {
        SharedPreferences mSharedPref = getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEditor = mSharedPref.edit();

        mEditor.putString("HOSTNAME", editTextIp.getText().toString());
        mEditor.putString("USERNAME", editTextUser.getText().toString());
        mEditor.putString("USERPASS", editTextPass.getText().toString());
        mEditor.putBoolean("USEVIDEO", checkBoxVideo.isChecked());
        mEditor.putString("VIDEOPAUSETIME", editTextTime.getText().toString());
        mEditor.commit();

        getOptions();
        finish();
    }

    public void onbtnChancelClick(View view)
    {
        finish();
    }
}
