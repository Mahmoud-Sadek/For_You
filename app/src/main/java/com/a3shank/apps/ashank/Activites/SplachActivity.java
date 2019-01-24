package com.a3shank.apps.ashank.Activites;

import android.content.Intent;

import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.admin.AdminPanelActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.auth.FirebaseAuth;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


public class SplachActivity extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!

    private FirebaseAuth auth;

    @Override
    public void initSplash(ConfigSplash configSplash) {

            /* you don't have to override every property */
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.primaryColor); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.splash_icon);//or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
//        configSplash.setPathSplash(); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(5); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.white); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(100);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
//        configSplash.setTitleFont("arabic2.otf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {

        //transit to another activity here
        //or do whatever you want
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            SplachActivity.this.startActivity(new Intent(SplachActivity.this, LoginActivity.class));
            SplachActivity.this.finish();
        } else {
            final Intent mainIntent = new Intent(SplachActivity.this, AdminPanelActivity.class);
            SplachActivity.this.startActivity(mainIntent);
            SplachActivity.this.finish();
        }
    }
}

/*
public class SplachActivity extends AppCompatActivity
//        implements Animation.AnimationListener
{

    private ViewGroup mContentView;
    // Animation
    Animation animFadein;
    private boolean isRunning;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        mContentView = (ViewGroup) findViewById(R.id.activity_splach);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToFullScreen();
            }
        });

        // load the animation
//        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.animation);
//        animFadein.setAnimationListener(this);
//        animFadein.setDuration(2000);
//        imageView.startAnimation(animFadein);
//        startActivity(new Intent(SplachActivity.this, LoginActivity.class));

//        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
//        pGif.setImageResource( R.drawable.source);

        this.isRunning = true;
//        startSplash();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() == null) {
                    SplachActivity.this.startActivity(new Intent(SplachActivity.this, LoginActivity.class));
                    SplachActivity.this.finish();
                } else {
                    final Intent mainIntent = new Intent(SplachActivity.this, MainActivity.class);
                    SplachActivity.this.startActivity(mainIntent);
                    SplachActivity.this.finish();
                }
            }
        }, 5000);
    }

    private void setToFullScreen() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setToFullScreen();
    }
//
//    @Override
//    public void onAnimationStart(Animation animation) {
//    }
//
//    @Override
//    public void onAnimationEnd(Animation animation) {
//        startActivity(new Intent(SplachActivity.this, MainActivity.class));
//    }

//    @Override
//    public void onAnimationRepeat(Animation animation) {
//    }
//
//    */
/* renamed from: com.glam.SplashScreen.1 *//*

//    class SplashScreen implements Runnable {
//
//        */
/* renamed from: com.glam.SplashScreen.1.1 *//*

//        class SplashScreen2 implements Runnable {
//            SplashScreen2() {
//            }
//
//            public void run() {
//                SplachActivity.this.doFinish();
//            }
//        }
//
//        SplashScreen() {
//        }
//
//        ImageView imageView;
//
//        public void run() {
//            try {
//
//                imageView = (ImageView) findViewById(R.id.img);
//                animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
//                        R.anim.scale_up_fast);
//
//                animFadein.setDuration(3000);
//                animFadein.setFillAfter(true);
//                imageView.startAnimation(animFadein);
//                Thread.sleep(3010);
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                SplachActivity.this.runOnUiThread(new SplashScreen2());
//            }
//        }
//    }
//
//    private void startSplash() {
//        new Thread(new SplashScreen()).start();
//    }
//
//    private synchronized void doFinish() {
//        if (this.isRunning) {
//            this.isRunning = false;
//            Intent i = new Intent(this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode != 4) {
//            return super.onKeyDown(keyCode, event);
//        }
//        this.isRunning = false;
//        finish();
//        return true;
//    }
}*/
