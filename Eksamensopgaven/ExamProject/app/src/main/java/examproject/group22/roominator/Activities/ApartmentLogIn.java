package examproject.group22.roominator.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class ApartmentLogIn extends AppCompatActivity {

    EditText name;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_log_in);
        name = (EditText)findViewById(R.id.apartment_name_txt);
        password = (EditText)findViewById(R.id.apartment_password_txt);


    }
    public void onClicLogInApartment(View view){
        if()
    }
}
