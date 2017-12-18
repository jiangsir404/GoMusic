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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.email_login_form);
        //  linearLayout.getBackground().setAlpha(100);//0~255透明度值 0：全透明；255不透明

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Button loginButton = (Button) findViewById(R.id.button_add);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = editText1.getText().toString(); //获取手机号
                String s2 = editText2.getText().toString(); //获取密码
                if (s1.length() > 0 && s2.length() > 0) { //判断不为空
                    sendRequestWithHttpClient(); //进入登录的验证程序程序
                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "请填写用户名密码", Toast.LENGTH_SHORT);
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
                    ContentValues postParams = new ContentValues(); //设置post值
                    //要传递的参数
                    postParams.put("phonenum", editText1.getText().toString());
                    postParams.put("password", editText2.getText().toString());
                    String s=  UrlManager.httpUrlConnectionPost("/work/login.php",postParams); //调用URLManger的post登录
                    if(s.indexOf("login success") != -1) { //登录成功，跳转到MusicListActivity
                        try {

                            String res = UrlManager.httpUrlConnectionPost("/work/index.php",postParams);  //解析用户信息
                            JSONObject r = new JSONObject(res);
                            JSONArray array = r.getJSONArray("infos");
                            JSONObject obj = array.getJSONObject(0);
                            Personalinfo perinfo = new Personalinfo();
                            perinfo.setUsername(obj.getString("username"));
                            perinfo.setPhonenum(obj.getString("phonenum"));
                            perinfo.setText(obj.getString("text"));
                            perinfo.setSex(obj.getString("sex"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LoginActivity.this,MusicListActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else{
                        Looper.prepare();
                        Toast toast= Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT);
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