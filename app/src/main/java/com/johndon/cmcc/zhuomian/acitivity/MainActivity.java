package com.johndon.cmcc.zhuomian.acitivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.johndon.cmcc.zhuomian.R;
import com.johndon.cmcc.zhuomian.view.RevealLayout;

public class MainActivity extends GnBaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private RevealLayout mRevalLayout;
    private static final String SP_NAME = "user_account";
    private static final String KEY_USERNMAE = "user_name";
    private static final String KEY_PASSWORD = "user_password";
    private static final String DEFAULT_NAME = "username";
    private static final String DEFAULT_PASSWORD = "123123";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mEtUsername.getText().toString().trim();
                final String password = mEtPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password))
                {
                    if (isCorrectUsernameAndPassword(username,password))
                    {
                        showShortToast(getResources().getString(R.string.login_successfully));
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                    else
                    {
                        showShortToast(getResources().getString(R.string.user_name_password_error));

                    }
                }
                else
                {
                    showShortToast(getResources().getString(R.string.user_name_password_empty));
                }
            }
        });
    }

    private boolean isCorrectUsernameAndPassword(String username,String password){
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_APPEND);
        String name = sharedPreferences.getString(KEY_USERNMAE,DEFAULT_NAME);
        String pwd = sharedPreferences.getString(KEY_PASSWORD,DEFAULT_PASSWORD);
        return  name.equals(username)&&pwd.equals(password);
    }

}
