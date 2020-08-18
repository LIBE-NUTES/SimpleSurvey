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
package br.edu.uepb.nutes.simplesurvey.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.R;

public class SelectSpinner extends LinearLayout {
    protected final Context context;
    protected OnSpinnerListener mListener;
    protected AppCompatSpinner mSpinner;
    protected int indexItemSelected;

    protected List<String> items;

    protected CustomSpinnerAdapter mAdapter;
    protected LinearLayout boxButton;
    protected AppCompatImageButton mButton;

    protected String hint;
    protected String titleDialogAddNewItem;
    @ColorInt
    protected int colorSelectedText;
    @ColorInt
    protected int colorBackgroundTint;
    protected boolean enabledAddNewItem;
    protected int textAlign;

    public SelectSpinner(Context context) {
        super(context);
        this.context = context;

        initControl();
    }

    public SelectSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initControl();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectSpinner);
        if (typedArray != null && typedArray.length() > 0) {
            try {
                setItems(typedArray.getTextArray(R.styleable.SelectSpinner_android_entries));
                setHint(typedArray.getString(R.styleable.SelectSpinner_android_hint));
                setColorSelectedText(typedArray.getColor(R.styleable.SelectSpinner_colorSelectedText, Color.GRAY));
                setColorBackgroundTint(typedArray.getColor(R.styleable.SelectSpinner_colorBackgroundTint, Color.GRAY));
                setEnabledAddNewItem(typedArray.getBoolean(R.styleable.SelectSpinner_enabledAddNewItem, true));
                setTextAlign(typedArray.getInt(R.styleable.SelectSpinner_textAlign, 0));
            } finally {
                typedArray.recycle();
            }
        }
    }

    /**
     * Load xml layout and elements.
     */
    private void initControl() {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ui_custom_spinner_layout, this);

        this.indexItemSelected = -1;
        this.enabledAddNewItem = true;
        this.items = new ArrayList<>();
        this.hint = context.getResources().getString(R.string.survey_select_an_answer);
        this.titleDialogAddNewItem = context.getResources().getString(R.string.survey_add_new_item);

        assignUiElements();
        assignListeners();
        initAdapter();
    }

    /**
     * get instance elements.
     */
    private void assignUiElements() {
        this.mSpinner = findViewById(R.id.custom_select_spinner);
        this.boxButton = findViewById(R.id.custom_box_add_item);
        this.mButton = findViewById(R.id.custom_add_item_imageButton);
    }

    /**
     * init handlers
     */
    private void assignListeners() {
        if (this.enabledAddNewItem) mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogAddNewItem();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && mListener != null) {
                    mListener.onItemSelected(
                            String.valueOf(parent.getItemAtPosition(position)),
                            position - 1
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not implemented!
            }
        });
    }

    /**
     * Init adapter spinner.
     */
    private void initAdapter() {
        this.mAdapter = new CustomSpinnerAdapter(getContext(),
                android.R.layout.simple_spinner_item, getItems());
        this.mSpinner.setAdapter(this.mAdapter);

        refreshComponent();
    }

    /**
     * Refresh components.
     */
    private void refreshComponent() {
        if (this.mSpinner == null) return;

        ViewCompat.setBackgroundTintList(this.mSpinner, ColorStateList.valueOf(getColorBackgroundTint()));
        this.mButton.setColorFilter(getColorBackgroundTint());

        if (!this.enabledAddNewItem) this.boxButton.setVisibility(View.GONE);
        else this.boxButton.setVisibility(View.VISIBLE);

        refreshAdapter();
    }

    /**
     * Refresh adapter spinner.
     */
    private void refreshAdapter() {
        this.mAdapter.clear();
        this.mAdapter.addAll(getItems());
    }

    /**
     * Add new item Dynamically.
     *
     * @param item {@link String}
     */
    private void addItem(String item) {
        if (item == null) throw new IllegalArgumentException("item is required! cannot be null.");
        this.items.add(item);

        this.indexItemSelected = this.items.size() - 2;
        this.mSpinner.setSelection(this.indexItemSelected + 1);
        if (this.mListener != null) this.mListener.onItemSelected(item, this.indexItemSelected);

        refreshAdapter();
    }

    /**
     * Clear adapter spinner.
     */
    public void clear() {
        this.mAdapter.clear();
        refreshAdapter();
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
        addHintInItems();
    }

    public void setItems(CharSequence[] entries) {
        this.items = new ArrayList<>();
        if (entries == null) return;

        for (CharSequence c : entries) {
            this.items.add(String.valueOf(c));
        }

        addHintInItems();
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        if (this.hint != null) this.hint = hint;
        addHintInItems();
    }

    public int getColorSelectedText() {
        return this.colorSelectedText;
    }

    public void setColorSelectedText(int colorSelectedText) {
        this.colorSelectedText = colorSelectedText;
        refreshComponent();
    }

    public int getColorBackgroundTint() {
        return this.colorBackgroundTint;
    }

    public void setColorBackgroundTint(int colorBackgroundTint) {
        this.colorBackgroundTint = colorBackgroundTint;
        refreshComponent();
    }

    public int getIndexItemSelected() {
        return this.indexItemSelected;
    }

    public String getItemSelected() {
        if (this.indexItemSelected >= 0 && (this.indexItemSelected + 1) <= this.items.size()) {
            if (this.items.contains(getHint()))
                return this.items.get(this.indexItemSelected + 1);
            else return this.items.get(this.indexItemSelected);
        }
        return null;
    }

    /**
     * Add hint in the list of inputItems.
     */
    private void addHintInItems() {
        if (this.items == null || this.hint == null) return;

        this.items.remove(getHint());
        this.items.add(0, getHint());
        refreshComponent();
    }

    public void setEnabledAddNewItem(boolean enabled) {
        this.enabledAddNewItem = enabled;
        refreshComponent();
    }

    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
        initAdapter();
    }


    /**
     * Set {@link OnSpinnerListener}
     *
     * @param listener OnSpinnerListener
     */
    public void setOnSpinnerListener(OnSpinnerListener listener) {
        this.mListener = listener;
    }

    /**
     * Select item in list.
     *
     * @param index int
     */
    public void selection(int index) {
        if (index >= 0 && (index + 1) <= this.items.size()) {
            this.indexItemSelected = index;
            this.mSpinner.setSelection(index + 1);
        }
    }

    /**
     * Select item in list.
     *
     * @param value {@link String}
     */
    public void selection(String value) {
        if (value != null && !mAdapter._items.isEmpty()) {
            int index = mAdapter._items.indexOf(value);
            selection(index);
        }
    }

    /**
     * open dialog to add new item.
     */
    public void openDialogAddNewItem() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        if (this.titleDialogAddNewItem != null) alertBuilder.setTitle(this.titleDialogAddNewItem);

        // Creating EditText
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setSingleLine();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 40;
        params.rightMargin = 40;
        params.topMargin = 20;
        input.setLayoutParams(params);
        FrameLayout layout = new FrameLayout(context);
        layout.addView(input);
        alertBuilder.setView(layout);

        // Action add
        alertBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String valueItem = String.valueOf(input.getText()).trim();
                if (valueItem.isEmpty() || items.contains(valueItem)) {
                    if (mListener != null) mListener.onAddNewItemCancel();
                } else {
                    addItem(valueItem);
                    if (mListener != null)
                        mListener.onAddNewItemSuccess(valueItem, indexItemSelected);
                }
            }
        });

        // Action cancel
        alertBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) mListener.onAddNewItemCancel();
            }
        });

        // Cancellation action for any other reason.
        alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mListener != null) mListener.onAddNewItemCancel();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    closeKeyBoard();
                }
            });
        }

        alertBuilder.show();
        openKeyBoard();
    }

    /**
     * Open Keyboard.
     */
    private void openKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * Close Keyboard.
     */
    private void closeKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) (this.context)
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("indexItemSelected", getIndexItemSelected());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("superState");
            selection(bundle.getInt("indexItemSelected"));
        }
        super.onRestoreInstanceState(state);
    }

    class CustomSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {
        private final Context context;
        private List<String> _items;

        public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> _items) {
            super(context, resource, _items);
            this.context = context;
            this._items = _items;
        }

        public int getCount() {
            return _items.size();
        }

        public String getItem(int i) {
            return _items.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView txt = new TextView(context);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(_items.get(position));
            TextViewCompat.setTextAppearance(txt, android.R.style.TextAppearance_Medium);

            // Set color hint message
            if (position == 0) {
                txt.setPadding(40, 30, 50, 0);
                txt.setTextColor(Color.GRAY);
            } else {
                txt.setPadding(50, 30, 50, 30);
                txt.setTextColor(Color.BLACK);
            }

            return txt;
        }

        @SuppressLint("InflateParams")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView txt = new TextView(context);
            txt.setText(_items.get(position));
            txt.setTextSize(16);
            txt.setTextColor(getColorSelectedText());

            // text align
            if (textAlign == 1) txt.setGravity(Gravity.CENTER);
            else if (textAlign == 2) txt.setGravity(Gravity.END);
            else txt.setGravity(Gravity.START);

            return txt;
        }

        @Override
        public boolean isEnabled(int position) {
            // If the spinner has hint, disable it
            return position != 0;
        }
    }

    /**
     * Listener spinner.
     */
    public interface OnSpinnerListener {
        void onItemSelected(String item, int indexItem);

        void onAddNewItemSuccess(String item, int indexItem);

        void onAddNewItemCancel();
    }
}
