package br.edu.uepb.nutes.simplesurvey;

import android.support.v4.content.ContextCompat;

import br.edu.uepb.nutes.simplesurvey.base.SimpleSurvey;
import br.edu.uepb.nutes.simplesurvey.pages.DichotomicChoice;
import br.edu.uepb.nutes.simplesurvey.pages.Infor;

public class SimpleSurvey1 extends SimpleSurvey implements DichotomicChoice.OnRadioListener,
        Infor.OnButtonListener {

    @Override
    protected void initView() {
        addPages();
    }

    private void addPages() {
        setMessageBlocked("Ops! responda para poder ir para próxima perguta...");

        // page 1
        addQuestion(new DichotomicChoice.Config()
                .title("Title of the question")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry?")
                .radioLeftText(R.string.masc)
                .radioRightText(R.string.femi)
                .nextQuestionAuto()
                .image(R.drawable.placeholder)
                .enableZoomImage()
                .pageNumber(1)
                .build());

        addQuestion(new DichotomicChoice.Config()
                .title("Simple Survey 1")
                .description("Simple survey from @NUTES")
                .colorBackground(ContextCompat.getColor(this, R.color.colorCyan))
                .pageNumber(2)
                .build());

        addQuestion(new DichotomicChoice.Config()
                .title("Simple Survey 2")
                .description("Simple survey from @NUTES")
                .pageNumber(3)
                .nextQuestionAuto()
                .build());

        addQuestion(new DichotomicChoice.Config()
                .title("Simple Survey 3")
                .description("Simple survey from @NUTES")
                .colorBackground(ContextCompat.getColor(this, R.color.colorBlueGrey))
                .pageNumber(4)
                .build());


        addQuestion(new Infor.Config()
                .title("Lorem Ipsum")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,")
                .pageNumber(4)
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
