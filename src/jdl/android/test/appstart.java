package jdl.android.test;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.WindowManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.view.Window;

public class appstart extends Activity{
    private SharedPreferences first;
    Editor et1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState); 
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.welcom);

        new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                 
                    Intent intent = new Intent (appstart.this,MainActivity.class);           
                        startActivity(intent);  
                    appstart.this.finish();
                }
            }, 3000);
    }
}
