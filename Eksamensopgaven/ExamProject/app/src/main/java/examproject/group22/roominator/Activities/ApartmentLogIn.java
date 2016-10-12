package examproject.group22.roominator.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class ApartmentLogIn extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;


    EditText name;
    EditText password;
    DatabaseService db;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_log_in);

        pref = ApartmentLogIn.this.getPreferences(MODE_PRIVATE);
        prefEditor = pref.edit();

        name = (EditText)findViewById(R.id.apartment_name_txt);
        password = (EditText)findViewById(R.id.apartment_password_txt);
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_APARTMENT_AUTHENTICATION));
        FetchUser();

    }

    @Override
    protected void onResume() {
        if(pref.getAll()!=null) {
            String apId = pref.getString("aId","");

        }
        super.onResume();
    }

    public void FetchUser()
    {
        Intent i = getIntent();
        User u = (User)i.getSerializableExtra("User");
        currentUser = u;
        //Log.v("Ah", "oh");
        //Check if user is in apartment ?
        //If yes, cool
        //If not, add him to apartment... somehow :'(
    }

    public void onClickLogInApartment(View view){
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
        public void onReceive(Context context, Intent intent)
        {
            int id = intent.getIntExtra("apartmentID", 0);
            boolean passwordOK = intent.getBooleanExtra("apartmentOK", false);
            if(id == 0) //the apartment doesn't exist
                makePopMessage();
            else if(passwordOK == false)
                LoginError("That apartment exists, but the password is wrong!"); //TODO: Externalize
            else {
                LogIn(id);
            }
        }
    };

    public void LogIn(int apartment_id)
    {
        prefEditor.putInt("aId",apartment_id);
        prefEditor.apply();
        Intent loggedInIntent = new Intent(ApartmentLogIn.this, OverviewActivity.class);
        loggedInIntent.putExtra("apartmentID", apartment_id);
        loggedInIntent.putExtra("User", currentUser);
        startActivity(loggedInIntent);
        finish();
    }

    public void makePopMessage() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ApartmentLogIn.this).create();
        alertDialog.setTitle("New Apartment?"); //TODO: Externalize
        alertDialog.setMessage("Apartment not found, create a new one with this name and password?"); //TODO: Externalize
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Create", new DialogInterface.OnClickListener(){
           public void onClick(DialogInterface dialog, int which){
               Apartment a = new Apartment(name.getText().toString(),password.getText().toString());
               db.post_NewApartment(a);
           }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }
}
