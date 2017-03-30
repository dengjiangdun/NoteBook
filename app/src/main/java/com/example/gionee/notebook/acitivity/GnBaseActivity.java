package com.example.gionee.notebook.acitivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.gionee.notebook.util.StyleUtil;

/**
 * Created by johndon on 2/25/17.
 */

abstract  public class GnBaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle();
        setContentView(getLayoutId());
        initView();
        initListener();
        initData();
    }

    protected void initView(){}

    protected void initListener(){}

    protected void initData(){}

    protected void setStyle(){
        StyleUtil.createTheme(this);
    }

    abstract protected  int getLayoutId();

    protected void showShortToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void showLongToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }


}
