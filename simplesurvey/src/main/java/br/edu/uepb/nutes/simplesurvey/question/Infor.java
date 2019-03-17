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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;

/**
 * Infor implementation.
 * Useful to use as a home screen or thank you screen for participating in the assessment.
 */
public class Infor extends BaseQuestion<Infor.Config> implements ISlideBackgroundColorHolder {
    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnInfoListener mListener;
    private Config configPage;
    private AppCompatButton button;

    public Infor() {
    }

    /**
     * New Infor instance.
     *
     * @param configPage {@link Config}
     * @return Infor
     */
    private static Infor newInstance(Config configPage) {
        Infor pageFragment = new Infor();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            if (configPage == null) return;
            super.setPageNumber(configPage.getPageNumber());
        }
    }

    @Override
    public void initView(View v) {
        // Initialize components
        this.button = v.findViewById(R.id.answer_button);

        if (this.configPage.buttonBackground != 0) {
            this.button.setBackgroundResource(this.configPage.buttonBackground);
        }

        if (this.configPage.buttonText != 0) {
            this.button.setText(this.configPage.buttonText);
        } else if (this.configPage.buttonTextStr != null &&
                !this.configPage.buttonTextStr.isEmpty()) {
            this.button.setText(this.configPage.buttonTextStr);
        }

        if (configPage.buttonColorText != 0) {
            this.button.setTextColor(this.configPage.buttonColorText);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (button == null || mListener == null) return;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAnswerInfo(getQuestionNumber());
                if (configPage.isNextQuestionAuto()) nextQuestion();
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
        return button;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (button != null) button.setOnClickListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInfoListener) {
            mListener = (OnInfoListener) context;
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
        return (configPage.getColorBackground() != 0) ? configPage.getColorBackground() : Color.GRAY;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (configPage.getColorBackground() != 0 && getView() != null) {
            getView().setBackgroundColor(configPage.getColorBackground());
        }
    }

    @Override
    public void clearAnswer() {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Infor.Config> implements Parcelable {
        @StringRes
        private int buttonText;
        @ColorRes
        private int buttonColorText;
        @DrawableRes
        private int buttonBackground;
        private String buttonTextStr;

        public Config() {
            super.layout(R.layout.page_infor);
            this.buttonText = 0;
            this.buttonColorText = 0;
            this.buttonBackground = 0;
        }

        protected Config(Parcel in) {
            buttonText = in.readInt();
            buttonColorText = in.readInt();
            buttonBackground = in.readInt();
            buttonTextStr = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(buttonText);
            dest.writeInt(buttonColorText);
            dest.writeInt(buttonBackground);
            dest.writeString(buttonTextStr);
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
         * Set button text.
         *
         * @param text {@link StringRes} resource of text.
         * @return Config
         */
        public Config inputText(@StringRes int text) {
            this.buttonText = text;
            return this;
        }

        /**
         * Set button text.
         *
         * @param text {@link String} resource of text.
         * @return Config
         */
        public Config inputText(String text) {
            this.buttonTextStr = text;
            return this;
        }

        /**
         * Set button text color.
         *
         * @param color {@link ColorInt} resource of text color.
         * @return Config
         */
        public Config buttonColorText(@ColorInt int color) {
            this.buttonColorText = color;
            return this;
        }

        /**
         * Set button background.
         *
         * @param drawable {@link DrawableRes} resource of the background.
         * @return Config
         */
        public Config buttonBackground(@DrawableRes int drawable) {
            this.buttonBackground = drawable;
            return this;
        }

        @Override
        public Infor build() {
            return Infor.newInstance(this);
        }
    }

    /**
     * Interface OnInfoListener.
     */
    public interface OnInfoListener extends OnQuestionListener {
        void onAnswerInfo(int page);
    }
}
