package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import giovannilenguito.co.uk.parceldelivery.R;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRegister(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
