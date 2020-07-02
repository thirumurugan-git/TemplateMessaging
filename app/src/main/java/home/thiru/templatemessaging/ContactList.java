package home.thiru.templatemessaging;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.util.ArrayList;
import java.util.Iterator;

public class ContactList extends AppCompatActivity {
    ListView listView;
    String template;
    Integer size;
    AdapterForContactList adapterForContactList;
    ArrayList<String> contact_names = new ArrayList();
    ArrayList<String> contact_numbers = new ArrayList();
    ArrayList<Integer> contact_position = new ArrayList();
    ArrayList<String> template_contact = new ArrayList();
    ArrayList<String> template_numbers = new ArrayList();

    public ContactList() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_contact_list);
        Intent intent = this.getIntent();
        this.template = intent.getExtras().getString("template");
        this.size = intent.getExtras().getInt("size");
        this.listView = (ListView)this.findViewById(R.id.listforcontact);
        this.getting_template_contact();
        this.getting_contat();
        this.adapterForContactList = new AdapterForContactList(this, this.contact_names, this.contact_numbers, this.contact_position);
        this.listView.setAdapter(this.adapterForContactList);
    }

    private void getting_template_contact() {
        try {
            DB snappydb = DBFactory.open(this, this.template, new Kryo[0]);
            String[] var2 = snappydb.findKeys("#");
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String key = var2[var4];
                String[] temp = (String[])snappydb.getArray(key, String.class);
                this.template_contact.add(temp[0]);
                this.template_numbers.add(temp[1]);
            }

            snappydb.close();
        } catch (SnappydbException var7) {
            var7.printStackTrace();
        }

    }

    public void getting_contat() {
        ContentResolver cr = this.getContentResolver();
        Cursor cur = cr.query(Contacts.CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
        if (cur.getCount() > 0) {
            while(true) {
                if (!cur.moveToNext()) {
                    cur.close();
                    cur.deactivate();
                    break;
                }

                String id = cur.getString(cur.getColumnIndex("_id"));
                String name = cur.getString(cur.getColumnIndex("display_name"));
                if (cur.getString(cur.getColumnIndex("has_phone_number".trim())).equalsIgnoreCase("1") && name != null) {
                    Cursor pCur = cr.query(Phone.CONTENT_URI, (String[])null, "contact_id = ?", new String[]{id}, (String)null);

                    while(true) {
                        if (pCur.moveToNext()) {
                            String PhoneNumber = pCur.getString(pCur.getColumnIndex("data1"));
                            PhoneNumber = PhoneNumber.replaceAll("-", "");
                            if (PhoneNumber.trim().length() >= 10) {
                                PhoneNumber = PhoneNumber.substring(PhoneNumber.length() - 10);
                            }

                            if (this.template_contact.contains(name) && this.template_numbers.contains(PhoneNumber)) {
                                continue;
                            }

                            int pos;
                            for(pos = 0; pos < this.contact_names.size() && name.compareTo((String)this.contact_names.get(pos)) > 0; ++pos) {
                            }

                            this.contact_names.add(pos, name);
                            this.contact_numbers.add(pos, PhoneNumber);
                        }

                        pCur.close();
                        pCur.deactivate();
                        break;
                    }
                }
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menuforcontactlist, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.tikok:
                this.make_contact_in_template();
                Intent set = new Intent();
                this.setResult(1, set);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void make_contact_in_template() {
        try {
            DB snappydb = DBFactory.open(this, this.template, new Kryo[0]);
            if (!snappydb.exists("count")) {
                snappydb.putInt("count", 0);
            }

            int count = snappydb.getInt("count");
            Iterator var3 = this.adapterForContactList.contacts_position.iterator();

            while(var3.hasNext()) {
                int position = (Integer)var3.next();
                ++count;
                String[] temp = new String[this.size + 2];
                temp[0] = (String)this.contact_names.get(position);
                temp[1] = (String)this.contact_numbers.get(position);

                for(int i = 2; i < temp.length; ++i) {
                    temp[i] = "";
                }

                snappydb.put("#" + String.valueOf(count), temp);
            }

            snappydb.putInt("count", count);
            snappydb.close();
        } catch (SnappydbException var7) {
            var7.printStackTrace();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }
}
