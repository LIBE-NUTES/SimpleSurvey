package br.edu.uepb.nutes.simplesurvey.question;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TimePicker;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;
import br.edu.uepb.nutes.simplesurvey.base.OnQuestionListener;

import static br.edu.uepb.nutes.simplesurvey.question.Single.ARG_CONFIGS_PAGE;

public class Time extends BaseQuestion<Time.Config> implements ISlideBackgroundColorHolder, View.OnClickListener {

    private EditText editTime;
    private int mHour, mMinute;
    private Config configPage;
    private OnTimeListener mListener;

    public Time() {
    }

    /**
     * New Time instance.
     *
     * @param configPage {@link Config}
     * @return Time
     */
    private static Time builder(Config configPage) {
        Time pageFragment = new Time();
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

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            if (configPage.answerInit > 0) {
                c.setTimeInMillis(configPage.answerInit);
            }

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            String timeStr = timeFormat(c.getTimeInMillis());
                            setAnswer(timeStr);

                            mListener.onAnswerTime(configPage.getPageNumber(), timeStr, c);
                        }
                    }, mHour, mMinute, configPage.enable24Hours);
            timePickerDialog.show();
        }
    }


    @Override
    public void initView(View v) {
        // Initialize components
        this.editTime = v.findViewById(R.id.answer_text_box);

        this.editTime.setOnClickListener(this);

        if (editTime != null) {
            editTime.setImeOptions(EditorInfo.IME_ACTION_DONE);

            if (configPage.hintStr != null && !configPage.hintStr.isEmpty()) {
                editTime.setHint(configPage.hintStr);
            } else if (configPage.hint != 0) {
                editTime.setHint(configPage.hint);
            }

            if (configPage.answerInit > 0) {
                editTime.setText(timeFormat(configPage.answerInit));
            }

            if (configPage.colorBackgroundTint != 0) {
                ViewCompat.setBackgroundTintList(editTime,
                        ColorStateList.valueOf(configPage.colorBackgroundTint));
            }

            if (configPage.colorText != 0) {
                editTime.setTextColor(configPage.colorText);
                editTime.setHintTextColor(configPage.colorText);
            }
        }
    }

    @Override
    public int getLayout() {
        return configPage.getLayout();
    }

    @Override
    public Time.Config getConfigsQuestion() {
        return this.configPage;
    }

    @Override
    public View getComponentAnswer() {
        return editTime;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Time.OnTimeListener) {
            mListener = (Time.OnTimeListener) context;
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
        editTime.setText("");
        editTime.setHint(configPage.hint);
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
        if (value != null && !value.isEmpty()) editTime.setText(value);
    }

    /**
     * sets the time format.
     */
    private String timeFormat(long millis) {
        String format_hour = configPage.formatHour;

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(millis);

        return new SimpleDateFormat(format_hour, Locale.getDefault()).format(calendar.getTime());
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
        private long answerInit;
        private boolean enable24Hours;
        private String formatHour;

        public Config() {
            super.layout(R.layout.question_time_layout);
            this.colorBackgroundTint = 0;
            this.hint = R.string.select_time;
            this.answerInit = -1;
            this.enable24Hours = false;
            this.formatHour = "hh:mm a";
        }

        protected Config(Parcel in) {
            colorText = in.readInt();
            colorBackgroundTint = in.readInt();
            hint = in.readInt();
            answerInit = in.readLong();
            hintStr = in.readString();
            enable24Hours = Boolean.parseBoolean(in.readString());
            formatHour = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(colorText);
            dest.writeInt(colorBackgroundTint);
            dest.writeInt(hint);
            dest.writeLong(answerInit);
            dest.writeString(hintStr);
            dest.writeString(String.valueOf(enable24Hours));
            dest.writeString(formatHour);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
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
         * Enable 24 hours format.
         *
         * @return Config
         */
        public Config enable24Hours() {
            this.enable24Hours = true;
            this.formatHour = "HH:mm";
            return this;
        }

        /**
         * set time format.
         *
         * @return Config
         */
        public Config formatSelectedTime(String format) {
            this.formatHour = format;
            return this;
        }

        /**
         * Set answer init.
         * Hour in HH:mm format.
         *
         * @param time {@link long} answer.
         * @return Config
         */
        public Config answerInit(String time) {
            SimpleDateFormat f = new SimpleDateFormat("HH:mm", Locale.getDefault());

            try {
                this.answerInit = f.parse(time).getTime();
            } catch (ParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
            return this;
        }

        /**
         * Set answer init.
         *
         * @param milliseconds {@link long} answer.
         * @return Config
         */
        public Config answerInit(long milliseconds) {
            this.answerInit = milliseconds;
            return this;
        }

        @Override
        public Time build() {
            return Time.builder(this);
        }

    }

    /**
     * Interface OnTimeListener.
     */
    public interface OnTimeListener extends OnQuestionListener {
        void onAnswerTime(int page, String value, Calendar calendar);
    }
}
