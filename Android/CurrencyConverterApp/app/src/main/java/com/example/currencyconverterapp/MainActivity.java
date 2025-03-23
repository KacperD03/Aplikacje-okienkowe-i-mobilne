package com.example.currencyconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText amountInput = findViewById(R.id.amountInput);
        Spinner sourceCurrencySpinner = findViewById(R.id.sourceCurrencySpinner);
        Spinner targetCurrencySpinner = findViewById(R.id.targetCurrencySpinner);
        Button convertButton = findViewById(R.id.convertButton);
        TextView resultTextView = findViewById(R.id.resultTextView);

        String[] currencies = {"USD", "EUR", "PLN", "GBP"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceCurrencySpinner.setAdapter(adapter);
        targetCurrencySpinner.setAdapter(adapter);

        HashMap<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.85);
        rates.put("PLN", 4.0);
        rates.put("GBP", 0.75);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sourceCurrency = sourceCurrencySpinner.getSelectedItem().toString();
                String targetCurrency = targetCurrencySpinner.getSelectedItem().toString();
                String amountText = amountInput.getText().toString();

                if (!amountText.isEmpty()) {
                    double amount = Double.parseDouble(amountText);
                    double sourceRate = rates.get(sourceCurrency);
                    double targetRate = rates.get(targetCurrency);
                    double result = (amount / sourceRate) * targetRate;

                    resultTextView.setText(String.format("Result: %.2f %s", result, targetCurrency));
                } else {
                    resultTextView.setText("Please enter a valid amount.");
                }
            }
        });
    }
}
