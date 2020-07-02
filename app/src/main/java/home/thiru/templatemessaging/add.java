package home.thiru.templatemessaging;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class add extends AppCompatActivity {
    public String file_name;
    Action action = new Action();
    String template_name;
    TextView textView;
    EditText editText;

    public add() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add);
        Intent get = this.getIntent();
        Bundle bundle = get.getExtras();
        this.template_name = bundle.getString("file");
        this.file_name = this.action.folder_storage(this) + "/" + this.template_name + ".txt";
        this.textView = (TextView)this.findViewById(R.id.text);
        this.textView.setText(this.template_name);
        this.editText = (EditText)this.findViewById(R.id.edit_text);
        this.editText.setText(this.action.readFile(this.file_name));
    }

    public void save(View v) {
        String text = this.editText.getText().toString();
        this.writeToFile(text);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void writeToFile(String data) {
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(new File(this.file_name));
            stream.write(data.getBytes());
            stream.close();
        } catch (FileNotFoundException var4) {
            var4.printStackTrace();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
