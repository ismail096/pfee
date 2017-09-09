package com.navigation.drawer.activity.Activity.Administation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.navigation.drawer.activity.Activity.BaseActivity;
import com.navigation.drawer.activity.Activity.Main2Activity;
import com.navigation.drawer.activity.R;

public class AdministratorActivity extends BaseActivity implements View.OnClickListener{

    EditText pseudo = null , password = null ;
    Button Login = null ;
    public static Class nextActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_administrator, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        pseudo = (EditText) findViewById(R.id.pseudo);
        password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.login);

        Login.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v.getClass().getSimpleName().equals("Button")){
            Button b = (Button) v;
            if(b.getText().equals("Login")){
                if(pseudo.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(getApplicationContext(),"Login succeded",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(this,nextActivity));
                }
                else
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();

            }
        }
    }
}
