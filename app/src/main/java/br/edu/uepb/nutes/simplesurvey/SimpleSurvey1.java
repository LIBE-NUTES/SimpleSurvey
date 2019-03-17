package br.edu.uepb.nutes.simplesurvey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.base.SimpleSurvey;
import br.edu.uepb.nutes.simplesurvey.question.Dichotomic;
import br.edu.uepb.nutes.simplesurvey.question.Infor;
import br.edu.uepb.nutes.simplesurvey.question.Multiple;
import br.edu.uepb.nutes.simplesurvey.question.Single;
import br.edu.uepb.nutes.simplesurvey.question.Open;

public class SimpleSurvey1 extends SimpleSurvey implements Infor.OnInfoListener,
        Dichotomic.OnDichotomicListener, Single.OnSingleListener,
        Multiple.OnMultipleListener,
        Open.OnTextBoxListener {
    private final String LOG_TAG = SimpleSurvey1.class.getSimpleName();

    @Override
    protected void initView() {
        addPages();
    }

    private void addPages() {
        setMessageBlocked("Oops! answer so i can go to the next question...");
        /**
         * Available animations:
         *    - setFadeAnimation()
         *    - setZoomAnimation()
         *    - setFlowAnimation()
         *    - setSlideOverAnimation()
         *    - setDepthAnimation()
         * More details: {https://github.com/AppIntro/AppIntro#animations}
         */
        setFadeAnimation();

        addQuestion(new Infor.Config()
                .layout(R.layout.welcome)
                .nextQuestionAuto()
                .pageNumber(0)
                .build());

        addQuestion(new Open.Config()
                .title("Title of the question 1", Color.WHITE)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?",
                        Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorDeepPurple))
                .image(R.drawable.placeholder)
                .buttonClose(R.drawable.ic_action_close_dark)
                .inputColorBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
                .inputColorText(Color.WHITE)
                .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .nextQuestionAuto()
                .pageNumber(1)
                .build());

        addQuestion(new Open.Config()
                .title("Title of the question 2", Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorDeepPurple))
                .buttonClose(R.drawable.ic_action_close_dark)
                .inputBackground(R.drawable.edittext_border_style)
                .inputColorText(Color.WHITE)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .pageNumber(2)
                .build());

        addQuestion(new Dichotomic.Config()
                .title("Title of the question 3", Color.WHITE)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?",
                        Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorGreen))
                .buttonClose(R.drawable.ic_action_close_dark)
                .image(R.drawable.placeholder)
                .enableZoomImage()
                .inputStyle(R.drawable.radio_sample1_lef, R.drawable.radio_sample1_right,
                        Color.WHITE, Color.WHITE)
                .inputLeftText(R.string.masc)
                .inputRightText(R.string.femi)
                .pageNumber(3)
                .build());

        addQuestion(new Single.Config()
                .title("Title of the question 4", Color.WHITE)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?",
                        Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorCyan))
                .image(R.drawable.placeholder)
                .buttonClose(R.drawable.ic_action_close_dark)
                .inputColorBackgroundTint(Color.WHITE)
                .inputColorSelectedText(Color.WHITE)
                .inputItems(new ArrayList<String>() {{
                    add("Item 1");
                    add("Item 2");
                    add("Item 3");
                    add("Item 4");
                }})
                .inputDisableAddNewItem()
                .nextQuestionAuto()
                .pageNumber(4)
                .build());

        addQuestion(new Dichotomic.Config()
                .title("Title of the question 5")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?")
                .nextQuestionAuto()
                .pageNumber(3)
                .build());

        addQuestion(new Multiple.Config()
                .title("Simple Survey 6", Color.WHITE)
                .description("Simple survey from @NUTES", Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorDeepPurple))
                .buttonClose(R.drawable.ic_action_close_dark)
                .inputItems(new ArrayList<String>() {{
                    add("Item 1");
                    add("Item 2");
                    add("Item 3");
                    add("Item 4");
                }})
                .inputColorBackgroundTint(Color.WHITE)
                .inputColorSelectedText(Color.WHITE)
                .nextQuestionAuto()
                .pageNumber(5)
                .build());

        addQuestion(new Infor.Config()
                .title("Thank you for the answers :)")
                .pageNumber(-1)
                .build());
    }

    @Override
    public void onClosePage() {
        new AlertDialog
                .Builder(this)
                .setMessage("Do you want to cancel the survey??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onAnswerInfo(int page) {
        Log.d(LOG_TAG, "onAnswerInfo() | PAGE: " + page);
        if (page == -1) { // end page
            finish();
        }
    }

    @Override
    public void onAnswerDichotomic(int page, boolean value) {
        Log.d(LOG_TAG, "onAnswerDichotomic() | PAGE:  " + page + " | ANSWER: " + value);
    }

    @Override
    public void onAnswerSingle(int page, String value, int indexValue) {
        Log.d(LOG_TAG, "onAnswerMultiple() | PAGE:  " + page
                + " | ANSWER (value): " + value
                + " | ANSWER (index): " + indexValue);
    }

    @Override
    public void onAnswerMultiple(int page, List<String> values, List<Integer> indexValues) {
        Log.d(LOG_TAG, "onAnswerMultiple() | PAGE:  " + page
                + " | ANSWER (values): " + Arrays.toString(values.toArray())
                + " | ANSWER (indexes): " + Arrays.toString(indexValues.toArray()));
    }

    @Override
    public void onAnswerTextBox(int page, String value) {
        Log.d(LOG_TAG, "onAnswerTextBox() | PAGE:  " + page
                + " | ANSWER: " + value);
    }
}
