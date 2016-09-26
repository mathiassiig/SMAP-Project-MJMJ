package mathiassiig.l6_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    EditText text_place;
    EditText text_task;
    DatabaseHelper database;
    ListView listview_reminders;
    RemindersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = DatabaseHelper.getInstance(getApplicationContext());
        text_place = (EditText) findViewById(R.id.editTextPlace);
        text_task = (EditText) findViewById(R.id.editTextTask);
        listview_reminders = (ListView) findViewById(R.id.listViewReminders);
        ShowReminders();
    }

    public void ShowReminders()
    {
        ArrayList<Reminder> reminders = database.GetAllReminders();
        adapter = new RemindersAdapter(this, reminders);
        listview_reminders.setAdapter(adapter);
    }

    public void AddReminder(View view)
    {
        String task = text_task.getText().toString();
        String place = text_place.getText().toString();
        Reminder reminder = new Reminder(task, place);
        long key = database.AddReminder(reminder);
        reminder.ID = key;
        adapter.add(reminder);
    }
}
