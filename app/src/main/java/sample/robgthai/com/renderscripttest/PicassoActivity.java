package sample.robgthai.com.renderscripttest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class PicassoActivity extends ActionBarActivity {

    private static final String IMAGE_URL = "http://i.imgur.com/DvpvklR.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_picasso);


        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Loading image");

        Picasso picasso = Picasso.with(this);
        picasso.setLoggingEnabled(true);
        picasso.load(IMAGE_URL).into(imageView);
    }
}
