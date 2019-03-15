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
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.List;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;
import br.edu.uepb.nutes.simplesurvey.ui.SelectSpinner;

/**
 * SingleChoice implementation.
 */
public class SingleChoice extends BaseQuestion<SingleChoice.Config> implements ISlideBackgroundColorHolder {
    protected static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnSingleListener mListener;
    private int oldIndexAnswerValue;
    private Config configPage;
    private SelectSpinner answerSelectSpinner;

    public SingleChoice() {
    }

    /**
     * New SingleChoice instance.
     *
     * @param configPage
     * @return SingleChoice
     */
    private static SingleChoice builder(Config configPage) {
        SingleChoice pageFragment = new SingleChoice();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.blockQuestion();
        this.oldIndexAnswerValue = -1;

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            this.configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            super.setPageNumber(this.configPage.getPageNumber());

            // set inputHint
            configPage.items.add(0, getContext().getResources().getString(this.configPage.hint));
        }
    }

    @Override
    public void initView(View v) {
        this.answerSelectSpinner = v.findViewById(R.id.answer_spinner);

        this.answerSelectSpinner.setItems(this.configPage.items);
        this.answerSelectSpinner.setEnabledAddNewItem(this.configPage.enabledAdNewItem);

        if (configPage.hintStr != null && !configPage.hintStr.isEmpty()) {
            this.answerSelectSpinner.setHint(configPage.hintStr);
        } else if (this.configPage.hint != 0) {
            this.answerSelectSpinner.setHint(getContext()
                    .getResources().getString(this.configPage.hint));
        }

        if (this.configPage.colorSelectedText != 0)
            this.answerSelectSpinner.setColorSelectedText(this.configPage.colorSelectedText);

        if (configPage.colorBackgroundTint != 0)
            this.answerSelectSpinner.setColorBackgroundTint(this.configPage.colorBackgroundTint);

        // init answer
        if (this.configPage.indexAnswerInit != -1)
            this.setAnswer(this.configPage.indexAnswerInit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.answerSelectSpinner == null) return;

        this.answerSelectSpinner.setOnSpinnerListener(new SelectSpinner.OnSpinnerListener() {
            @Override
            public void onItemSelected(String item, int indexItem) {
                if (indexItem != oldIndexAnswerValue) {
                    oldIndexAnswerValue = indexItem;
                    unlockQuestion();
                    if (configPage.isNextQuestionAuto()) nextQuestion();
                    if (mListener != null) {
                        mListener.onAnswerSingle(getQuestionNumber(), item, indexItem);
                    }
                }
            }

            @Override
            public void onAddNewItemSuccess(String item, int indexItem) {

            }

            @Override
            public void onAddNewItemCancel() {

            }
        });
    }

    @Override
    public int getLayout() {
        return this.configPage.getLayout();
    }

    @Override
    public Config getConfigsQuestion() {
        return this.configPage;
    }

    @Override
    public View getComponentAnswer() {
        return this.answerSelectSpinner;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.answerSelectSpinner != null) this.answerSelectSpinner.setOnSpinnerListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSingleListener) {
            this.mListener = (OnSingleListener) context;
            super.setListener(this.mListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return this.configPage.getColorBackground();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (getView() != null) getView().setBackgroundColor(this.configPage.getColorBackground());
    }

    @Override
    public void clearAnswer() {
        this.oldIndexAnswerValue = -1;
        this.answerSelectSpinner.clear();

        // Block page
        super.blockQuestion();
    }

    /**
     * Set answer.
     *
     * @param indexValue index
     */
    private void setAnswer(int indexValue) {
        this.answerSelectSpinner.selection(indexValue);
        this.oldIndexAnswerValue = this.answerSelectSpinner.getIndexItemSelected(); // position 0 inputHint

        super.unlockQuestion();
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<SingleChoice.Config> implements Parcelable {
        @ColorInt
        private int colorSelectedText, colorBackgroundTint;
        @StringRes
        private int hint;
        private List<String> items;
        private int indexAnswerInit;
        private boolean enabledAdNewItem;
        private String hintStr;

        public Config() {
            super.layout(R.layout.question_select_spinner);
            this.colorSelectedText = 0;
            this.colorBackgroundTint = 0;
            this.hint = R.string.survey_select_an_answer;
            this.indexAnswerInit = -1;
            this.enabledAdNewItem = true;
        }

        protected Config(Parcel in) {
            colorSelectedText = in.readInt();
            colorBackgroundTint = in.readInt();
            hint = in.readInt();
            items = in.createStringArrayList();
            indexAnswerInit = in.readInt();
            enabledAdNewItem = in.readByte() != 0;
            hintStr = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(colorSelectedText);
            dest.writeInt(colorBackgroundTint);
            dest.writeInt(hint);
            dest.writeStringList(items);
            dest.writeInt(indexAnswerInit);
            dest.writeByte((byte) (enabledAdNewItem ? 1 : 0));
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
         * Set inputItems to the spinner.
         *
         * @param items {@link List < String >}
         * @return Config
         */
        public Config inputItems(List<String> items) {
            this.items = items;
            return this;
        }

        /**
         * Set color item selected.
         *
         * @param colorSelectedText @{@link ColorInt} resource color.
         * @return Config
         */
        public Config inputColorSelectedText(@ColorInt int colorSelectedText) {
            this.colorSelectedText = colorSelectedText;
            return this;
        }

        /**
         * Set color background tint.
         * The spinner line and the add new item image will receive this color.
         *
         * @param colorBackgroundTint @{@link ColorInt} resource color.
         * @return Config
         */
        public Config inputColorBackgroundTint(@ColorInt int colorBackgroundTint) {
            this.colorBackgroundTint = colorBackgroundTint;
            return this;
        }

        /**
         * Set inputHint message.
         *
         * @param hint @{@link StringRes} resource color.
         * @return Config
         */
        public Config inputHint(@StringRes int hint) {
            this.hint = hint;
            return this;
        }

        /**
         * Set hint message.
         *
         * @param hint {@String}
         * @return Config
         */
        public Config inputHint(String hint) {
            this.hintStr = hint;
            return this;
        }

        /**
         * Set answer init.
         *
         * @param indexAnswerInit @{@link ColorInt} index.
         * @return Config
         */
        public Config answerInit(int indexAnswerInit) {
            this.indexAnswerInit = indexAnswerInit;
            return this;
        }

        /**
         * Disable add new item.
         * The button to add new item will be removed from the layout.
         *
         * @return Config
         */
        public Config inputDisableAddNewItem() {
            this.enabledAdNewItem = false;
            return this;
        }

        @Override
        public SingleChoice build() {
            return SingleChoice.builder(this);
        }
    }

    /**
     * Interface OnSingleListener.
     */
    public interface OnSingleListener extends OnQuestionListener {
        void onAnswerSingle(int page, String value, int indexValue);
    }
}
