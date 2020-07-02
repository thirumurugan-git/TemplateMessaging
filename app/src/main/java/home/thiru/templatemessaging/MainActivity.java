package home.thiru.templatemessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Action act = new Action();

    public MainActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.request_permission();
    }

    private void request_permission() {
        int permission = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        if (permission == 0) {
            this.make_list();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 0);
        }

    }

    private void make_list() {
        ArrayList<String> template = this.act.get_files(this);
        ListView listView = (ListView)this.findViewById(R.id.lstview);
        CustomAdapter adapt = new CustomAdapter(this, template, this.act);
        this.act.adapter = adapt;
        listView.setAdapter(adapt);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int permission;
        switch(item.getItemId()) {
            case R.id.add:
                permission = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
                if (permission == 0) {
                    this.act.promt(this, "Save As");
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                }
                return true;
            case R.id.demo:
                int permit = ContextCompat.checkSelfPermission(this, "android.permission.INTERNET");
                if (permit == 0) {
                    Intent demo = new Intent(this, Demo.class);
                    this.startActivity(demo);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.INTERNET"}, 4);
                }
                return true;
            case R.id.history:
                Intent intent = new Intent(this, hisory.class);
                this.startActivity(intent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                this.make_list();
            } else {
                this.request_permission();
            }
        }

        if (requestCode == 1 && grantResults[0] == 0) {
            this.act.promt(this, "Save As");
        }

        if (requestCode == 4 && grantResults[0] == 0) {
            Intent demo = new Intent(this, Demo.class);
            this.startActivity(demo);
        }

    }
}
