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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;
import br.edu.uepb.nutes.simplesurvey.ui.MultiSelectSpinner;

/**
 * MultipleChoice implementation.
 */
public class MultipleChoice extends BaseQuestion<MultipleChoice.Config>
        implements ISlideBackgroundColorHolder {
    private String KEY_ITEMS_MULTI_SELECT_SPINNER;

    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnMultipleListener mListener;
    private List<Integer> oldIndexAnswerValue;
    private Config configPage;
    private MultiSelectSpinner answerMultiSelectSpinner;

    public MultipleChoice() {
    }

    /**
     * New MultipleChoice instance.
     *
     * @param configPage {@link Config}
     * @return MultipleChoice
     */
    private static MultipleChoice builder(Config configPage) {
        MultipleChoice pageFragment = new MultipleChoice();
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
        oldIndexAnswerValue = new ArrayList<>();

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            this.configPage = getArguments().getParcelable(ARG_CONFIGS_PAGE);
            if (this.configPage == null) return;
            super.setPageNumber(this.configPage.getPageNumber());

            KEY_ITEMS_MULTI_SELECT_SPINNER = "answer_page"
                    .concat(String.valueOf(this.configPage.getPageNumber()))
                    .concat("_")
                    .concat(getClass().getName().toLowerCase());
        }
    }

    @Override
    public void initView(View v) {
        this.answerMultiSelectSpinner = v.findViewById(R.id.answer_multi_select_spinner);

        this.answerMultiSelectSpinner.setItems(this.configPage.items);
        this.answerMultiSelectSpinner.setEnabledAddNewItem(this.configPage.enabledAdNewItem);

        if (this.configPage.hint != 0) {
            this.answerMultiSelectSpinner.setHint(
                    getContext().getResources().getString(this.configPage.hint));
        }

        if (this.configPage.messageEmpty != 0) {
            this.answerMultiSelectSpinner.setMessageEmpty(
                    getContext().getResources().getString(this.configPage.messageEmpty));
        }

        if (this.configPage.titleDialogAddNewItem != 0) {
            this.answerMultiSelectSpinner.setTitleDialogAddNewItem(getContext().getResources()
                    .getString(this.configPage.titleDialogAddNewItem));
        }

        if (this.configPage.colorSelectedText != 0) {
            this.answerMultiSelectSpinner.setColorSelectedText(
                    this.configPage.colorSelectedText);
        }

        if (configPage.colorBackgroundTint != 0) {
            this.answerMultiSelectSpinner.setColorBackgroundTint(
                    this.configPage.colorBackgroundTint);
        }

        // init answer
        if (!configPage.indexAnswerInit.isEmpty()) setAnswer(configPage.indexAnswerInit);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (this.answerMultiSelectSpinner == null) return;

        this.answerMultiSelectSpinner.setOnSpinnerListener(
                new MultiSelectSpinner.OnSpinnerListener() {
                    @Override
                    public void onMultiItemSelected(@NonNull List<String> items,
                                                    @NonNull List<Integer> indexItems) {
                        if (!indexItems.isEmpty()) {
                            oldIndexAnswerValue = indexItems;
                            unlockQuestion();
                            if (mListener != null) {
                                mListener.onAnswerMultiple(getQuestionNumber(), items, indexItems);
                                Log.w("SPINNER", Arrays.toString(items.toArray()));
                            }
                            if (configPage.isNextQuestionAuto()) nextQuestion();
                        } else {
                            blockQuestion();
                        }
                    }

                    @Override
                    public void onAddNewItemSuccess(@NonNull String item, @NonNull int indexItem) {
                        MultipleChoice.super.saveItemExtraSharedPreferences(
                                KEY_ITEMS_MULTI_SELECT_SPINNER, item);
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
        return this.answerMultiSelectSpinner;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.answerMultiSelectSpinner != null)
            this.answerMultiSelectSpinner.setOnSpinnerListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMultipleListener) {
            mListener = (OnMultipleListener) context;
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
        return this.configPage.getColorBackground();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (getView() != null) getView().setBackgroundColor(this.configPage.getColorBackground());
    }

    @Override
    public void clearAnswer() {
        this.oldIndexAnswerValue = new ArrayList<>();
        this.answerMultiSelectSpinner.clear();

        // Block page
        super.blockQuestion();
    }

    /**
     * Set answer.
     *
     * @param indexItems @{@link List<Integer>} list answers
     */
    private void setAnswer(List<Integer> indexItems) {
        this.oldIndexAnswerValue = indexItems;
        this.answerMultiSelectSpinner.selection(indexItems);
        super.unlockQuestion();
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<MultipleChoice.Config> implements Parcelable {
        @ColorInt
        private int colorSelectedText;
        @ColorInt
        private int colorBackgroundTint;
        @StringRes
        private int hint;
        @StringRes
        private int messageEmpty;
        @StringRes
        private int titleDialogAddNewItem;
        private List<String> items;
        private List<Integer> indexAnswerInit;
        private boolean enabledAdNewItem;

        public Config() {
            super.layout(R.layout.question_multi_select_spinner);
            this.colorSelectedText = 0;
            this.colorBackgroundTint = 0;
            this.hint = 0;
            this.messageEmpty = 0;
            this.titleDialogAddNewItem = 0;
            this.indexAnswerInit = new ArrayList<>();
            this.enabledAdNewItem = true;
        }

        protected Config(Parcel in) {
            colorSelectedText = in.readInt();
            colorBackgroundTint = in.readInt();
            hint = in.readInt();
            messageEmpty = in.readInt();
            titleDialogAddNewItem = in.readInt();
            items = in.createStringArrayList();
            enabledAdNewItem = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(colorSelectedText);
            dest.writeInt(colorBackgroundTint);
            dest.writeInt(hint);
            dest.writeInt(messageEmpty);
            dest.writeInt(titleDialogAddNewItem);
            dest.writeStringList(items);
            dest.writeByte((byte) (enabledAdNewItem ? 1 : 0));
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
         * @param colorSelectedText @{@link ColorInt} resource of text color.
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
         * @param colorBackgroundTint @{@link ColorInt} resource of text color.
         * @return Config
         */
        public Config inputColorBackgroundTint(@ColorInt int colorBackgroundTint) {
            this.colorBackgroundTint = colorBackgroundTint;
            return this;
        }

        /**
         * Set inputHint message.
         *
         * @param hint @{@link StringRes} resource of text.
         * @return Config
         */
        public Config inputHint(@StringRes int hint) {
            this.hint = hint;
            return this;
        }

        /**
         * Set message dialog add new item.
         *
         * @param titleDialogAddNewItem @{@link StringRes} resource of text.
         * @return Config
         */
        public Config inputTitleDialogAddNewItem(@StringRes int titleDialogAddNewItem) {
            this.titleDialogAddNewItem = titleDialogAddNewItem;
            return this;
        }

        /**
         * Set message empty.
         * Message that will be displayed in the selection dialog when there is no item.
         *
         * @param messageEmpty @{@link StringRes} resource of text.
         * @return Config
         */
        public Config inputMessageEmpty(@StringRes int messageEmpty) {
            this.messageEmpty = messageEmpty;
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

        /**
         * Set answer init.
         *
         * @param indexAnswerInit {@link List<Integer>} inputItems
         * @return Config
         */
        public Config answerInit(List<Integer> indexAnswerInit) {
            this.indexAnswerInit = indexAnswerInit;
            return this;
        }

        @Override
        public MultipleChoice build() {
            return MultipleChoice.builder(this);
        }
    }

    /**
     * Interface OnMultipleListener.
     */
    public interface OnMultipleListener extends OnQuestionListener {
        void onAnswerMultiple(int page, List<String> values, List<Integer> indexValues);
    }
}
