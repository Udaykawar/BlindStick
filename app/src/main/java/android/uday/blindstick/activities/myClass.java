package android.uday.blindstick.activities;

import android.os.Bundle;
import android.uday.blindstick.R;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class myClass extends AppCompatActivity {
    static int i = 0;
    TextView tvshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shubham);
        int a = getIntent().getIntExtra("add",0);
        tvshow = (TextView)findViewById(R.id.textview_s);
        tvshow.setText(""+a);
    }
}
