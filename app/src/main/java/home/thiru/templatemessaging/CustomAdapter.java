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

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> myList = new ArrayList();
    LayoutInflater inflater;
    Context context;
    Action action;

    public CustomAdapter(Context context, ArrayList<String> myList, Action action) {
        this.myList = myList;
        this.action = action;
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
        CustomAdapter.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.custom_adapter, parent, false);
            mViewHolder = new CustomAdapter.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (CustomAdapter.MyViewHolder)convertView.getTag();
        }

        mViewHolder.textView.setText((CharSequence)this.myList.get(position));
        mViewHolder.textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (CustomAdapter.this.action.check_file(CustomAdapter.this.context, (String)CustomAdapter.this.myList.get(position))) {
                    CustomAdapter.this.action.call_intent(CustomAdapter.this.context, (String)CustomAdapter.this.myList.get(position));
                }

            }
        });
        mViewHolder.btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CustomAdapter.this.action.move_to_contact(CustomAdapter.this.context, (String)CustomAdapter.this.myList.get(position));
            }
        });
        mViewHolder.btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CustomAdapter.this.action.promt_for_rename(CustomAdapter.this.context, "rename", (String)CustomAdapter.this.myList.get(position));
            }
        });
        mViewHolder.btn3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CustomAdapter.this.action.delete(CustomAdapter.this.context, (String)CustomAdapter.this.myList.get(position));
            }
        });
        return convertView;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.myList = this.action.get_files(this.context);
    }

    private class MyViewHolder {
        TextView textView;
        ImageButton btn1;
        ImageButton btn2;
        ImageButton btn3;

        public MyViewHolder(View item) {
            this.textView = (TextView)item.findViewById(R.id.text);
            this.btn1 = (ImageButton)item.findViewById(R.id.right);
            this.btn2 = (ImageButton)item.findViewById(R.id.edit);
            this.btn3 = (ImageButton)item.findViewById(R.id.cancel);
        }
    }
}
