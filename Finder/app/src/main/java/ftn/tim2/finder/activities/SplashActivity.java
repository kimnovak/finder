package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //textView = (TextView) findViewById(R.id.tv);
        imageView = (ImageView) findViewById(R.id.iv);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splashtransition);
        //textView.startAnimation(animation);
        imageView.startAnimation(animation);

        final Intent intent = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
