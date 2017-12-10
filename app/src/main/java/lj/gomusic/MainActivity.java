package lj.gomusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.email_login_form);
        //  linearLayout.getBackground().setAlpha(100);//0~255透明度值 0：全透明；255不透明

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        if(bundle!=null) {
            String name = bundle.getString("name");
            String password = bundle.getString("password");
            editText1.setText(name);
            editText2.setText(password);
        }
        Button loginButton = (Button) findViewById(R.id.button_add);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                if(s1.length()>0 && s2.length()>0)
                {
                    sendRequestWithHttpClient(); //登录程序
                }
                else {
                    Toast toast= Toast.makeText(MainActivity.this, "请填写用户名密码", Toast.LENGTH_SHORT);
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
                    postParams.put("accountid", editText1.getText().toString());
                    postParams.put("password", editText2.getText().toString());
                    String s=  UrlManager.httpUrlConnectionPost("/work/login.php",postParams); //调用URLManger的post登录
                    if(s.startsWith("success")) { //登录成功，跳转到MusicListActivity
                        Intent intent = new Intent(MainActivity.this,MusicListActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }else  if(s.startsWith("fail")) {
                        Looper.prepare();
                        Toast toast= Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}