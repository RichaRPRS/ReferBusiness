package rprs.business.refer.referbusiness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class MainActivity extends AwesomeSplash {

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.CadetBlue); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.refferblastimage); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Swing); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("Refer Business"); //change your app name here
        configSplash.setTitleTextColor(R.color.Blue);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
        //configSplash.setTitleFont("fonts/Pacifico.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        Intent intent;
        if (pref.contains("user_id")){
            DataField.user_id = pref.getString("user_id", "");
            DataField.user_name = pref.getString("user_name", "");
            DataField.user_mobile = pref.getString("mobile", "");
            intent=new Intent(getApplicationContext(),BusinessDetailsActivity.class);
        }else {
            intent=new Intent(getApplicationContext(),LoginActivity.class);
        }
        startActivity(intent);
        MainActivity.this.finish();
    }

}
