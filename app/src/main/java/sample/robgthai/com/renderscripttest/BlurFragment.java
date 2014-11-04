package sample.robgthai.com.renderscripttest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link BlurFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlurFragment extends Fragment {

    private static final String IMAGE_URL = "http://material-design.storage.googleapis.com/publish/v_1/quantumexternal/0Bx4BSt6jniD7TDlCYzRROE84YWM/materialdesign_introduction.png";

    private Bitmap bitmap;
    private ImageView imageView;
    private SeekBar seekbar;

    private TextView textView;

    private BluringAsyncTask bluringAsyncTask;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BlurFragment.
     */
    public static BlurFragment newInstance() {
        BlurFragment fragment = new BlurFragment();
        return fragment;
    }

    public BlurFragment() {
    }

    Target imageLoadingTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("Picasso", "Loading success: " + from.name());
            textView.setText("Image loaded");
            BlurFragment.this.bitmap = bitmap;
            seekbar.setEnabled(true);
            displayImageWithBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("Picasso", "Loading failed");
            textView.setText("Loading failed");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d("Picasso", "Prepare loading ...");
            seekbar.setEnabled(false);
        }
    };

    private void displayImageWithBitmap(Bitmap bmp) {
        imageView.setImageBitmap(bmp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blur, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Loading image");

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("Blurring", "Blurring: " + (bluringAsyncTask != null ? bluringAsyncTask.getStatus(): "null"));
                if(bluringAsyncTask != null && bluringAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                    bluringAsyncTask.cancel(true);
                }

                float safeRadius = (float) progress + 1;

                textView.setText(String.format("Blurring at %s radius.", safeRadius));
                bluringAsyncTask = new BluringAsyncTask(safeRadius);
                bluringAsyncTask.execute(bitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Picasso picasso = Picasso.with(getActivity());
        picasso.setLoggingEnabled(true);
        picasso.load(IMAGE_URL).into(imageLoadingTarget);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class BluringAsyncTask extends AsyncTask<Bitmap, Void, Bitmap> {
        float blurRadius = 3.0f;

        public BluringAsyncTask(float radius) {
            this.blurRadius = radius;
        }

        public void setBlurRadius(float radius) {
            this.blurRadius = radius;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {

            Bitmap bitmap = params[0];
            return blurThisImage(bitmap);
        }

        private Bitmap blurThisImage(Bitmap bitmap) {
            Bitmap newBitmap = createEmptyBitmap(bitmap.getWidth(), bitmap.getHeight());
            final RenderScript rs = RenderScript.create(getActivity());
            final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped( rs, input.getType() );

            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4(rs) );
            script.setRadius( blurRadius );
            script.setInput( input );
            script.forEach( output );
            output.copyTo( newBitmap );

            return newBitmap;
        }

        private Bitmap createEmptyBitmap(int width, int height) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            return Bitmap.createBitmap(width, height, conf);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            displayImageWithBitmap(bitmap);
        }
    }

}
