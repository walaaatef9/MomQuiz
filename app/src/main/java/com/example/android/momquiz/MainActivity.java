package com.example.android.momquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.momquiz.R;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button submitButton, resetButton, ansNewButton, ansGenButton, ansEmoButton, ansMealButton, ansCryButton, ansreadingToYourBaby ;
    CheckBox  answerScared,  answerTired,  answerWorried,  answerEnjoyable,  answerPickDirect,  answerTenMin,  answerUntileStop;
    RadioButton answerNewmom, answerGender, answerMeals;
    EditText readingToYourBaby;
    RadioGroup momGroup, genderGroup, mealGroup;
    LinearLayout emotionsGroup, cryGroup;
    List<Button> answerButtons;
    List<LinearLayout> groups;
    List<RadioButton> radioCorrectAnswers;
    TextView finalScore;
    static final String Baby_GROUP_STATE = "babyGroup";
    static final String SCORE = "score";
    static final String SCORE_VISIBILITY = "scoreVisibility";
    static final String ANSWER_VISIBILITY = "answerVisibility";
    static final String RESET_VISIBILITY = "resetVisibility";
    static final String SUBMIT_STATE = "submitButtonState";
    static final String USER_ANSWER_STATE = "readingToYourBabyState";
    static final String USER_ANSWER_FOCUSABLE = "readingToYourBabyFocusable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save current states and visibilities for buttons and answers
        savedInstanceState.putBoolean(Baby_GROUP_STATE, momGroup.getChildAt(0).isEnabled());
        savedInstanceState.putCharSequence(SCORE, finalScore.getText());
        savedInstanceState.putInt(SCORE_VISIBILITY, finalScore.getVisibility());
        savedInstanceState.putInt(ANSWER_VISIBILITY, ansNewButton.getVisibility());
        savedInstanceState.putInt(RESET_VISIBILITY, resetButton.getVisibility());
        savedInstanceState.putBoolean(SUBMIT_STATE, submitButton.isEnabled());
        savedInstanceState.putBoolean(USER_ANSWER_STATE, readingToYourBaby.isEnabled());
        savedInstanceState.putBoolean(USER_ANSWER_FOCUSABLE, readingToYourBaby.isFocusable());

        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state (enabled / disabled) for all group of answers
        for (LinearLayout group : groups) {
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setEnabled(savedInstanceState.getBoolean(Baby_GROUP_STATE));
            }
        }
        finalScore.setText(savedInstanceState.getCharSequence(SCORE));
        finalScore.setVisibility(savedInstanceState.getInt(SCORE_VISIBILITY));

        // Restore visibility for answer , reset and submit buttons
        for (Button btn : answerButtons) {
            btn.setVisibility(savedInstanceState.getInt(ANSWER_VISIBILITY));
        }
        resetButton.setVisibility(savedInstanceState.getInt(RESET_VISIBILITY));
        submitButton.setEnabled(savedInstanceState.getBoolean(SUBMIT_STATE));

        // Restore EditText View state
        readingToYourBaby.setEnabled(savedInstanceState.getBoolean(USER_ANSWER_STATE));
        readingToYourBaby.setFocusable(savedInstanceState.getBoolean(USER_ANSWER_FOCUSABLE));
    }

    public void initializeVariables() {
        finalScore = findViewById(R.id.final_score);
        readingToYourBaby = findViewById(R.id.reading_to_your_baby);

        // groups of answers
        momGroup = findViewById(R.id.newmom_group);
        genderGroup = findViewById(R.id.geder_group);
        emotionsGroup = findViewById(R.id.emotions_group);
        mealGroup = findViewById(R.id.meals_group);
        cryGroup = findViewById(R.id.cry_group);

        // buttons
        submitButton = findViewById(R.id.submit_button);
        resetButton = findViewById(R.id.reset_button);
        ansNewButton = findViewById(R.id.show_answer_new_mom);
        ansGenButton = findViewById(R.id.show_answer_gender);
        ansEmoButton = findViewById(R.id.show_answer_emotions);
        ansMealButton = findViewById(R.id.show_answer_meals);
        ansCryButton = findViewById(R.id.show_answer_cry);
        ansreadingToYourBaby = findViewById(R.id.show_answer_reading);

        // checkboxes
        answerScared = findViewById(R.id.scared_checkbox);
        answerTired = findViewById(R.id.tired_checkbox);
        answerWorried  = findViewById(R.id.worried_checkbox);
        answerEnjoyable = findViewById(R.id.enjoyable_checkbox);
        answerPickDirect = findViewById(R.id.pi_checkbox);
        answerTenMin = findViewById(R.id.ten_shaken);
        answerUntileStop = findViewById(R.id.untile_he_stop);

        // correct radio
        answerNewmom = findViewById(R.id.yes_radio_button);
        answerGender = findViewById(R.id.male_radio_button);
        answerMeals = findViewById(R.id.three_radio_button);

        // lists
        answerButtons = Arrays.asList(ansNewButton, ansGenButton, ansEmoButton,
                ansMealButton, ansCryButton, ansreadingToYourBaby);
        groups = Arrays.asList(momGroup, genderGroup, emotionsGroup, cryGroup, mealGroup);
        radioCorrectAnswers = Arrays.asList(answerNewmom, answerGender, answerMeals);

    }
    public void submitAnswers(View view) {
        changeButtonState(false);
        displayFinalScore(calculateScore());
    }

    public int calculateScore() {
        int score = 0;

        if ((! answerScared.isChecked()) && (! answerWorried.isChecked())) {
            if ((answerTired.isChecked()) && (answerEnjoyable.isChecked())) score++;
        }

        if ((!  answerUntileStop.isChecked())) {
            if ((answerPickDirect.isChecked()) && (answerTenMin.isChecked())) score++;
        }

        if (readingToYourBaby.getText().toString().equalsIgnoreCase(getResources().getString(R.string.show_answer_reading))) score++;


        for (RadioButton correctAnswer : radioCorrectAnswers) {
            if (correctAnswer.isChecked()) score++;
        }

        return score;
    }

    public void displayFinalScore(int score) {
        finalScore.setText(String.format("SCORE: %d/6", score));
        finalScore.setVisibility(View.VISIBLE);
    }

    public void changeButtonState(boolean state) {
        readingToYourBaby.setFocusable(state);
        readingToYourBaby.setEnabled(state);
        submitButton.setEnabled(state);

        resetButton.setVisibility(View.VISIBLE);
        for (Button btn : answerButtons) {
            btn.setVisibility(View.VISIBLE);
        }

        for (LinearLayout group : groups) {
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setEnabled(state);
            }
        }

    }

    // Shows toast message below button with button tag dependant text
    public void viewAnswer(View view) {
        String correctAnswer = "";
        int location[]=new int[2];

        for (Button btn : answerButtons) {
            if (view.getId() == btn.getId()) {
                correctAnswer = (String) btn.getTag();
            }
        }

        view.getLocationOnScreen(location);
        Toast toast = Toast.makeText(this, correctAnswer, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.START, location[0], location[1]+90);
        toast.show();
    }

    public void reset(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
