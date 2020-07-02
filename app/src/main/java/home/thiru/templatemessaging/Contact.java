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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.io.Serializable;
import java.util.ArrayList;

public class Contact extends AppCompatActivity {
    public String template_name;
    Action action = new Action();
    String message;
    ArrayList<String> template_values;
    String changer = "#";
    String file_name;
    ArrayList<String> copy_template;
    AdapterForContact adapter;
    ListView listView;
    DB snappdb;
    TextView textView;
    ArrayList<String> contactlist = new ArrayList();
    ArrayList<String> contact_id = new ArrayList();

    public Contact() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_contact);
        Intent get = this.getIntent();
        Bundle bundle = get.getExtras();
        this.template_name = bundle.getString("file");
        this.file_name = this.action.folder_storage(this) + "/" + this.template_name + ".txt";
        this.find_changer();
        this.getting_database();
        this.getting_id_name();
        this.textView = (TextView)this.findViewById(R.id.textView);
        this.listView = (ListView)this.findViewById(R.id.listview);
        this.textView.setText(this.template_name);
        this.adapter = new AdapterForContact(this.message, this, this.contactlist, this.contact_id, this.template_name);
        this.listView.setAdapter(this.adapter);
    }

    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, req, data);
        if (req == 1) {
            this.getting_id_name();
            this.adapter.myList = this.contactlist;
            this.adapter.id = this.contact_id;
            this.adapter.notifyDataSetChanged();
        }

    }

    private void getting_id_name() {
        try {
            this.snappdb = DBFactory.open(this, this.template_name, new Kryo[0]);
            this.contact_id.clear();
            this.contactlist.clear();
            String[] var1 = this.snappdb.findKeys("#");
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String key = var1[var3];
                this.contact_id.add(key);
                String[] for_name = (String[])this.snappdb.getArray(key, String.class);
                this.contactlist.add(for_name[0]);
            }

            this.snappdb.close();
        } catch (SnappydbException var6) {
            var6.printStackTrace();
        }

    }

    private void getting_database() {
        try {
            this.snappdb = DBFactory.open(this, this.template_name, new Kryo[0]);
            this.copy_template = this.template_values;
            this.copy_template.add(0, this.changer + "name");
            this.copy_template.add(1, this.changer + "number");
            if (this.snappdb.exists("default")) {
                this.change_the_exist_values();
            } else {
                String[] temp = new String[this.copy_template.size()];
                int cnt = 0;

                while(true) {
                    if (cnt >= this.copy_template.size()) {
                        this.snappdb.put("default", temp);
                        break;
                    }

                    temp[cnt] = (String)this.copy_template.get(cnt);
                    ++cnt;
                }
            }

            this.snappdb.close();
        } catch (SnappydbException var3) {
            var3.printStackTrace();
        }

    }

    private void change_the_exist_values() {
        boolean hasChanged = false;

        try {
            ArrayList<Integer> changed_values = new ArrayList();
            String[] exist_string = (String[])this.snappdb.getArray("default", String.class);
            ArrayList<String> exist_array = new ArrayList();

            int i;
            for(i = 0; i < exist_string.length; ++i) {
                exist_array.add(exist_string[i]);
            }

            int index;
            for(i = 0; i < this.copy_template.size(); ++i) {
                if (exist_array.contains(this.copy_template.get(i))) {
                    index = exist_array.indexOf(this.copy_template.get(i));
                    changed_values.add(index);
                    if (index != i) {
                        hasChanged = true;
                    }
                } else {
                    changed_values.add(-1);
                    hasChanged = true;
                }
            }

            if (hasChanged) {
                String[] var13 = this.snappdb.findKeys("#");
                index = var13.length;

                for(int var7 = 0; var7 < index; ++var7) {
                    String key = var13[var7];
                    String[] temp = (String[])this.snappdb.getArray(key, String.class);
                    ArrayList<String> temp_array = new ArrayList();

                    for(i = 0; i < changed_values.size(); ++i) {
                        if ((Integer)changed_values.get(i) == -1) {
                            temp_array.add("");
                        } else {
                            temp_array.add(temp[(Integer)changed_values.get(i)]);
                        }
                    }

                    this.snappdb.del(key);
                    this.snappdb.put(key, (Serializable[])temp_array.toArray(new String[temp_array.size()]));
                }

                this.snappdb.del("default");
                this.snappdb.put("default", (Serializable[])this.copy_template.toArray(new String[this.copy_template.size()]));
            }
        } catch (SnappydbException var12) {
            var12.printStackTrace();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menuforcontact, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addcontact:
                this.request_to_add_contact();
                return true;
            case R.id.allsend:
                this.request_to_send_message();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void request_to_send_message() {
        int permission2 = ContextCompat.checkSelfPermission(this, "android.permission.SEND_SMS");
        if (permission2 == 0) {
            this.adapter.send_to_all();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.SEND_SMS"}, 1);
        }

    }

    private void request_to_add_contact() {
        int permission1 = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS");
        if (permission1 == 0) {
            this.getting_contacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS"}, 0);
        }

    }

    private void getting_contacts() {
        Intent contact = new Intent(this, ContactList.class);
        contact.putExtra("template", this.template_name);
        contact.putExtra("size", this.template_values.size());
        this.startActivityForResult(contact, 1);
    }

    private void find_changer() {
        String text = this.action.readFile(this.file_name);
        this.message = text;
        ArrayList<Integer> changer_index = this.findWord(text, this.changer);
        this.template_values = this.word_saver(changer_index, text);
    }

    private ArrayList<String> word_saver(ArrayList<Integer> changer_index, String text) {
        ArrayList<String> words = new ArrayList();

        for(int i = 0; i < changer_index.size(); ++i) {
            int increment;
            char temp;
            for(increment = 1; (Integer)changer_index.get(i) + increment != text.length() && (temp = text.charAt((Integer)changer_index.get(i) + increment)) != ' ' && temp != '.' && temp != '\n' && temp != '#'; ++increment) {
            }

            String SubString = text.substring((Integer)changer_index.get(i) + 1, (Integer)changer_index.get(i) + increment);
            if (!words.contains(SubString) && increment != 1) {
                words.add(SubString);
            }
        }

        return words;
    }

    public ArrayList<Integer> findWord(String textString, String word) {
        ArrayList<Integer> indexes = new ArrayList();

        for(int index = textString.indexOf(word); index >= 0; index = textString.indexOf(word, index + 1)) {
            indexes.add(index);
        }

        return indexes;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                this.getting_contacts();
            } else {
                this.request_to_add_contact();
            }
        }

        if (requestCode == 1) {
            if (grantResults[0] == 0) {
                this.adapter.send_to_all();
            } else {
                this.request_to_send_message();
            }
        }

        if (requestCode == 3) {
            if (grantResults[0] == 0) {
                this.adapter.send_message(this.adapter.current_position);
            } else {
                Toast.makeText(this, "Give permission to send message", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
