package lj.gomusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PersionalActivity extends AppCompatActivity {
    private TextView username;
    private TextView phonenum;
    private TextView text;
    private TextView sex;
    private Button submit;
    private Button cancel;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persional);

        username = (TextView)findViewById(R.id.text_name);
        phonenum = (TextView)findViewById(R.id.text_num);
        sex = (TextView)findViewById(R.id.text_sex);
        text = (TextView)findViewById(R.id.text_text);
        submit = (Button)findViewById(R.id.submit);
        cancel = (Button)findViewById(R.id.cancel);
        back = (ImageView) findViewById(R.id.back);

        username.setText(Personalinfo.getUsername());
        phonenum.setText(Personalinfo.getPhonenum());
        sex.setText(Personalinfo.getSex());
        text.setText(Personalinfo.getText());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersionalActivity.this,ResetPwdActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersionalActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersionalActivity.this,MusicListActivity.class);
                startActivity(intent);
            }
        });

    }
}
