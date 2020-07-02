package home.thiru.templatemessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterForHistory extends BaseAdapter {
    ArrayList<String> template;
    ArrayList<String> date;
    ArrayList<String> time;
    ArrayList<String> name;
    ArrayList<String> send;
    LayoutInflater inflater;
    Context context;

    public AdapterForHistory(Context context, ArrayList<String> template, ArrayList<String> date, ArrayList<String> time, ArrayList<String> name, ArrayList<String> send) {
        this.name = name;
        this.template = template;
        this.send = send;
        this.date = date;
        this.time = time;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.template.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterForHistory.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.adapter_for_history, parent, false);
            mViewHolder = new AdapterForHistory.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (AdapterForHistory.MyViewHolder)convertView.getTag();
        }

        mViewHolder.template.setText((CharSequence)this.template.get(this.template.size() - position - 1));
        mViewHolder.name.setText((CharSequence)this.name.get(this.template.size() - position - 1));
        mViewHolder.date.setText((CharSequence)this.date.get(this.template.size() - position - 1));
        mViewHolder.time.setText((CharSequence)this.time.get(this.template.size() - position - 1));
        mViewHolder.send.setText((CharSequence)this.send.get(this.template.size() - position - 1));
        return convertView;
    }

    private class MyViewHolder {
        TextView template;
        TextView name;
        TextView date;
        TextView time;
        TextView send;

        public MyViewHolder(View item) {
            this.template = (TextView)item.findViewById(R.id.template_name);
            this.name = (TextView)item.findViewById(R.id.name);
            this.date = (TextView)item.findViewById(R.id.date);
            this.time = (TextView)item.findViewById(R.id.time);
            this.send = (TextView)item.findViewById(R.id.inform);
        }
    }
}
