package examproject.group22.roominator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.R;

public class AddProductActivity extends AppCompatActivity {

    private EditText EditTextProduct;
    public int ApartmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        ApartmentID = i.getIntExtra("ApartmentID", 0);

        EditTextProduct = (EditText)findViewById(R.id.addproduct_etName);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProductToDB();
            }
        });
    }

    private void saveProductToDB() {
        if (EditTextProduct.getText().toString().trim().length() != 0)
        {
            String name = EditTextProduct.getText().toString();
            Timestamp creation = new Timestamp(new Date().getTime());
            DatabaseService.getInstance(getApplicationContext()).post_NewGrocery(name, creation, ApartmentID);
        }
        else
        {
            Toast.makeText(this, "Udfyld produkt", Toast.LENGTH_LONG).show(); //TODO: Externalize

        }
    }

}
