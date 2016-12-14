package com.example.lmitak.qualificationproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button button;
    private EditText etNumberInput;
    private TextView tvFirstHigher;
    private TextView tvFirstLower;
    private LinearLayout llRes;

    private int number;
    private boolean btnClickedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btn);
        etNumberInput = (EditText) findViewById(R.id.etNumberInput);
        tvFirstHigher = (TextView) findViewById(R.id.textHigherFirst);
        tvFirstLower = (TextView) findViewById(R.id.textLowerFirst);
        llRes = (LinearLayout) findViewById(R.id.llResults);

        button.setOnClickListener(this);
        btnClickedState = false;
    }

    @Override
    public void onClick(View view) {
        if (!btnClickedState){
            number = Integer.parseInt(etNumberInput.getText().toString());
            if (number < 100 || number > 10000){
                Toast.makeText(getApplicationContext(), "Broj nije u zadanom rasponu", Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<Integer> digits = new ArrayList<>();
                int tempNumber = number;

                for (int i = number < 1000 ? 2 : 3; i >= 0; i--){
                    digits.add(tempNumber / (int)Math.pow(10f, ((double) i)));
                    tempNumber = tempNumber % ((int)Math.pow(10f, ((double) i)));
                }

                ArrayList<Integer> res = getAllDigitsSolutions(digits);

                int firstHigher = findFirstHighest(res, number);
                if (firstHigher == 10_000){
                    tvFirstHigher.setText(R.string.no_higher_value);
                }else{
                    tvFirstHigher.setText(String.valueOf(firstHigher));
                }
                int firstLower = findFirstLowest(res, number);
                if (firstLower == 0){
                    tvFirstLower.setText(R.string.no_lower_value);
                }else{
                    tvFirstLower.setText(String.valueOf(firstLower));
                }
                llRes.setVisibility(View.VISIBLE);
                button.setText(R.string.cancel);
                btnClickedState = true;
            }
        }else{
            llRes.setVisibility(View.INVISIBLE);
            button.setText(R.string.print);
            btnClickedState = false;
        }


    }

    private ArrayList<Integer> getAllDigitsSolutions(ArrayList<Integer> digits){
        if (digits.size() == 2){
            ArrayList<Integer>returnList = new ArrayList<>();
            returnList.add(digits.get(0) * 10 + digits.get(1));
            returnList.add(digits.get(1) * 10 + digits.get(0));
            return returnList;
        }else{
            ArrayList<Integer> newList;
            ArrayList<Integer>returnList = new ArrayList<>();
            for (int digit : digits) {
                newList = new ArrayList<>();
                newList.addAll(digits);
                newList.remove(newList.indexOf(digit));
                ArrayList<Integer>resList = getAllDigitsSolutions(newList);
                for (int res : resList) {
                    returnList.add(res + digit *  (int)Math.pow(10f,
                            (double)reverseFactorial(resList.size())));
                }
            }
            return  returnList;
        }
    }

    private int reverseFactorial(int number){
        int tempNumber = number;

        int divisor = 2;
        while (tempNumber != 1){
            tempNumber /= divisor;
            if (tempNumber == 1){
                return  divisor;
            }
            divisor++;
        }
        return 0;
    }

    private int findFirstHighest(ArrayList<Integer> list, int targetNumber){
        int tempHighest = 10_000;

        for (int listNumber: list) {
            if (listNumber < tempHighest && listNumber > targetNumber)
                tempHighest = listNumber;
        }
        return tempHighest;
    }

    private int findFirstLowest(ArrayList<Integer> list, int targetNumber){
        int tempLowest = 0;

        for (int listNumber : list) {
            if (listNumber > tempLowest && listNumber < targetNumber)
                tempLowest = listNumber;
        }
        return tempLowest;
    }
}


