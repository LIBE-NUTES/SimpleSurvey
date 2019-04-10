package br.edu.uepb.nutes.simplesurvey.question;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.Calendar;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;

import static br.edu.uepb.nutes.simplesurvey.question.Single.ARG_CONFIGS_PAGE;

public class Date extends BaseQuestion<Date.Config> implements ISlideBackgroundColorHolder, View.OnClickListener {
    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private EditText editDate;
    private int mYear, mMonth, mDay;
    private Config configPage;
    private OnDateBoxListener mListener;

    public Date() {
    }

    /**
     * New Date instance.
     *
     * @param configPage {@link Config}
     * @return Date
     */
    private static Date builder(Config configPage) {
        Date pageFragment = new Date();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.blockQuestion();

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            if (configPage == null) return;
            super.setPageNumber(configPage.getPageNumber());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        if (id == R.id.answer_text_box) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }


    @Override
    public void initView(View v) {
        // Initialize components
        this.editDate = v.findViewById(R.id.answer_text_box);

        editDate.setOnClickListener(this);

        if (editDate != null) {
            editDate.setImeOptions(EditorInfo.IME_ACTION_DONE);


            if (configPage.hintStr != null && !configPage.hintStr.isEmpty()) {
                editDate.setHint(configPage.hintStr);
            } else if (configPage.hint != 0) {
                editDate.setHint(configPage.hint);
            }

            if (configPage.inputType != 0)
                editDate.setInputType(configPage.inputType);

            if (configPage.answerInit != null && !configPage.answerInit.isEmpty())
                editDate.setText(configPage.answerInit);

            if (configPage.background != 0)
                editDate.setBackgroundResource(configPage.background);

            if (configPage.colorBackgroundTint != 0) {
                ViewCompat.setBackgroundTintList(editDate,
                        ColorStateList.valueOf(configPage.colorBackgroundTint));
            }

            if (configPage.colorText != 0) {
                editDate.setTextColor(configPage.colorText);
                editDate.setHintTextColor(configPage.colorText);
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (editDate == null) return;

        editDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (String.valueOf(editDate.getText()).isEmpty()) {
                        blockQuestion();
                        return true;
                    }

                    if (mListener != null) {
                        mListener.onAnswerDate(configPage.getPageNumber(),
                                String.valueOf(editDate.getText()));
                    }
                    if (configPage.isNextQuestionAuto()) nextQuestion();
                    else unlockQuestion();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getLayout() {
        return configPage.getLayout();
    }

    @Override
    public Config getConfigsQuestion() {
        return this.configPage;
    }

    @Override
    public View getComponentAnswer() {
        return editDate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Date.OnDateBoxListener) {
            mListener = (Date.OnDateBoxListener) context;
            super.setListener(mListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return configPage.getColorBackground();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (getView() != null) getView().setBackgroundColor(configPage.getColorBackground());
    }

    @Override
    public void clearAnswer() {
        editDate.setText("");
        editDate.setHint(configPage.hint);
        // Block page
        super.blockQuestion();
    }

    /**
     * Set Answer.
     *
     * @param value {@link String}
     */
    private void setAnswer(String value) {
        super.unlockQuestion();
        if (value != null && !value.isEmpty()) editDate.setText(value);
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Config> implements Parcelable {
        @DrawableRes
        private int background;
        @ColorInt
        int colorText, colorBackgroundTint;
        @StringRes
        private int hint;
        private String answerInit, hintStr;
        private int inputType;

        public Config() {
            super.layout(R.layout.question_date_layout);
            this.background = 0;
            this.colorBackgroundTint = 0;
            this.colorBackgroundTint = 0;
            this.hint = R.string.select_date;
            this.answerInit = null;
            this.inputType = InputType.TYPE_CLASS_TEXT;
        }

        protected Config(Parcel in) {
            background = in.readInt();
            colorText = in.readInt();
            colorBackgroundTint = in.readInt();
            hint = in.readInt();
            answerInit = in.readString();
            inputType = in.readInt();
            hintStr = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(background);
            dest.writeInt(colorText);
            dest.writeInt(colorBackgroundTint);
            dest.writeInt(hint);
            dest.writeString(answerInit);
            dest.writeInt(inputType);
            dest.writeString(hintStr);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Config> CREATOR = new Creator<Config>() {
            @Override
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            @Override
            public Config[] newArray(int size) {
                return new Config[size];
            }
        };

        /**
         * Set background style.
         *
         * @param drawable {@link DrawableRes} resource background.
         * @return Config
         */
        public Config inputBackground(@DrawableRes int drawable) {
            this.background = drawable;
            return this;
        }

        /**
         * Set color text.
         *
         * @param color {@link ColorInt} color text.
         * @return Config
         */
        public Config inputColorText(@ColorInt int color) {
            this.colorText = color;
            return this;
        }

        /**
         * Set color background tint.
         * Corresponds to the bottom horizontal line.
         *
         * @param color {@link ColorInt} resource color.
         * @return Config
         */
        public Config inputColorBackgroundTint(@ColorInt int color) {
            this.colorBackgroundTint = color;
            return this;
        }

        /**
         * Set inputHint message.
         *
         * @param message {@link StringRes} resource color.
         * @return Config
         */
        public Config inputHint(@StringRes int message) {
            this.hint = message;
            return this;
        }

        /**
         * Set hint message.
         *
         * @param message {@String}
         * @return Config
         */
        public Config inputHint(String message) {
            this.hintStr = message;
            return this;
        }

        /**
         * Set input type.
         * {@link InputType}
         *
         * @param type Input type.
         * @return Config
         */
        public Config inputType(int type) {
            this.inputType = type;
            return this;
        }

        /**
         * Set answer init.
         *
         * @param answer {@link String} answer.
         * @return Config
         */
        public Config answerInit(String answer) {
            this.answerInit = answer;
            return this;
        }

        @Override
        public Date build() {
            return Date.builder(this);
        }
    }

    /**
     * Interface OnDateBoxListener.
     */
    public interface OnDateBoxListener extends OnQuestionListener {
        void onAnswerDate(int page, String value);
    }
}
