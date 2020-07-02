package home.thiru.templatemessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterForContact extends BaseAdapter {
    ArrayList<String> myList;
    ArrayList<String> id;
    int current_position;
    String changer = "#";
    String db;
    Activity activity;
    String message;
    LayoutInflater inflater;
    Context context;

    public AdapterForContact(String message, Context context, ArrayList<String> myList, ArrayList<String> id, String db) {
        this.myList = myList;
        this.message = message;
        this.activity = (Activity)context;
        this.id = id;
        this.db = db;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.myList.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        AdapterForContact.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.adapter_for_contact, parent, false);
            mViewHolder = new AdapterForContact.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (AdapterForContact.MyViewHolder)convertView.getTag();
        }

        mViewHolder.textView.setText((CharSequence)this.myList.get(position));
        mViewHolder.textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterForContact.this.open_contact_valuess(position);
            }
        });
        mViewHolder.btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int permission2 = ContextCompat.checkSelfPermission(AdapterForContact.this.context, "android.permission.SEND_SMS");
                if (permission2 == 0) {
                    AdapterForContact.this.send_message(position);
                } else {
                    AdapterForContact.this.current_position = position;
                    Toast.makeText(AdapterForContact.this.context, "Give permission to send", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(AdapterForContact.this.activity, new String[]{"android.permission.SEND_SMS"}, 3);
                }

            }
        });
        mViewHolder.btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final AlertDialog dialog = (new Builder(AdapterForContact.this.context)).setTitle("Do you want to remove?").setMessage("Template: " + (String)AdapterForContact.this.myList.get(position)).setPositiveButton("ok", (android.content.DialogInterface.OnClickListener)null).setNegativeButton("cancel", (android.content.DialogInterface.OnClickListener)null).show();
                Button button = dialog.getButton(-1);
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();

                        try {
                            DB snappydb = DBFactory.open(AdapterForContact.this.context, AdapterForContact.this.db, new Kryo[0]);
                            snappydb.del((String)AdapterForContact.this.id.get(position));
                            snappydb.close();
                            AdapterForContact.this.id.remove(position);
                            AdapterForContact.this.myList.remove(position);
                            AdapterForContact.this.notifyDataSetChanged();
                        } catch (SnappydbException var3) {
                            var3.printStackTrace();
                        }

                    }
                });
            }
        });
        return convertView;
    }

    public void send_message(int position) {
        boolean send = this.send_message_permitted(position);
        DBHelper dbHelper = new DBHelper(this.context);
        String currentDate = (new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())).format(new Date());
        String currentTime = (new SimpleDateFormat("HH:mm:ss", Locale.getDefault())).format(new Date());
        if (send) {
            dbHelper.insert(this.db, currentDate, currentTime, (String)this.myList.get(position), "sent");
            Toast.makeText(this.context, "send message success to " + (String)this.myList.get(position), Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.insert(this.db, currentDate, currentTime, (String)this.myList.get(position), "not sent");
            Toast.makeText(this.context, "send message not success to " + (String)this.myList.get(position), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean send_message_permitted(int position) {
        boolean send = false;

        try {
            DB snappydb = DBFactory.open(this.context, this.db, new Kryo[0]);
            String key = (String)this.id.get(position);
            String[] change_word = (String[])snappydb.getArray("default", String.class);
            String[] value = (String[])snappydb.getArray(key, String.class);
            String temp_mes = this.message;

            for(int i = 2; i < change_word.length; ++i) {
                temp_mes = temp_mes.replaceAll(this.changer + change_word[i], value[i]);
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(value[1], (String)null, temp_mes, (PendingIntent)null, (PendingIntent)null);
            snappydb.close();
            send = true;
        } catch (SnappydbException var9) {
            var9.printStackTrace();
            send = false;
        } catch (Exception var10) {
            send = false;
        }

        return send;
    }

    public void send_to_all() {
        for(int i = 0; i < this.id.size(); ++i) {
            boolean send = this.send_message_permitted(i);
            DBHelper dbHelper = new DBHelper(this.context);
            String currentDate = (new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())).format(new Date());
            String currentTime = (new SimpleDateFormat("HH:mm:ss", Locale.getDefault())).format(new Date());
            if (send) {
                dbHelper.insert(this.db, currentDate, currentTime, (String)this.myList.get(i), "sent");
            } else {
                dbHelper.insert(this.db, currentDate, currentTime, (String)this.myList.get(i), "sent");
            }
        }

        Toast.makeText(this.context, "see history sent to all performed", Toast.LENGTH_SHORT).show();
    }

    private void open_contact_valuess(int position) {
        Intent intent = new Intent(this.context, contactValues.class);
        intent.putExtra("template", this.db);
        intent.putExtra("key", (String)this.id.get(position));
        this.context.startActivity(intent);
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class MyViewHolder {
        TextView textView;
        ImageButton btn1;
        ImageButton btn2;

        public MyViewHolder(View item) {
            this.textView = (TextView)item.findViewById(R.id.textViewer);
            this.btn1 = (ImageButton)item.findViewById(R.id.send);
            this.btn2 = (ImageButton)item.findViewById(R.id.delete);
        }
    }
}
