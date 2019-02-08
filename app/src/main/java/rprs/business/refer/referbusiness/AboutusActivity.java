package rprs.business.refer.referbusiness;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AboutusActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        textView=(TextView)findViewById(R.id.textView24);

        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}
