package examproject.group22.roominator.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;

import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.GroceryItemModel;
import examproject.group22.roominator.Models.UserModel;
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
        final GroceryItemModel i = (GroceryItemModel)bundle.getSerializable("groceryItem");
        final UserModel u = (UserModel)bundle.getSerializable("potentialBuyer");
        TextviewProductName.setText(i.name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCurrent(i, u);
            }
        });
    }

    private void BuyCurrent(GroceryItemModel i, UserModel buyer) {
        if (editTextPrice.getText().toString().trim().length() != 0) {
            Timestamp bought = new Timestamp(new Date().getTime());
            i.buyerID = buyer.id;
            i.boughtStamp = bought;
            //EditText et = (EditText) findViewById(R.id.buyproduct_etPrice);
            i.price = Integer.parseInt(editTextPrice.getText().toString());
            DatabaseService.getInstance(getApplicationContext()).put_UpdateGrocery(i);
            finish();
        }
        else
        {
            Toast.makeText(this, "Indtast pris", Toast.LENGTH_LONG).show(); //TODO: Externalize

        }
    }



}
