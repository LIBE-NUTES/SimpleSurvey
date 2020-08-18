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
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;
import br.edu.uepb.nutes.simplesurvey.base.TextAlign;

/**
 * Open question implementation.
 */
public class Open extends BaseQuestion<Open.Config> implements ISlideBackgroundColorHolder {
    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnTextBoxListener mListener;
    private Config configPage;
    private EditText textBox;

    public Open() {
    }

    /**
     * New Open question instance.
     *
     * @param configPage {@link Config}
     * @return Open
     */
    private static Open builder(Config configPage) {
        Open pageFragment = new Open();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.blockQuestion();

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            super.setPageNumber(configPage.getPageNumber());
        }
    }

    @Override
    public void initView(View v) {
        this.textBox = v.findViewById(R.id.answer_text_box);

        if (textBox != null) {
            textBox.setImeOptions(EditorInfo.IME_ACTION_DONE);

            setTextAlign(configPage.getTextAlign());

            if (configPage.hintStr != null && !configPage.hintStr.isEmpty()) {
                textBox.setHint(configPage.hintStr);
            } else if (configPage.hint != 0) {
                textBox.setHint(configPage.hint);
            }

            if (configPage.inputType != 0)
                textBox.setInputType(configPage.inputType);

            if (configPage.answerInit != null && !configPage.answerInit.isEmpty())
                textBox.setText(configPage.answerInit);

            if (configPage.background != 0)
                textBox.setBackgroundResource(configPage.background);

            if (configPage.colorBackgroundTint != 0) {
                ViewCompat.setBackgroundTintList(textBox,
                        ColorStateList.valueOf(configPage.colorBackgroundTint));
            }

            if (configPage.colorText != 0) {
                textBox.setTextColor(configPage.colorText);
                textBox.setHintTextColor(configPage.colorText);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (textBox == null) return;

        textBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    if (String.valueOf(textBox.getText()).isEmpty()) {
                        blockQuestion();
                        return true;
                    }

                    if (mListener != null) {
                        mListener.onAnswerTextBox(configPage.getPageNumber(),
                                String.valueOf(textBox.getText()));
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
        return textBox;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTextBoxListener) {
            mListener = (OnTextBoxListener) context;
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
        textBox.setText("");
        textBox.setHint(configPage.hint);
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
        if (value != null && !value.isEmpty()) textBox.setText(value);
    }

    private void setTextAlign(int align) {
        // text align
        if (align == TextAlign.CENTER) textBox.setGravity(Gravity.CENTER);
        else if (align == TextAlign.END) textBox.setGravity(Gravity.END);
        else textBox.setGravity(Gravity.START);
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Open.Config> implements Parcelable {
        @DrawableRes
        private int background;
        @ColorInt
        int colorText, colorBackgroundTint;
        @StringRes
        private int hint;
        private String answerInit, hintStr;
        private int inputType;

        public Config() {
            super.layout(R.layout.question_open_layout);
            this.background = 0;
            this.colorBackgroundTint = 0;
            this.colorBackgroundTint = 0;
            this.hint = R.string.survey_enter_an_answer;
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
        public Open build() {
            return Open.builder(this);
        }
    }

    /**
     * Interface OnTextBoxListener.
     */
    public interface OnTextBoxListener extends OnQuestionListener {
        void onAnswerTextBox(int page, String value);
    }
}
