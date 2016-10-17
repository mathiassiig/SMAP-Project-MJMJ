package examproject.group22.roominator.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;
//import examproject.group22.roominator.DatabaseService.LocalBinder;

/**
 * A login screen that offers login via email/password.
 * implements LoaderCallbacks<Cursor>
 */
public class LoginActivity extends AppCompatActivity
{

    private static final int SIGUN_REQUEST_CODE = 1;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private EditText txtUsername;
    private EditText txtPassword;
    private DatabaseService db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_USER_AUTHENTICATION));
        load_from_sp();
        //load_from_sp();

    }


    private void load_from_sp()
    {
        pref = getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        prefEditor = pref.edit();
        if(pref.getString("name","default") !="default")
        {
            String tryusername = pref.getString("name", "default");
            String trypassword = pref.getString("password", "default");
            tryLoginLogic(tryusername, trypassword);
        }
    }

    private void save_to_sp(User u)
    {
        prefEditor.putString("name",u.name);
        prefEditor.putString("password", u.password);


        //prefEditor.putBoolean("isloggedin", true);
        prefEditor.apply();
    }


    public void tryLogin(View view)
    {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if(username.equals(""))
            LoginError(getString(R.string.error_login_apartmentShortUser));
        else if(password.equals(""))
            LoginError(getString(R.string.error_login_apartmentShortPass));
        else
            tryLoginLogic(username, password);
    }

    private void tryLoginLogic(String username, String password)
    {
        db.get_checkPassWithUsername(username, password);
    }

    private void LoginError(String error)
    {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            User u = (User)intent.getSerializableExtra("User");
            if(u != null)
            {
                save_to_sp(u);
                Intent loggedInIntent = new Intent(LoginActivity.this, ApartmentLogIn.class);
                loggedInIntent.putExtra("User", u);
                //save_to_sp(u);
                startActivity(loggedInIntent);
                finish();
            }
            else
                LoginError("Username and password did not match, perhaps the user doesn't exist. ");
        }
    };
    public void signUp(View view){
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
        signUpIntent.putExtra("name",txtUsername.getText());
        signUpIntent.putExtra("pass",txtPassword.getText());
        startActivityForResult(signUpIntent,SIGUN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SIGUN_REQUEST_CODE){
            if(resultCode==RESULT_OK) {
                tryLoginLogic(data.getStringExtra("name"), data.getStringExtra("password"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

