package examproject.group22.roominator.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
    EditText txtUsername;
    EditText txtPassword;
    DatabaseService db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        db = DatabaseService.getInstance(getApplicationContext());
        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever,new IntentFilter(DatabaseService.INTENT_USER_AUTHENTICATION));
    }

    private  boolean checkInternetConnection(){
        boolean isconnected;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isconnected = true;
            return isconnected;
        } else {
            isconnected=false;
            return isconnected;
        }
    }

    public void tryLogin(View view)
    {
        DatabaseService.getInstance(getApplicationContext());
        String username = txtUsername.getText().toString();
        if(username == "")
            LoginError("Username must be at least 1 character"); //TODO: Externalize
        String password = txtPassword.getText().toString();
        if(password == "")
            LoginError("Password must be at least 1 character"); //TODO:;
        db.get_checkPassWithUsername(username, password);
    }

    public void LoginError(String error)
    {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean areEqual = false;
            intent.getBooleanExtra("areEqual", areEqual);
            if(areEqual)
            {
                Intent loggedInIntent = new Intent(LoginActivity.this, OverviewActivity.class);
                loggedInIntent.putExtra("Username", txtUsername.getText().toString());
                startActivity(loggedInIntent);
                finish();
            }
        }
    };
}

