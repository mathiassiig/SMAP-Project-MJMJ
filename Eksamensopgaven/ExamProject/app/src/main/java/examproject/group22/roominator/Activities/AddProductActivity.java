package examproject.group22.roominator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.NumberPicker;

import examproject.group22.roominator.R;

public class AddProductActivity extends AppCompatActivity {

    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        numberPicker = (NumberPicker)findViewById(R.id.addproduct_NumberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(99);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Opdater databasen!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
             //   Intent addIntent = new Intent(AddProductActivity.this, OverviewActivity.class);
             //   startActivity(addIntent);
            }
        });
    }

}
