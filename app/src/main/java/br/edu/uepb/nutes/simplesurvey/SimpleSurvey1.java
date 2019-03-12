package br.edu.uepb.nutes.simplesurvey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.base.SimpleSurvey;
import br.edu.uepb.nutes.simplesurvey.pages.DichotomicChoice;
import br.edu.uepb.nutes.simplesurvey.pages.Infor;
import br.edu.uepb.nutes.simplesurvey.pages.MultipleChoice;
import br.edu.uepb.nutes.simplesurvey.pages.SingleChoice;

public class SimpleSurvey1 extends SimpleSurvey implements Infor.OnInfoListener,
        DichotomicChoice.OnDichotomicListener, SingleChoice.OnSingleListener,
        MultipleChoice.OnMultipleListener {
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

        addQuestion(new DichotomicChoice.Config()
                .title("Title of the question 1", Color.WHITE)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?",
                        Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorGreen))
                .radioStyle(R.drawable.radio_sample1_lef, R.drawable.radio_sample1_right, Color.WHITE, Color.WHITE)
                .radioLeftText(R.string.masc)
                .radioRightText(R.string.femi)
                .image(R.drawable.placeholder)
                .buttonClose(R.drawable.ic_action_close_dark)
                .enableZoomImage()
                .pageNumber(1)
                .build());

        addQuestion(new SingleChoice.Config()
                .title("Title of the question 2", Color.WHITE)
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?",
                        Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorCyan))
                .colorBackgroundTint(Color.WHITE)
                .colorSelectedText(Color.WHITE)
                .items(new ArrayList<String>() {{
                    add("Item 1");
                    add("Item 2");
                    add("Item 3");
                    add("Item 4");
                }})
                .image(R.drawable.placeholder)
                .buttonClose(R.drawable.ic_action_close_dark)
                .disableAddNewItem()
                .nextQuestionAuto()
                .pageNumber(2)
                .build());

        addQuestion(new DichotomicChoice.Config()
                .title("Title of the question 3")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?")
                .nextQuestionAuto()
                .pageNumber(3)
                .build());

        addQuestion(new MultipleChoice.Config()
                .title("Simple Survey 3", Color.WHITE)
                .description("Simple survey from @NUTES", Color.WHITE)
                .colorBackground(ContextCompat.getColor(this, R.color.colorDeepPurple))
                .items(new ArrayList<String>() {{
                    add("Item 1");
                    add("Item 2");
                    add("Item 3");
                    add("Item 4");
                }})
                .buttonClose(R.drawable.ic_action_close_dark)
                .nextQuestionAuto()
                .colorBackgroundTint(Color.WHITE)
                .colorSelectedText(Color.WHITE)
                .pageNumber(4)
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
        Log.w(LOG_TAG, "onAnswerInfo() | PAGE: " + page);
        if (page == 0) { // first page
            nextQuestion();
        } else if (page == -1) { // end page
            finish();
        }
    }

    @Override
    public void onAnswerDichotomic(int page, boolean value) {
        Log.w(LOG_TAG, "onAnswerDichotomic() | PAGE:  " + page + " | ANSWER: " + value);
    }

    @Override
    public void onAnswerSingle(int page, String value, int indexValue) {
        Log.w(LOG_TAG, "onAnswerMultiple() | PAGE:  " + page
                + " | ANSWER (value): " + value
                + " | ANSWER (index): " + indexValue);
    }

    @Override
    public void onAnswerMultiple(int page, List<String> values, List<Integer> indexValues) {
        Log.w(LOG_TAG, "onAnswerMultiple() | PAGE:  " + page
                + " | ANSWER (values): " + Arrays.toString(values.toArray())
                + " | ANSWER (indexes): " + Arrays.toString(indexValues.toArray()));
    }
}
