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
public class InforPage extends BasePage<InforPage.ConfigPage> implements ISlideBackgroundColorHolder {
    private final String TAG = "InforPage";

    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnButtonListener mListener;
    private InforPage.ConfigPage configPage;
    private AppCompatButton button;

    public InforPage() {
    }

    /**
     * New InforPage instance.
     *
     * @param configPage {@link ConfigPage}
     * @return InforPage
     */
    private static InforPage newInstance(ConfigPage configPage) {
        InforPage pageFragment = new InforPage();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.blockPage();

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            configPage = (ConfigPage) getArguments().getSerializable(ARG_CONFIGS_PAGE);
            if (configPage == null) return;
            super.pageNumber = configPage.pageNumber;
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
        return configPage.layout != 0 ? configPage.layout : R.layout.page_infor;
    }

    @Override
    public InforPage.ConfigPage getConfigsPage() {
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
        return (configPage.colorBackground != 0) ? configPage.colorBackground : Color.GRAY;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (configPage.colorBackground != 0 && getView() != null) {
            getView().setBackgroundColor(configPage.colorBackground);
        }
    }

    @Override
    public void clearAnswer() {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    /**
     * Class config page.
     */
    public static class ConfigPage extends BaseConfigPage<ConfigPage> implements Serializable {
        private int buttonText,
                buttonColorText,
                buttonBackground;

        public ConfigPage() {
            this.buttonText = 0;
            this.buttonColorText = 0;
            this.buttonBackground = 0;
        }

        /**
         * Set button text.
         *
         * @param buttonText @{@link StringRes} resource of text.
         * @return ConfigPage
         */
        public ConfigPage buttonText(@StringRes int buttonText) {
            this.buttonText = buttonText;
            return this;
        }

        /**
         * Set button text color.
         *
         * @param buttonColorText @{@link ColorInt} resource of text color.
         * @return ConfigPage
         */
        public ConfigPage buttonColorText(@ColorInt int buttonColorText) {
            this.buttonColorText = buttonColorText;
            return this;
        }

        /**
         * Set button background.
         *
         * @param buttonBackground @{@link DrawableRes} resource of the background.
         * @return ConfigPage
         */
        public ConfigPage buttonBackground(@DrawableRes int buttonBackground) {
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
