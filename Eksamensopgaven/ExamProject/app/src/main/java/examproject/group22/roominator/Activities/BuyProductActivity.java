package examproject.group22.roominator.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import examproject.group22.roominator.R;

public class BuyProductActivity extends AppCompatActivity {


    private TextView TextviewProductName;
    private EditText editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextviewProductName = (TextView) findViewById(R.id.buyproduct_txtName);
        editTextPrice = (EditText) findViewById(R.id.buyproduct_etPrice);


        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("productname")!= null)
        {
            TextviewProductName.setText("Produkt: " + bundle.getString("productname"));
        }else{
            Toast.makeText(this, "Send data med fra liste",Toast.LENGTH_SHORT).show();}


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFromDB();
            }
        });
    }

    private void RemoveFromDB() {
        Toast.makeText(this, "Remove from DB", Toast.LENGTH_LONG).show();
        //   Intent addIntent = new Intent(BuyProductActivity.this, OverviewActivity.class);
        //   startActivity(addIntent);
    }



}
