package com.lunny.xknife;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xknife.XKnife;
import com.xknife.annotation.BindColor;
import com.xknife.annotation.BindString;
import com.xknife.annotation.BindView;
import com.xknife.annotation.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test_view)
    TextView testView;
    @BindView(R.id.test_button)
    Button testButton;
    @BindString(R.string.app_name)
    String appName;
    @BindColor(R.color.colorAccent)
    Color mainColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XKnife.bind(this);
    }

    @OnClick({R.id.test_button, R.id.toast_button})
    public void onToastClick(View view) {
        Toast.makeText(getApplicationContext(), "click toast button", Toast.LENGTH_SHORT).show();
    }

    private boolean flag;

    @OnClick(R.id.change_color_button)
    public void onChangeColorClick(View view) {
        if (flag) {
            testView.setTextColor(Color.BLACK);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                testView.setTextColor(mainColor.toArgb());
            }
        }
        flag = !flag;
    }

}
