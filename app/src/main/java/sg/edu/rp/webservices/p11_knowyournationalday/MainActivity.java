package sg.edu.rp.webservices.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvQns;
    ArrayList<String> alQns = new ArrayList<>();
    ArrayAdapter<String> aaQns;
    String accessCode = "";
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvQns = (ListView) findViewById(R.id.lvQns);
        aaQns = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alQns);

        alQns.add("Singapore National Day is on 9 Aug");
        alQns.add("Singapore is 52 years old");
        alQns.add("Theme is '#OneNationalTogether'");

        lvQns.setAdapter(aaQns);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int count = alQns.size();
        String message = "";
        for (int x = 0; x < count; x++) {
            String content = alQns.get(x);
            message += content;
        }

        if (item.getItemId() == R.id.action_send) {
            String[] options = new String[]{"Email", "SMS"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String finalMessage = message;
            builder.setTitle("Select the way to enrich your friend")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_TEXT, finalMessage);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setType("vnd.android-dir/mms-sms");
                                intent.putExtra("sms_body", finalMessage);
                                startActivity(intent);
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_quiz) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout quiz = (LinearLayout) inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup) quiz.findViewById(R.id.radioGroup1);
            final RadioGroup rg2 = (RadioGroup) quiz.findViewById(R.id.radioGroup2);
            final RadioGroup rg3 = (RadioGroup) quiz.findViewById(R.id.radioGroup3);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter")
                    .setView(quiz)
                    .setNegativeButton("I dont know lah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            final int selectedButtonId = rg1.getCheckedRadioButtonId();
                            final int selectedButtonId2 = rg2.getCheckedRadioButtonId();
                            final int selectedButtonId3 = rg3.getCheckedRadioButtonId();

                            if (selectedButtonId == (R.id.radio1No)) {
                                score++;
                            }
                            if (selectedButtonId2 == (R.id.radio2Yes)) {
                                score++;
                            }
                            if (selectedButtonId3 == (R.id.radio3Yes)) {
                                score++;
                            }
                            Toast.makeText(MainActivity.this, "Score: " + score, Toast.LENGTH_SHORT).show();
                            score = 0;
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Not Really", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("accessCode", accessCode);
        prefEdit.commit();
    }

    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String code = prefs.getString("accessCode", "");
        accessCode = code;

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =
                (LinearLayout) inflater.inflate(R.layout.passphrase, null);
        final EditText etPassphrase = (EditText) passPhrase.findViewById(R.id.editTextPassPhrase);

        if (accessCode.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(passPhrase)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String passcode = "738964";
                            if (etPassphrase.getText().toString().equals(passcode)) {
                                accessCode = etPassphrase.getText().toString();
                            } else {
                                Toast.makeText(MainActivity.this, "Wrong Access Code", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("No access code", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(MainActivity.this, "This app requires an access code", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
