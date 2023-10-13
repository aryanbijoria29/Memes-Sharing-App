package com.example.memesharingapp;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class MainActivity extends AppCompatActivity {

    private String currentMemeUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
    }

    private void loadMeme() {
        findViewById(R.id.nextButton).setEnabled(false);
        findViewById(R.id.shareButton).setEnabled(false);
        findViewById(R.id.loadingMeme).setVisibility(View.VISIBLE);
        String url = "https://meme-api.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    currentMemeUrl = response.optString("url");

                    Glide.with(this).load(currentMemeUrl).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            findViewById(R.id.loadingMeme).setVisibility(View.GONE);
                            findViewById(R.id.nextButton).setEnabled(true);
                            findViewById(R.id.shareButton).setEnabled(true);
                            return false;
                        }

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            findViewById(R.id.loadingMeme).setVisibility(View.GONE);
                            return false;
                        }
                    }).into((ImageView) findViewById(R.id.meme));
                },
                error -> {
                    findViewById(R.id.loadingMeme).setVisibility(View.GONE);
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void showNextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi, checkout this meme " + currentMemeUrl);
        startActivity(Intent.createChooser(intent, "Share this meme with"));
    }
}
