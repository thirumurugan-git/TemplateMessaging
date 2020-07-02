package home.thiru.templatemessaging;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class hisory extends AppCompatActivity {
    Button btn;
    ListView listView;
    DBHelper dbHelper;

    public hisory() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_hisory);
        this.dbHelper = new DBHelper(this);
        this.btn = (Button)this.findViewById(R.id.btntoclear);
        this.listView = (ListView)this.findViewById(R.id.listtohistory);
        AdapterForHistory adapterForHistory = new AdapterForHistory(this, this.dbHelper.getAlltemplate(), this.dbHelper.getAlldate(), this.dbHelper.getAlltime(), this.dbHelper.getAllname(), this.dbHelper.getAllsend());
        this.listView.setAdapter(adapterForHistory);
        this.btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                hisory.this.dbHelper.delete();
                Toast.makeText(hisory.this, "History cleared", Toast.LENGTH_SHORT).show();
                hisory.this.finish();
            }
        });
    }
}
