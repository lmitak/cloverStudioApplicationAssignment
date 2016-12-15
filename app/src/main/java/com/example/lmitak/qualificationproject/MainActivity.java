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
    private boolean btnClickedState;    /**variable for checking if button was clicked to display values**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**reference layout elements**/
        button = (Button) findViewById(R.id.btn);
        etNumberInput = (EditText) findViewById(R.id.etNumberInput);
        tvFirstHigher = (TextView) findViewById(R.id.textHigherFirst);
        tvFirstLower = (TextView) findViewById(R.id.textLowerFirst);
        llRes = (LinearLayout) findViewById(R.id.llResults);

        /**Set onClickListener for the button**/
        button.setOnClickListener(this);
        /**Set default state of the button**/
        btnClickedState = false;
    }

    @Override
    public void onClick(View view) {
        /**Check if button was clicked to display values**/
        if (!btnClickedState){
            number = Integer.parseInt(etNumberInput.getText().toString());  /**Get value for input field**/
            /**If value is out of range, display a message to the user**/
            if (number <= 100 || number >= 10000){
                Toast.makeText(getApplicationContext(), "Broj nije u zadanom rasponu", Toast.LENGTH_SHORT).show();
            }else{  /**If value is in the range proceed with calculation**/

                ArrayList<Integer> digits = new ArrayList<>();  /**List for storing all digits in a number**/
                int tempNumber = number;
                /**Loop over from 2(or 3, depending on number size) to 0**/
                for (int i = number < 1000 ? 2 : 3; i >= 0; i--){
                    /**Add a number to the list which was divided by 10 to the power for variable i **/
                    digits.add(tempNumber / (int)Math.pow(10f, ((double) i)));
                    /**Store the remainder of the division for next iteration**/
                    tempNumber = tempNumber % ((int)Math.pow(10f, ((double) i)));
                }
                /**Get all possible solutions from list of digits**/
                ArrayList<Integer> res = getAllDigitsSolutions(digits);
                /**Get the closest higher solution with same digits from solutions list**/
                int firstHigher = findFirstHighest(res, number);
                /**If firstHigher value is 10_000, higher number was not found**/
                if (firstHigher == 10_000){
                    tvFirstHigher.setText(R.string.no_higher_value);
                }else{
                    tvFirstHigher.setText(String.valueOf(firstHigher));
                }
                /**Get the closest lower solution with same digits from solutions list**/
                int firstLower = findFirstLowest(res, number);
                /**If firstLower value is 0, lower number was not found**/
                if (firstLower == 0){
                    tvFirstLower.setText(R.string.no_lower_value);
                }else{
                    tvFirstLower.setText(String.valueOf(firstLower));
                }

                llRes.setVisibility(View.VISIBLE);  /**Display layout with closest lower number and higher number values**/
                button.setText(R.string.cancel);    /**Change button text to 'cancel' text**/
                btnClickedState = true; /**Change button state**/
                etNumberInput.setFocusable(false);  /**Disable editing of the input field**/
            }
        }else{
            llRes.setVisibility(View.INVISIBLE);    /**Hide layout with result information**/
            button.setText(R.string.print); /**change button text to 'print' text**/
            btnClickedState = false;    /**Change button state**/
            etNumberInput.setFocusableInTouchMode(true);    /**Enable editing of the input field**/

        }


    }

    /**Returns all number solutions for the given digits**/
    private ArrayList<Integer> getAllDigitsSolutions(ArrayList<Integer> digits){
        /**Check if there are only 2 digits left**/
        if (digits.size() == 2){
            /**Make 2 possible solutions from 2 digits and return them back**/
            ArrayList<Integer>returnList = new ArrayList<>();
            returnList.add(digits.get(0) * 10 + digits.get(1));
            returnList.add(digits.get(1) * 10 + digits.get(0));
            return returnList;
        }else{
            ArrayList<Integer> newList;
            ArrayList<Integer>returnList = new ArrayList<>(); /**Create an instance of a list that will be returned**/
            /**Iterate through all digits*/
            for (int digit : digits) {
                newList = new ArrayList<>();    /**Initialize new list**/
                /**Copy all contents from digits list to the new list and remove from it the digit which is currently the iterator **/
                newList.addAll(digits);
                newList.remove(newList.indexOf(digit));
                /**Recursively call itself for the new list of digits and store returned result list**/
                ArrayList<Integer>resList = getAllDigitsSolutions(newList);
                /**Iterate over returned result**/
                for (int res : resList) {
                    /**Add to the list a value that is a sum of the value from the returned result list
                     * and the value that is result of multiplying
                     * the digit that is currently a iterator in the outer loop
                     * and 10 to the power of reverse factorial operation of a number of values in returned result list.
                     * For instance, if returned result list has values: 135, 153, 531, 513, 351, 315, then
                     * in first iteration a value that will be added to the new list is:
                     * 135 + value of the outer loop * 10 to the power of 3 (3 is result of reverse factorial operation on value: 6)**/
                    returnList.add(res + digit *  (int)Math.pow(10f,
                            (double)reverseFactorial(resList.size())));
                }
            }
            return  returnList;
        }
    }

    /**Returns a value that would give parameter as a result of factorial operation**/
    private int reverseFactorial(int number){
        int tempNumber = number;
        /**Choose lowest divisor**/
        int divisor = 2;
        /**Loop over while result of division isn't 1**/
        while (tempNumber != 1){
            /**Divide a number with the divisor **/
            tempNumber /= divisor;
            /**if result is 1, then the divisor is the value that would give parameter as a result of factorial operation**/
            if (tempNumber == 1){/****/
                return  divisor;
            }
            divisor++; /**Increase divisor after every iteration**/
        }
        return 0;
    }

    /**Find first highest value in given list that isn't the given number and has same amount of digits**/
    private int findFirstHighest(ArrayList<Integer> list, int targetNumber){
        int tempHighest = 10_000;

        for (int listNumber: list) {
            if (listNumber < tempHighest && listNumber > targetNumber
                    && numberOfDigits(listNumber) == numberOfDigits(targetNumber))
                tempHighest = listNumber;
        }
        return tempHighest;
    }
    /**Find first lowest value in given list that isn't the given number and has same amount of digits**/
    private int findFirstLowest(ArrayList<Integer> list, int targetNumber){
        int tempLowest = 0;

        for (int listNumber : list) {
            if (listNumber > tempLowest && listNumber < targetNumber
                    && numberOfDigits(listNumber) == numberOfDigits(targetNumber))
                tempLowest = listNumber;
        }
        return tempLowest;
    }

    /**Returns how many digits a number has**/
    private int numberOfDigits(int number){
        int i = 0;
        int tempNumber = number;
        while (tempNumber != 0){
            tempNumber = number / (int)Math.pow(10f, (double)i);
            i++;
        }

        return i-1;
    }
}


