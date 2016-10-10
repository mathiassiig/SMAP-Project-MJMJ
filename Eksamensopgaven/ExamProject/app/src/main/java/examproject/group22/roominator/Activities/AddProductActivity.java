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

import examproject.group22.roominator.R;

public class AddProductActivity extends AppCompatActivity {

    private NumberPicker numberPicker;
    private EditText EditTextProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditTextProduct = (EditText)findViewById(R.id.addproduct_etName);
        numberPicker = (NumberPicker)findViewById(R.id.addproduct_NumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProductToDB();
            }
        });
    }

    private void saveProductToDB() {
        if (EditTextProduct.getText().toString().trim().length() != 0) {
            Toast.makeText(this, "Save to DB", Toast.LENGTH_LONG).show();
            //   Intent addIntent = new Intent(AddProductActivity.this, OverviewActivity.class);
            //   startActivity(addIntent);
        }
        else{
            Toast.makeText(this, "Udfyld produkt", Toast.LENGTH_LONG).show();

        }
    }

}
