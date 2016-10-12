package examproject.group22.roominator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;

public class SignUpActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    ImageView avatar;
    Button createBtn;
    EditText name;
    EditText password;
    DatabaseService db;
    Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        avatar = (ImageView)findViewById(R.id.avatar);
        createBtn = (Button)findViewById(R.id.singUp_button);
        name = (EditText)findViewById(R.id.signup_name_txt);
        password = (EditText)findViewById(R.id.singUp_password_txt);
        db = DatabaseService.getInstance(getApplicationContext());
        if(getIntent().getExtras()!=null){
            name.setText(getIntent().getStringExtra("pass"));
            password.setText(getIntent().getStringExtra("name"));
        }

    }
   // https://developer.android.com/training/camera/photobasics.html
    public void onClickTakePicture(View view){
        Intent  takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePhotoIntent,REQUEST_IMAGE_CAPTURE);
        }
    }
    public void onClickCreate(View view){
            String n = name.getText().toString();
            String p = password.getText().toString();
            Bitmap img = photo;
            User u = new User(n, p, img);
            db.post_NewUser(u);


            Intent returnIntent = new Intent();
            returnIntent.putExtra("name", name.getText().toString());
            returnIntent.putExtra("password", password.getText().toString());
            setResult(RESULT_OK,returnIntent);
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            photo = (Bitmap) extras.get("data");
            avatar.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
  /* // http://stackoverflow.com/questions/20700181/convert-imageview-in-bytes-android
    public byte[] convertImgToByteArray(ImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap b = imageView.getDrawingCache();
        ByteArrayOutputStream BAPS = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,100, BAPS);
        byte[] byteArray = BAPS.toByteArray();
        return  byteArray;
    }*/
}
