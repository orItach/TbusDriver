package project.java.tbusdriver.Controller.Activitys;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import project.java.tbusdriver.Controller.Fragments.Auth;
import project.java.tbusdriver.Controller.Fragments.Login;
import project.java.tbusdriver.R;
import project.java.tbusdriver.RWSetting;

public class LoginAuth extends AppCompatActivity implements Login.OnFragmentInteractionListener,Auth.OnFragmentInteractionListener
{
    Context context=this;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Login loginFragment;
    Auth authFragment;
    RWSetting rwSettings;
    String phone;

    public String  getPhone()
    {
        return  phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_auth);
        fragmentManager = getSupportFragmentManager();
        loginFragment= new Login();
        authFragment= new Auth();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.login_authContainer,loginFragment);
        //fragmentTransaction.replace(R.id.fragment_container, loginFragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClickLogin(View v)
    {
        //accessToData(1);
        int GET_MY_PERMISSION = 1;
        if(ContextCompat.checkSelfPermission(LoginAuth.this,Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginAuth.this,
                    Manifest.permission.READ_SMS)){
                /* do nothing*/
            }
            else{

                ActivityCompat.requestPermissions(LoginAuth.this,
                        new String[]{Manifest.permission.READ_SMS},GET_MY_PERMISSION);
            }
        }
        loginFragment.onClickLogin(v);
    }

    public void onClickAuth(View v)
     {
         authFragment.onClickAuth(v);
     }

    @Override
    public void onAuthFragmentInteraction(int fragmentReturn) {
        Toast.makeText(this, "work", Toast.LENGTH_LONG).show();
        Intent i =new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void OnLoginFragmentInteractionListener(int what) {
        EditText Phone=(EditText) findViewById(R.id.phone);
        phone=Phone.getText().toString();
        Bundle bundle=new Bundle();
        bundle.putString("PHONE",phone);
        authFragment.setArguments(bundle);
        rwSettings= RWSetting.getInstance(LoginAuth.this);

        rwSettings.setStringSetting(phone,"phone");
        EditText UserName=(EditText)findViewById(R.id.userName);
        Phone.setText("");
        UserName.setText("");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_authContainer, authFragment);
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.login_authContainer);

        if (currentFragment instanceof Auth) {
            super.onBackPressed();
        }
        if (currentFragment instanceof Login) {
            ActivityCompat.finishAffinity(LoginAuth.this);

            //android.os.Process.killProcess(android.os.Process.myPid());
            this.finish();
        }
    }
}
