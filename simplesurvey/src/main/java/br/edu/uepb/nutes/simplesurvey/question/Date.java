package br.edu.uepb.nutes.simplesurvey.question;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;

public class Date extends BaseQuestion<Date.Config> implements ISlideBackgroundColorHolder, View.OnClickListener {
    private static final String ARG_CONFIGS_PAGE = "arg_configs_page";

    private EditText editDate;
    private Config configPage;
    private OnDateListener mListener;

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
        super.blockQuestion();

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
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            if (configPage.answerInit > 0) {
                c.setTimeInMillis(configPage.answerInit);
            }
            final int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR, mYear);
                            c.set(Calendar.MONTH, mMonth);
                            c.set(Calendar.DAY_OF_MONTH, mDay);

                            String timeStr = dateFormat(c.getTimeInMillis());
                            setAnswer(timeStr);

                            mListener.onAnswerDate(configPage.getPageNumber(),
                                    String.valueOf(editDate.getText()));
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
            if (configPage.answerInit > 0) {
                editDate.setText(dateFormat(configPage.answerInit));
            }

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
        if (context instanceof Date.OnDateListener) {
            mListener = (Date.OnDateListener) context;
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
     * sets the date format.
     */
    private String dateFormat(long date) {
        String format_date = configPage.formatDate;

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(date);

        return new SimpleDateFormat(format_date, Locale.getDefault()).format(calendar.getTime());
    }

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Config> implements Parcelable {

        @ColorInt
        int colorText, colorBackgroundTint;
        @StringRes
        private int hint;
        private String hintStr;
        private String formatDate;
        private long answerInit;

        public Config() {
            super.layout(R.layout.question_date_layout);
            this.colorBackgroundTint = 0;
            this.hint = R.string.select_date;
            this.answerInit = -1;
            this.formatDate = "yyyy-MM-dd";
        }

        protected Config(Parcel in) {
            colorText = in.readInt();
            colorBackgroundTint = in.readInt();
            hint = in.readInt();
            answerInit = in.readLong();
            hintStr = in.readString();
            formatDate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(colorText);
            dest.writeInt(colorBackgroundTint);
            dest.writeInt(hint);
            dest.writeLong(answerInit);
            dest.writeString(hintStr);
            dest.writeString(formatDate);
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
         * Set format date selected .
         *
         * @param format {@link String} format date.
         * @return Config
         */
        public Config formatSelectedDate(String format) {
            this.formatDate = format;
            return this;
        }

        @Override
        public Date build() {
            return Date.builder(this);
        }

        /**
         * Set answer init.
         * Date in dd-MM-yyyy format.
         *
         * @param date {@link long} answer.
         * @return Config
         */
        public Config answerInit(String date) {
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            try {
                this.answerInit = f.parse(date).getTime();
            } catch (ParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
            return this;
        }

        /**
         * Set answer init.
         *
         * @param date {@link String} answer.
         * @return Config
         */
        public Config answerInit(long date) {
            this.answerInit = date;
            return this;
        }
    }


    /**
     * Interface OnDateListener.
     */
    public interface OnDateListener extends OnQuestionListener {
        void onAnswerDate(int page, String value);
    }
}
