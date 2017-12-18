package lj.gomusic;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ResetPwdActivity extends AppCompatActivity {
    private TextView phonenum;
    private TextView oldpass;
    private TextView newpass;
    private TextView reppass;
    private Button button;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        phonenum = (TextView)findViewById(R.id.resetpwd_edit_iphone);
        oldpass = (TextView)findViewById(R.id.resetpwd_edit_pwd_old);
        newpass = (TextView)findViewById(R.id.resetpwd_edit_pwd_new);
        reppass = (TextView)findViewById(R.id.resetpwd_edit_pwd_check);
        button = (Button)findViewById(R.id.submit);
        back = (Button)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPwdActivity.this,PersionalActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = phonenum.getText().toString();
                String s2 = oldpass.getText().toString();
                String s3 = newpass.getText().toString();
                String s4 = reppass.getText().toString();
                if (s1.length() > 0 && s2.length() > 0 && s3.length() >0 && s4.length() > 0) {
                    sendRequestWithHttpClient(); //登录程序
                } else {
                    Toast toast = Toast.makeText(ResetPwdActivity.this, "请填写用户名密码", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
                    postParams.put("phonenum", phonenum.getText().toString());
                    postParams.put("oldpass", oldpass.getText().toString());
                    postParams.put("newpass", newpass.getText().toString());
                    postParams.put("repeatpass", reppass.getText().toString());
                    String s=  UrlManager.httpUrlConnectionPost("/work/resetpwd.php",postParams); //调用URLManger的post登录
                    if(s.indexOf("reset success") != -1) { //登录成功，跳转到MusicListActivity
                        Looper.prepare();
                        Toast toast= Toast.makeText(ResetPwdActivity.this, "重置成功", Toast.LENGTH_SHORT);
                        toast.show();
                        Looper.loop();
                        Intent intent = new Intent(ResetPwdActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Looper.prepare();
                        Toast toast= Toast.makeText(ResetPwdActivity.this, "重置失败", Toast.LENGTH_SHORT);
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
