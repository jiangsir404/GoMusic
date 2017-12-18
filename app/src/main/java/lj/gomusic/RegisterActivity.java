package lj.gomusic;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText phone;
    private EditText password;
    private EditText repeatPassword;
    private Button btsubmit;
    private Button btback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phone = (EditText)findViewById(R.id.edit_phone);
        username = (EditText)findViewById(R.id.edit_name);
        password = (EditText)findViewById(R.id.edit_password);
        repeatPassword = (EditText)findViewById(R.id.edit_repeat_password);
        btsubmit = (Button)findViewById(R.id.submit);
        btback = (Button)findViewById(R.id.back);
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = phone.getText().toString();
                String s2 = username.getText().toString();
                String s3 = password.getText().toString();
                String s4 = repeatPassword.getText().toString();
                if (s1.length() > 0 && s2.length() > 0 && s3.length() >0 && s4.length() > 0) {
                    sendRequestWithHttpClient(); //登录程序
                } else {
                    Toast toast = Toast.makeText(RegisterActivity.this, "请填写用户名密码", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ContentValues postParams = new ContentValues();
                    //要传递的参数
                    postParams.put("phonenum", phone.getText().toString());
                    postParams.put("username", username.getText().toString());
                    postParams.put("password", password.getText().toString());
                    postParams.put("repeat_password", repeatPassword.getText().toString());
                    String s=  UrlManager.httpUrlConnectionPost("/work/register.php",postParams); //调用URLManger的post登录
                    if(s.indexOf("register success") != -1) { //登录成功，跳转到MusicListActivity
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Looper.prepare();
                        Toast toast= Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了

    }

}
