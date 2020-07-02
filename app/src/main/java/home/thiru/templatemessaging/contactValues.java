package home.thiru.templatemessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.io.Serializable;
import java.util.ArrayList;

public class contactValues extends AppCompatActivity {
    ListView listView;
    TextView t1;
    TextView t2;
    String id;
    String name;
    String number;
    String template;
    ArrayList<String> dynamic_name = new ArrayList();
    ArrayList<String> dynamic_values = new ArrayList();
    AdapterForContactValue adapterForContactValue;

    public contactValues() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_contact_values);
        Intent get = this.getIntent();
        Bundle bundle = get.getExtras();
        this.template = bundle.getString("template");
        this.id = bundle.getString("key");
        this.ready_for_the_values();
        this.listView = (ListView)this.findViewById(R.id.listforvalue);
        this.t1 = (TextView)this.findViewById(R.id.idforname);
        this.t1.setText(this.name);
        this.t2 = (TextView)this.findViewById(R.id.idfornumber);
        this.t2.setText(this.number);
        this.adapterForContactValue = new AdapterForContactValue(this, this.dynamic_name, this.dynamic_values);
        this.listView.setAdapter(this.adapterForContactValue);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menuforcontactlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.tikok:
                try {
                    DB snappydb = DBFactory.open(this, this.template, new Kryo[0]);
                    ArrayList<String> temp = this.adapterForContactValue.dynamic_value;
                    temp.add(0, this.name);
                    temp.add(1, this.number);
                    snappydb.del(this.id);
                    snappydb.put(this.id, (Serializable[])temp.toArray(new String[temp.size()]));
                    snappydb.close();
                    this.finish();
                } catch (SnappydbException var4) {
                    var4.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ready_for_the_values() {
        try {
            DB snappydb = DBFactory.open(this, this.template, new Kryo[0]);
            String[] temp_title = (String[])snappydb.getArray("default", String.class);
            String[] temp_value = (String[])snappydb.getArray(this.id, String.class);
            this.name = temp_value[0];
            this.number = temp_value[1];

            for(int i = 2; i < temp_title.length; ++i) {
                this.dynamic_name.add(temp_title[i]);
                this.dynamic_values.add(temp_value[i]);
            }

            snappydb.close();
        } catch (SnappydbException var5) {
            var5.printStackTrace();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }
}
