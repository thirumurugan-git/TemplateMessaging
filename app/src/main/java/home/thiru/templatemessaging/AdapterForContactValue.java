package home.thiru.templatemessaging;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterForContactValue extends BaseAdapter {
    ArrayList<String> dynamic_name;
    ArrayList<String> dynamic_value;
    LayoutInflater inflater;
    Context context;

    public AdapterForContactValue(Context context, ArrayList<String> dynamic_name, ArrayList<String> dynamic_value) {
        this.dynamic_name = dynamic_name;
        this.dynamic_value = dynamic_value;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.dynamic_name.size();
    }

    public Object getItem(int position) {
        return 0;
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        AdapterForContactValue.MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.adapter_for_values, parent, false);
            mViewHolder = new AdapterForContactValue.MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (AdapterForContactValue.MyViewHolder)convertView.getTag();
        }

        mViewHolder.textView.setText((CharSequence)this.dynamic_name.get(position));
        mViewHolder.editText.setText((CharSequence)this.dynamic_value.get(position));
        mViewHolder.editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                AdapterForContactValue.this.dynamic_value.remove(position);
                AdapterForContactValue.this.dynamic_value.add(position, value);
            }
        });
        return convertView;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class MyViewHolder {
        TextView textView;
        EditText editText;

        public MyViewHolder(View item) {
            this.textView = (TextView)item.findViewById(R.id.settingvalue);
            this.editText = (EditText)item.findViewById(R.id.gettingvalue);
        }
    }
}
