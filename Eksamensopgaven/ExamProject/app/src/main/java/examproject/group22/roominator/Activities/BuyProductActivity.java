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

import examproject.group22.roominator.R;

public class BuyProductActivity extends AppCompatActivity {


    private TextView TextviewProductName;
    private EditText editTextPrice;
    private NumberPicker numberpicker;
    private TextView TextviewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextviewProductName = (TextView) findViewById(R.id.buyproduct_txtName);
        TextviewTotal = (TextView) findViewById(R.id.buyproduct_textViewTotal);
        editTextPrice = (EditText) findViewById(R.id.buyproduct_etPrice);
        numberpicker = (NumberPicker)findViewById(R.id.numberPickerBuyProduct);

        numberpicker.setMinValue(1);
        numberpicker.setMaxValue(99);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("productname")!= null)
        {
            TextviewProductName.setText("Produkt: " + bundle.getString("productname"));
            numberpicker.setValue(bundle.getInt("productnumber"));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_buy);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void Calc_total(View view){
        int number = numberpicker.getValue();
        int price = Integer.parseInt(editTextPrice.getText().toString());
        int total = number*price;
        TextviewTotal.setText(total + " kr.");

    }

}
