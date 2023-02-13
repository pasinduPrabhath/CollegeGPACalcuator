package com.example.practical5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CheckBox cbPizza;
    private CheckBox cbCoffee;
    private CheckBox cbBurger;
    private  Button sbmtBtn;
    private int total = 0;
    ArrayList<String> checkedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cbPizza = findViewById(R.id.cbPizza);
        cbCoffee = findViewById(R.id.cbCoffee);
        cbBurger = findViewById(R.id.cbBurger);

        cbPizza.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                total += 50;
                checkedItems.add("Pizza Rs 50");
            } else {
                total -= 50;
                checkedItems.remove("Pizza Rs 50");
            }
        });
        cbCoffee.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                total += 120;
                checkedItems.add("Coffee Rs 120");
            } else {
                total -= 120;
                checkedItems.remove("Coffee Rs 120");
            }
        });
        cbBurger.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                total += 40;
                checkedItems.add("Burger Rs 40");
            } else {
                total -= 40;
                checkedItems.remove("Burger Rs 40");
            }
        });

    }

    public  void onSubmitButtonClicked(View view){
        sbmtBtn = (Button) findViewById(R.id.submitbtn);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Selected items");


        sbmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedItems.add("\nTotal =" + total);
                String checkedItemsString = TextUtils.join("\n", checkedItems);
                builder.setMessage(checkedItemsString);
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                checkedItems.remove("\nTotal =" + total);
            }
        });
    }
}
