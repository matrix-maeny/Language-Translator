package com.matrix_maeny.languagetranslator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.matrix_maeny.languagetranslator.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String[] fromLanguages = {"From", "English", "Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan"
            , "Czech","Chinese", "Danish", "German", "Greek", "Hindi", "Italian", "Japanese", "Kannada", "Korean", "Marathi", "Persian","Portuguese", "Russian"
            ,"Romanian", "Spanish", "Telugu","Tamil","Turkish","Thai", "Urdu","Ukrainian","Vietnamese", "Welsh"};

    private final String[] toLanguages = {"To", "English", "Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan"
            , "Czech","Chinese", "Danish", "German", "Greek", "Hindi", "Italian", "Japanese", "Kannada", "Korean", "Marathi", "Persian","Portuguese", "Russian"
            ,"Romanian", "Spanish", "Telugu","Tamil","Turkish","Thai", "Urdu","Ukrainian","Vietnamese", "Welsh"};

    private final static int MIC_REQUEST_CODE = 1;
    int languageCode = 0, fromLanguageCode = 0, toLanguageCode = 0;
    private String sourceText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromSpinnerAdapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_item, fromLanguages);
        fromSpinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.fromSpinner.setAdapter(fromSpinnerAdapter);

        binding.toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toSpinnerAdapter = new ArrayAdapter(MainActivity.this, R.layout.spinner_item, toLanguages);
        toSpinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.toSpinner.setAdapter(toSpinnerAdapter);


        binding.translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSourceText() && checkFromLanguage() && checkToLanguage()) {
                    binding.sourceText.setText(sourceText);
                    binding.sourceText.setSelection(sourceText.length());
                    translateText();
                }
            }
        });

        binding.inputMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text");

                try {
                    startActivityForResult(intent, MIC_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Contract(pure = true)
    private int getLanguageCode(@NonNull String language) {

        int lCode = 0;

        switch (language) {
            case "English":
                lCode = FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                lCode = FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                lCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                lCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bulgarian":
                lCode = FirebaseTranslateLanguage.BG;
                break;
            case "Bengali":
                lCode = FirebaseTranslateLanguage.BN;
                break;
            case "Catalan":
                lCode = FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                lCode = FirebaseTranslateLanguage.CS;
                break;
            case "Danish":
                lCode = FirebaseTranslateLanguage.DA;
                break;
            case "German":
                lCode = FirebaseTranslateLanguage.DE;
                break;
            case "Greek":
                lCode = FirebaseTranslateLanguage.EL;
                break;
            case "Welsh":
                lCode = FirebaseTranslateLanguage.CY;
                break;
            case "Hindi":
                lCode = FirebaseTranslateLanguage.HI;
                break;
            case "Urdu":
                lCode = FirebaseTranslateLanguage.UR;
                break;
            case "Telugu":
                lCode = FirebaseTranslateLanguage.TE;
                break;
            case "Spanish":
                lCode = FirebaseTranslateLanguage.ES;
                break;
            case "Persian":
                lCode = FirebaseTranslateLanguage.FA;
                break;
            case "Italian":
                lCode = FirebaseTranslateLanguage.IT;
                break;
            case "Japanese":
                lCode = FirebaseTranslateLanguage.JA;
                break;
            case "Kannada":
                lCode = FirebaseTranslateLanguage.KN;
                break;
            case "Korean":
                lCode = FirebaseTranslateLanguage.KO;
                break;
            case "Marathi":
                lCode = FirebaseTranslateLanguage.MR;
                break;
            case "Russian":
                lCode = FirebaseTranslateLanguage.RU;
                break;
            case "Romanian":
                lCode = FirebaseTranslateLanguage.RO;
                break;
            case "Portuguese":
                lCode = FirebaseTranslateLanguage.PT;
                break;
            case "Tamil":
                lCode = FirebaseTranslateLanguage.TA;
                break;
            case "Thai":
                lCode = FirebaseTranslateLanguage.TH;
                break;
            case "Turkish":
                lCode = FirebaseTranslateLanguage.TR;
                break;
            case "Ukrainian":
                lCode = FirebaseTranslateLanguage.UK;
                break;
            case "Vietnamese":
                lCode = FirebaseTranslateLanguage.VI;
                break;
            case "Chinese":
                lCode = FirebaseTranslateLanguage.ZH;
                break;



        }

        return lCode;
    }

    private boolean checkSourceText() {
        sourceText = "";

        try {
            sourceText = Objects.requireNonNull(binding.sourceText.getText()).toString().trim();
            if (!sourceText.equals("")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
        return false;

    }

    private boolean checkFromLanguage() {

        if (fromLanguageCode == 0) {
            Toast.makeText(this, "Please select languages", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkToLanguage() {

        if (toLanguageCode == 0) {
            Toast.makeText(this, "Please select languages", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    private void translateText() {
        binding.resultText.setText("Downloading Language...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode).build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                binding.resultText.setText("Translating...");
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        binding.resultText.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "fail to translate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "fail to download Model: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MIC_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.sourceText.setText(list.get(0));
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // do something
        startActivity(new Intent(MainActivity.this,AboutActivity.class));
        return super.onOptionsItemSelected(item);
    }
}