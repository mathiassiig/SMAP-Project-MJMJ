package examproject.group22.roominator.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class ApartmentLogIn extends AppCompatActivity {

    EditText name;
    EditText password;
    DatabaseService db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_log_in);
        name = (EditText)findViewById(R.id.apartment_name_txt);
        password = (EditText)findViewById(R.id.apartment_password_txt);
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_APARTMENT_AUTHENTICATION));


    }
    public void onClicLogInApartment(View view){
        String username = name.getText().toString();
        String upassword = password.getText().toString();
        if(username.equals(""))
            LoginError("Username must be at least 1 character");
        else if(upassword.equals(""))
            LoginError("Password must be at least 1 character");
        else
            db.get_CheckPassWithApartmentName(name.getText().toString(), password.getText().toString());
    }

    public void LoginError(String error)
    {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null)
            {
                Intent loggedInIntent = new Intent(ApartmentLogIn.this, OverviewActivity.class);
                int id = intent.getIntExtra("apartmentID", 0);
                loggedInIntent.putExtra("apartmentID", id);
                startActivity(loggedInIntent);
                finish();
            }else{
                makePopMessage();
            }
        }
    };
    public void makePopMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApartmentLogIn.this);
        builder.setTitle("New Apartment?"); //TODO: Externalize
        builder.setMessage("Apartment not found, create a new one with this name and password?"); //TODO: Externalize
        builder.setPositiveButton("Create", null); //TODO: Externalize
        builder.setNegativeButton("Cancel", null); //TODO: Externalize
        builder.show();
    }
}
