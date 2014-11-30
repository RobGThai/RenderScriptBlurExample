package sample.robgthai.com.renderscripttest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * to handle interaction events.
 * Use the {@link sample.robgthai.com.renderscripttest.PicassoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PicassoFragment extends Fragment {

    private static final String IMAGE_URL = "http://i.imgur.com/DvpvklR.png";

    private Bitmap bitmap;
    private ImageView imageView;

    private TextView textView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BlurFragment.
     */
    public static PicassoFragment newInstance() {
        PicassoFragment fragment = new PicassoFragment();
        return fragment;
    }

    public PicassoFragment() {
    }

    Target imageLoadingTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("Picasso", "Loading success: " + from.name());
            textView.setText("Image loaded");
            PicassoFragment.this.bitmap = bitmap;
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
        return inflater.inflate(R.layout.fragment_picasso, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Loading image");

        Picasso picasso = Picasso.with(getActivity());
        picasso.setLoggingEnabled(true);
        picasso.load(IMAGE_URL).into(imageLoadingTarget);
//        picasso.load(IMAGE_URL).into(imageView);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
