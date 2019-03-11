package br.edu.uepb.nutes.simplesurvey;

import android.support.v4.content.ContextCompat;

import br.edu.uepb.nutes.simplesurvey.base.SimpleSurvey;
import br.edu.uepb.nutes.simplesurvey.pages.InforPage;
import br.edu.uepb.nutes.simplesurvey.pages.RadioQuestion;

public class SimpleSurvey1 extends SimpleSurvey implements RadioQuestion.OnRadioListener,
        InforPage.OnButtonListener {

    @Override
    protected void initView() {
        addPages();
    }

    private void addPages() {

        // page 1
        addQuestion(new RadioQuestion.Config()
                .title("Title of the question")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?")
                .radioLeftText(R.string.masc)
                .radioRightText(R.string.femi)
                .enableNextQuestionAuto()
                .image(R.drawable.placeholder)
                .answerRequired()
                .pageNumber(1)
                .build());

        addQuestion(new RadioQuestion.Config()
                .title("Simple Survey")
                .description("Simple survey from @NUTES")
                .colorBackground(ContextCompat.getColor(this, R.color.colorCyan))
                .answerRequired()
                .pageNumber(1)
                .build());

        addQuestion(new InforPage.Config()
                .title("Lorem Ipsum")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,")
                .build());
    }

    @Override
    public void onAnswerButton(int page) {

    }

    @Override
    public void onAnswerRadio(int page, boolean value) {

    }

    @Override
    public void onClosePage() {

    }
}
