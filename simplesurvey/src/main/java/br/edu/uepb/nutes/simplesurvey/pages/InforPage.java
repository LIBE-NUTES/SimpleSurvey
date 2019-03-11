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
package br.edu.uepb.nutes.simplesurvey.pages;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.io.Serializable;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigPage;
import br.edu.uepb.nutes.simplesurvey.base.BasePage;
import br.edu.uepb.nutes.simplesurvey.base.OnPageListener;

/**
 * InforPage implementation.
 * Useful to use as a home screen or thank you screen for participating in the assessment.
 */
public class InforPage extends BasePage<InforPage.Config> implements ISlideBackgroundColorHolder {
    private final String TAG = "InforPage";

    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnButtonListener mListener;
    private Config configPage;
    private AppCompatButton button;

    public InforPage() {
    }

    /**
     * New InforPage instance.
     *
     * @param configPage {@link Config}
     * @return InforPage
     */
    private static InforPage newInstance(Config configPage) {
        InforPage pageFragment = new InforPage();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = (Config) getArguments().getSerializable(ARG_CONFIGS_PAGE);
            if (configPage == null) return;
            super.pageNumber = configPage.getPageNumber();
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
                mListener.onAnswerButton(pageNumber);
            }
        });
    }

    @Override
    public int getLayout() {
        return configPage.getLayout();
    }

    @Override
    public Config getConfigsPage() {
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
        if (context instanceof OnButtonListener) {
            mListener = (OnButtonListener) context;
            super.mPageListener = mListener;
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
    public static class Config extends BaseConfigPage<Config> implements Serializable {
        private int buttonText,
                buttonColorText,
                buttonBackground;

        public Config() {
            super.layout(R.layout.page_infor);
            this.buttonText = 0;
            this.buttonColorText = 0;
            this.buttonBackground = 0;
        }

        /**
         * Set button text.
         *
         * @param buttonText @{@link StringRes} resource of text.
         * @return Config
         */
        public Config buttonText(@StringRes int buttonText) {
            this.buttonText = buttonText;
            return this;
        }

        /**
         * Set button text color.
         *
         * @param buttonColorText @{@link ColorInt} resource of text color.
         * @return Config
         */
        public Config buttonColorText(@ColorInt int buttonColorText) {
            this.buttonColorText = buttonColorText;
            return this;
        }

        /**
         * Set button background.
         *
         * @param buttonBackground @{@link DrawableRes} resource of the background.
         * @return Config
         */
        public Config buttonBackground(@DrawableRes int buttonBackground) {
            this.buttonBackground = buttonBackground;
            return this;
        }

        @Override
        public InforPage build() {
            return InforPage.newInstance(this);
        }
    }

    /**
     * Interface OnRadioListener.
     */
    public interface OnButtonListener extends OnPageListener {
        void onAnswerButton(int page);
    }
}
