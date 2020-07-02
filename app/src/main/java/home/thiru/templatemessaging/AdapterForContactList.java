package home.thiru.templatemessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterForContactList extends BaseAdapter {
    ArrayList<String> names;
    ArrayList<String> numbers;
    ArrayList<Integer> contacts_position;
    LayoutInflater inflater;
    Context context;

    public AdapterForContactList(Context context, ArrayList<String> names, ArrayList<String> numbers, ArrayList<Integer> contacts_position) {
        this.names = names;
        this.contacts_position = contacts_position;
        this.numbers = numbers;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.names.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        AdapterForContactList.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.activity_contact_list, parent, false);
            mViewHolder = new AdapterForContactList.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (AdapterForContactList.MyViewHolder)convertView.getTag();
        }

        mViewHolder.textView1.setText((CharSequence)this.names.get(position));
        mViewHolder.textView2.setText((CharSequence)this.numbers.get(position));
        if (this.contacts_position.contains(position)) {
            mViewHolder.btn1.setImageResource(R.drawable.star);
        } else {
            mViewHolder.btn1.setImageResource(R.drawable.starborder);
        }

        mViewHolder.textView1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterForContactList.this.perform_click_action(position);
            }
        });
        mViewHolder.textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterForContactList.this.perform_click_action(position);
            }
        });
        mViewHolder.btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterForContactList.this.perform_click_action(position);
            }
        });
        return convertView;
    }

    private void perform_click_action(int position) {
        if (this.contacts_position.contains(position)) {
            this.contacts_position.remove(new Integer(position));
        } else {
            this.contacts_position.add(position);
        }

        this.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class MyViewHolder {
        TextView textView1;
        TextView textView2;
        ImageButton btn1;

        public MyViewHolder(View item) {
            this.textView1 = (TextView)item.findViewById(R.id.texter1);
            this.textView2 = (TextView)item.findViewById(R.id.texter2);
            this.btn1 = (ImageButton)item.findViewById(R.id.star);
        }
    }
}
