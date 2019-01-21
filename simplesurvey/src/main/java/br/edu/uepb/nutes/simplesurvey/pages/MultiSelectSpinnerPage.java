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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigPage;
import br.edu.uepb.nutes.simplesurvey.base.BasePage;
import br.edu.uepb.nutes.simplesurvey.base.OnPageListener;
import br.edu.uepb.nutes.simplesurvey.ui.MultiSelectSpinner;

/**
 * MultiSelectSpinnerPage implementation.
 */
public class MultiSelectSpinnerPage extends BasePage<MultiSelectSpinnerPage.ConfigPage>
        implements ISlideBackgroundColorHolder {
    private final String TAG = "MultiSelectSpinnerPage";
    private String KEY_ITEMS_MULTI_SELECT_SPINNER;

    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private OnMultiSelectSpinnerListener mListener;
    private List<Integer> oldIndexAnswerValue;
    private MultiSelectSpinnerPage.ConfigPage configPage;
    private MultiSelectSpinner answerMultiSelectSpinner;

    public MultiSelectSpinnerPage() {
    }

    /**
     * New MultiSelectSpinnerPage instance.
     *
     * @param configPage {@link ConfigPage}
     * @return MultiSelectSpinnerPage
     */
    private static MultiSelectSpinnerPage newInstance(ConfigPage configPage) {
        MultiSelectSpinnerPage pageFragment = new MultiSelectSpinnerPage();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIGS_PAGE, configPage);

        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.blockPage();

        // Setting default values
        super.isBlocked = true;
        oldIndexAnswerValue = new ArrayList<>();

        // Retrieving arguments
        if (getArguments() != null && getArguments().size() != 0) {
            this.configPage = (ConfigPage) getArguments().getSerializable(ARG_CONFIGS_PAGE);
            if (this.configPage == null) return;
            super.pageNumber = this.configPage.pageNumber;

            KEY_ITEMS_MULTI_SELECT_SPINNER = "answer_page"
                    .concat(String.valueOf(this.configPage.pageNumber))
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
                        if (!indexItems.isEmpty() && !indexItems.equals(oldIndexAnswerValue)) {
                            oldIndexAnswerValue = indexItems;
                            MultiSelectSpinnerPage.super.unlockPage();
                            if (mListener != null) {
                                mListener.onMultiSelectSpinner(pageNumber, items, indexItems);
                            }
                        } else {
                            MultiSelectSpinnerPage.super.blockPage();
                        }
                    }

                    @Override
                    public void onAddNewItemSuccess(@NonNull String item, @NonNull int indexItem) {
                        MultiSelectSpinnerPage.super.saveItemExtraSharedPreferences(
                                KEY_ITEMS_MULTI_SELECT_SPINNER, item);
                    }

                    @Override
                    public void onAddNewItemCancel() {

                    }
                });
    }

    @Override
    public int getLayout() {
        return this.configPage.layout != 0 ? this.configPage.layout : R.layout.question_multi_select_spinner;
    }

    @Override
    public MultiSelectSpinnerPage.ConfigPage getConfigsPage() {
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
        if (context instanceof OnMultiSelectSpinnerListener) {
            mListener = (OnMultiSelectSpinnerListener) context;
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
        return (this.configPage.colorBackground != 0) ? this.configPage.colorBackground : Color.GRAY;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        if (this.configPage.colorBackground != 0)
            getView().setBackgroundColor(this.configPage.colorBackground);
    }

    @Override
    public void clearAnswer() {
        this.oldIndexAnswerValue = new ArrayList<>();
        this.answerMultiSelectSpinner.clear();

        // Block page
        super.blockPage();
    }

    /**
     * Set answer.
     *
     * @param indexItems @{@link List<Integer>} list answers
     */
    private void setAnswer(List<Integer> indexItems) {
        this.oldIndexAnswerValue = indexItems;
        this.answerMultiSelectSpinner.selection(indexItems);
        super.unlockPage();
    }

    /**
     * Class config page.
     */
    public static class ConfigPage extends BaseConfigPage<ConfigPage> implements Serializable {
        @ColorInt
        private int colorSelectedText;
        @ColorInt
        private  int colorBackgroundTint;
        @StringRes
        private  int hint;
        @StringRes
        private  int messageEmpty;
        @StringRes
        private  int titleDialogAddNewItem;
        private  List<String> items;
        private  List<Integer> indexAnswerInit;
        private  boolean enabledAdNewItem;

        public ConfigPage() {
            this.colorSelectedText = 0;
            this.colorBackgroundTint = 0;
            this.hint = 0;
            this.messageEmpty = 0;
            this.titleDialogAddNewItem = 0;
            this.indexAnswerInit = new ArrayList<>();
            this.enabledAdNewItem = true;
        }

        /**
         * Set items to the spinner.
         *
         * @param items {@link List < String >}
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage items(List<String> items) {
            this.items = items;
            return this;
        }

        /**
         * Set color item selected.
         *
         * @param colorSelectedText @{@link ColorInt} resource of text color.
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage colorSelectedText(@ColorInt int colorSelectedText) {
            this.colorSelectedText = colorSelectedText;
            return this;
        }

        /**
         * Set color background tint.
         * The spinner line and the add new item image will receive this color.
         *
         * @param colorBackgroundTint @{@link ColorInt} resource of text color.
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage colorBackgroundTint(@ColorInt int colorBackgroundTint) {
            this.colorBackgroundTint = colorBackgroundTint;
            return this;
        }

        /**
         * Set hint message.
         *
         * @param hint @{@link StringRes} resource of text.
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage hint(@StringRes int hint) {
            this.hint = hint;
            return this;
        }

        /**
         * Set message dialog add new item.
         *
         * @param titleDialogAddNewItem @{@link StringRes} resource of text.
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage titleDialogAddNewItem(@StringRes int titleDialogAddNewItem) {
            this.titleDialogAddNewItem = titleDialogAddNewItem;
            return this;
        }

        /**
         * Set message empty.
         * Message that will be displayed in the selection dialog when there is no item.
         *
         * @param messageEmpty @{@link StringRes} resource of text.
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage messageEmpty(@StringRes int messageEmpty) {
            this.messageEmpty = messageEmpty;
            return this;
        }

        /**
         * Disable add new item.
         * The button to add new item will be removed from the layout.
         *
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage disableAddNewItem() {
            this.enabledAdNewItem = false;
            return this;
        }

        /**
         * Set answer init.
         *
         * @param indexAnswerInit {@link List<Integer>} items
         * @return ConfigPage
         */
        public MultiSelectSpinnerPage.ConfigPage answerInit(List<Integer> indexAnswerInit) {
            this.indexAnswerInit = indexAnswerInit;
            return this;
        }

        @Override
        public MultiSelectSpinnerPage build() {
            return MultiSelectSpinnerPage.newInstance(this);
        }
    }

    /**
     * Interface OnMultiSelectSpinnerListener.
     */
    public interface OnMultiSelectSpinnerListener extends OnPageListener {
        void onMultiSelectSpinner(int page, List<String> values, List<Integer> indexValues);
    }
}
