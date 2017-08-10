package project.java.tbusdriver.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import project.java.tbusdriver.R;

public class firstTry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_try);
        View ins=(View)findViewById(R.id.allInstruction);
        ins.bringToFront();

    }
}
