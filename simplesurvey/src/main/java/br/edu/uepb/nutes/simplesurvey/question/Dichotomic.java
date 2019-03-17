/*
 * Copyright (c) 2018 NUTES/UEPB
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.edu.uepb.nutes.simplesurvey.question;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.RadioGroup;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;

/**
 * Dichotomic implementation.
 */
public class Dichotomic extends BaseQuestion<Dichotomic.Config> implements ISlideBackgroundColorHolder {
    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";
    private static final String KEY_OLD_ANSWER_BUNDLE = "old_answer";

    private OnDichotomicListener mListener;
    private boolean actionClearCheck;
    private int oldAnswer;
    private Config configPage;
    private RadioGroup radioGroup;
    private AppCompatRadioButton radioLeft;
    private AppCompatRadioButton radioRight;

    public Dichotomic() {
    }

    /**
     * New Dichotomic instance.
     *
     * @param configPage {@link Config}
     * @return Dichotomic
     */
    private static Dichotomic builder(Config configPage) {
        Dichotomic pageFragment = new Dichotomic();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.blockQuestion();

        // Setting default values
        oldAnswer = -1;
        actionClearCheck = false;

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            super.setPageNumber(configPage.getPageNumber());
        }
    }

    @Override
    public void initView(View v) {
        this.radioGroup = v.findViewById(R.id.answer_radioGroup);
        this.radioLeft = v.findViewById(R.id.left_radioButton);
        this.radioRight = v.findViewById(R.id.right_radioButton);

        if (radioGroup != null) {
            if (configPage.radioLeftText != 0)
                radioLeft.setText(configPage.radioLeftText);
            else if (this.configPage.radioLeftTextStr != null &&
                    !this.configPage.radioLeftTextStr.isEmpty()) {
                radioLeft.setText(configPage.radioLeftTextStr);
            }

            if (configPage.radioRightText != 0)
                radioRight.setText(configPage.radioRightText);
            else if (this.configPage.radioRightTextStr != null &&
                    !this.configPage.radioRightTextStr.isEmpty()) {
                radioLeft.setText(configPage.radioRightTextStr);
            }

            if (configPage.radioLeftBackground != 0)
                radioLeft.setBackgroundResource(configPage.radioLeftBackground);

            if (configPage.radioRightBackground != 0)
                radioRight.setBackgroundResource(configPage.radioRightBackground);

            if (configPage.radioColorTextNormal != 0) {
                radioLeft.setTextColor(configPage.radioColorTextNormal);
                radioRight.setTextColor(configPage.radioColorTextNormal);
            }
            // init answer
            if (configPage.answerInit != -1) setAnswer(configPage.answerInit != 0);

            refreshStyles();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_OLD_ANSWER_BUNDLE, oldAnswer);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (radioGroup == null) return;

        if (savedInstanceState != null) {
            oldAnswer = savedInstanceState.getInt(KEY_OLD_ANSWER_BUNDLE, -1);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (actionClearCheck) return;

                if (checkedId == R.id.left_radioButton && oldAnswer != 0) {
                    setAnswer(false);
                    if (mListener != null) {
                        mListener.onAnswerDichotomic(getQuestionNumber(), false);
                        if (configPage.isNextQuestionAuto()) nextQuestion();
                    }
                } else if (checkedId == R.id.right_radioButton && oldAnswer != 1) {
                    setAnswer(true);
                    if (mListener != null) {
                        mListener.onAnswerDichotomic(getQuestionNumber(), true);
                        if (configPage.isNextQuestionAuto()) nextQuestion();
                    }
                }
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
        return radioGroup;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (radioGroup != null) radioGroup.setOnCheckedChangeListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDichotomicListener) {
            mListener = (OnDichotomicListener) context;
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
        actionClearCheck = true;
        radioGroup.clearCheck();
        actionClearCheck = false;
        oldAnswer = -1;
        refreshStyles();

        // Block page
        super.blockQuestion();
    }

    /**
     * Set Answer.
     *
     * @param value boolean
     */
    private void setAnswer(boolean value) {
        super.unlockQuestion();
        oldAnswer = !value ? 0 : 1;

        if (value) radioRight.setChecked(true);
        else radioLeft.setChecked(true);
        refreshStyles();
    }

    private void refreshStyles() {
        if (this.configPage.radioColorTextNormal == 0 &&
                this.configPage.radioColorTextChecked == 0) return;

        if (this.oldAnswer == 1) {
            this.radioRight.setTextColor(this.configPage.radioColorTextChecked);
            this.radioLeft.setTextColor(this.configPage.radioColorTextNormal);
        } else if (this.oldAnswer == 0) {
            this.radioLeft.setTextColor(this.configPage.radioColorTextChecked);
            this.radioRight.setTextColor(this.configPage.radioColorTextNormal);
        } else {
            this.radioLeft.setTextColor(this.configPage.radioColorTextNormal);
            this.radioRight.setTextColor(this.configPage.radioColorTextNormal);
        }
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Dichotomic.Config> implements Parcelable {
        private int radioLeftText,
                radioRightText,
                radioColorTextNormal,
                radioColorTextChecked,
                radioLeftBackground,
                radioRightBackground,
                answerInit;
        private String radioLeftTextStr,
                radioRightTextStr;

        public Config() {
            super.layout(R.layout.question_dichotomic_layout);
            this.radioLeftText = 0;
            this.radioRightText = 0;
            this.radioColorTextNormal = 0;
            this.radioColorTextChecked = 0;
            this.radioLeftBackground = 0;
            this.radioRightBackground = 0;
            this.answerInit = -1;
        }

        protected Config(Parcel in) {
            radioLeftText = in.readInt();
            radioRightText = in.readInt();
            radioColorTextNormal = in.readInt();
            radioColorTextChecked = in.readInt();
            radioLeftBackground = in.readInt();
            radioRightBackground = in.readInt();
            answerInit = in.readInt();
            radioLeftTextStr = in.readString();
            radioRightTextStr = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(radioLeftText);
            dest.writeInt(radioRightText);
            dest.writeInt(radioColorTextNormal);
            dest.writeInt(radioColorTextChecked);
            dest.writeInt(radioLeftBackground);
            dest.writeInt(radioRightBackground);
            dest.writeInt(answerInit);
            dest.writeString(radioLeftTextStr);
            dest.writeString(radioRightTextStr);
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
         * Set left radio text.
         *
         * @param text {@link StringRes} resource text left.
         * @return Config
         */
        public Config inputLeftText(@StringRes int text) {
            this.radioLeftText = text;
            return this;
        }

        /**
         * Set right radio text.
         *
         * @param text {@link StringRes} resource text right.
         * @return Config
         */
        public Config inputRightText(@StringRes int text) {
            this.radioRightText = text;
            return this;
        }

        /**
         * Set left radio text.
         *
         * @param text {@link String} text left.
         * @return Config
         */
        public Config inputLeftText(String text) {
            this.radioLeftTextStr = text;
            return this;
        }

        /**
         * Set right radio text.
         *
         * @param text {@link String} text right.
         * @return Config
         */
        public Config inputRightText(String text) {
            this.radioRightTextStr = text;
            return this;
        }

        /**
         * Set style radio.
         *
         * @param backgroundLeftInput   {@link DrawableRes} resource the left side.
         * @param backgroundRightInput  {@link DrawableRes} resource the right side.
         * @param textColorInputNormal  {@link ColorInt} resource text color normal.
         * @param textColorInputChecked {@link ColorInt} resource text color checked.
         * @return Config
         */
        public Config inputStyle(@DrawableRes int backgroundLeftInput,
                                 @DrawableRes int backgroundRightInput,
                                 @ColorInt int textColorInputNormal,
                                 @ColorInt int textColorInputChecked) {
            this.radioLeftBackground = backgroundLeftInput;
            this.radioRightBackground = backgroundRightInput;
            this.radioColorTextNormal = textColorInputNormal;
            this.radioColorTextChecked = textColorInputChecked;
            return this;
        }

        /**
         * Set answer init.
         *
         * @param answer boolean answer.
         * @return Config
         */
        public Config answerInit(boolean answer) {
            if (answer) this.answerInit = 1;
            else this.answerInit = 0;
            return this;
        }

        @Override
        public Dichotomic build() {
            return Dichotomic.builder(this);
        }
    }

    /**
     * Interface OnDichotomicListener.
     */
    public interface OnDichotomicListener extends OnQuestionListener {
        void onAnswerDichotomic(int page, boolean value);
    }
}
