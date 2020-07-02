package home.thiru.templatemessaging;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.esotericsoftware.kryo.Kryo;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Action {
    CustomAdapter adapter;

    public Action() {
    }

    public String folder_storage(Context context) {
        File file = new File(context.getFilesDir().getPath() + "/template messaging/");
        if (!file.exists()) {
            file.mkdir();
        }

        return file.getPath();
    }

    public boolean fileisthere(String file, Context context) throws IOException {
        File fl = new File(this.folder_storage(context) + "/" + file + ".txt");
        return fl.exists();
    }

    public void createfile(String file, Context context) throws IOException {
        File fl = new File(this.folder_storage(context) + "/" + file + ".txt");
        fl.createNewFile();
    }

    public void promt(final Context context, String msg) {
        final String[] value = new String[1];
        final EditText edtText = new EditText(context);
        final AlertDialog dialog = (new Builder(context)).setTitle(msg).setMessage("Name of the template").setView(edtText).setPositiveButton("ok", (OnClickListener)null).setNegativeButton("cancel", (OnClickListener)null).show();
        Button button = dialog.getButton(-1);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                value[0] = edtText.getText().toString();
                dialog.dismiss();

                try {
                    if (Action.this.fileisthere(value[0], context)) {
                        Action.this.promt(context, "file exist!");
                    } else if (!value[0].contains(".") && !value[0].contains("/") && !value[0].contains("*") && !value[0].contains(" ") && !value[0].contains("\"") && !value[0].contains("'")) {
                        if (value[0] != "" && value[0] != null && !value[0].isEmpty()) {
                            Action.this.call_intent(context, value[0]);
                        } else {
                            Action.this.promt(context, "Give a name");
                        }
                    } else {
                        Action.this.promt(context, "Dont use / . * \" 'and space");
                    }
                } catch (IOException var3) {
                    var3.printStackTrace();
                }

            }
        });
    }

    public void call_intent(Context context, String value) {
        try {
            this.createfile(value, context);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        this.refresh();
        Intent add = new Intent(context, add.class);
        add.putExtra("file", value);
        context.startActivity(add);
    }

    public ArrayList<String> get_files(Context context) {
        ArrayList<String> files = new ArrayList();
        File file = new File(this.folder_storage(context));
        File[] list = file.listFiles();
        File[] var5 = list;
        int var6 = list.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            File f = var5[var7];
            String name = f.getName();
            if (name.endsWith(".txt")) {
                files.add(name.replace(".txt", ""));
            }
        }

        return files;
    }

    public void promt_for_rename(final Context context, String msg, final String name) {
        final String[] value = new String[1];
        final EditText edtText = new EditText(context);
        edtText.setText(name);
        final AlertDialog dialog = (new Builder(context)).setTitle(msg).setMessage("Name of the template").setView(edtText).setPositiveButton("ok", (OnClickListener)null).setNegativeButton("cancel", (OnClickListener)null).show();
        Button button = dialog.getButton(-1);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                value[0] = edtText.getText().toString();
                dialog.dismiss();

                try {
                    if (!value[0].contains(".") && !value[0].contains("/") && !value[0].contains("*") && !value[0].contains(" ") && !value[0].contains("'") && !value[0].contains("\"")) {
                        if (value[0] != null && value[0] != "" && !value[0].isEmpty()) {
                            if (Action.this.fileisthere(value[0], context)) {
                                Action.this.promt_for_rename(context, "file exist!", name);
                            } else {
                                Action.this.rename(context, name, value[0]);
                            }
                        } else {
                            Action.this.promt_for_rename(context, "Give a name", name);
                        }
                    } else {
                        Action.this.promt_for_rename(context, "Dont use / . * \" 'and space", name);
                    }
                } catch (IOException var3) {
                    var3.printStackTrace();
                }

            }
        });
    }

    public void rename(Context context, String from, String to) {
        File dir = new File(this.folder_storage(context));
        File f = new File(dir, from + ".txt");
        if (dir.exists() && f.exists()) {
            File t = new File(dir, to + ".txt");
            f.renameTo(t);
            this.rename_the_db(context, from, to);
            this.refresh();
        }

    }

    private void rename_the_db(Context context, String from, String to) {
        try {
            DB from_db = DBFactory.open(context, from, new Kryo[0]);
            int count = 0;
            String[] defaults = new String[0];
            ArrayList<String[]> temp = new ArrayList();
            boolean exist = false;
            if (from_db.exists("count")) {
                exist = true;
                count = from_db.getInt("count");
                defaults = (String[])from_db.getArray("default", String.class);
                String[] var9 = from_db.findKeys("#");
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String key = var9[var11];
                    temp.add(from_db.getArray(key, String.class));
                }
            }

            from_db.destroy();
            DB to_db = DBFactory.open(context, to, new Kryo[0]);
            if (exist && count != 0) {
                to_db.put("default", defaults);
                Iterator var15 = temp.iterator();

                while(var15.hasNext()) {
                    String[] value = (String[])var15.next();
                    ++count;
                    to_db.put("#" + String.valueOf(count), value);
                }

                to_db.putInt("count", count);
            }

            to_db.close();
        } catch (SnappydbException var13) {
            var13.printStackTrace();
            Toast.makeText(context, "error", 0).show();
        }

    }

    public void delete(final Context context, final String file) {
        final AlertDialog dialog = (new Builder(context)).setTitle("Do you want to delete?").setMessage("Template: " + file).setPositiveButton("ok", (OnClickListener)null).setNegativeButton("cancel", (OnClickListener)null).show();
        Button button = dialog.getButton(-1);
        button.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Action.this.delete_confirm(context, file);
            }
        });
    }

    public void delete_confirm(Context context, String file) {
        File fi = new File(this.folder_storage(context) + "/" + file + ".txt");
        if (fi.exists()) {
            fi.delete();

            try {
                DB snappydb = DBFactory.open(context, file, new Kryo[0]);
                snappydb.destroy();
            } catch (SnappydbException var5) {
                var5.printStackTrace();
                Toast.makeText(context, "error occur", 0).show();
            }

            this.refresh();
        } else {
            Toast.makeText(context, "file error", 0).show();
        }

    }

    public void move_to_contact(Context context, String file) {
        if (this.check_file(context, file)) {
            this.refresh();
            Intent contact = new Intent(context, Contact.class);
            contact.putExtra("file", file);
            context.startActivity(contact);
        } else {
            Toast.makeText(context, "file error", 0).show();
        }

    }

    public boolean check_file(Context context, String file) {
        File fi = new File(this.folder_storage(context) + "/" + file + ".txt");
        return fi.exists();
    }

    public void refresh() {
        this.adapter.notifyDataSetChanged();
    }

    public String readFile(String file_name) {
        File fileEvents = new File(file_name);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));

            String line;
            while((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            br.close();
        } catch (IOException var6) {
        }

        String result = text.toString();
        return result;
    }
}
