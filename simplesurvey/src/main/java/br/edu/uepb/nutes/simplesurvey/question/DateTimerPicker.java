package br.edu.uepb.nutes.simplesurvey.question;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

import java.util.Calendar;

import br.edu.uepb.nutes.simplesurvey.R;
import br.edu.uepb.nutes.simplesurvey.base.BaseConfigQuestion;
import br.edu.uepb.nutes.simplesurvey.base.BaseQuestion;

import static br.edu.uepb.nutes.simplesurvey.question.Single.ARG_CONFIGS_PAGE;

public class DateTimerPicker extends BaseQuestion<DateTimerPicker.Config> implements ISlideBackgroundColorHolder, View.OnClickListener {


    private Button btnDatePicker, btnTimePicker;
    private TextView txtDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Config configPage;

    public DateTimerPicker() {
    }

    /**
     * New DateTimerPicker instance.
     *
     * @param configPage {@link Config}
     * @return DateTimerPicker
     */
    private static DateTimerPicker builder(Config configPage) {
        DateTimerPicker pageFragment = new DateTimerPicker();
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

        if (id == R.id.btn_date) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
//        if (id == R.id.btn_time) {
//
//            // Get Current Time
//            final Calendar c = Calendar.getInstance();
//            mHour = c.get(Calendar.HOUR_OF_DAY);
//            mMinute = c.get(Calendar.MINUTE);
//
//            // Launch Time Picker Dialog
//            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
//                    new TimePickerDialog.OnTimeSetListener() {
//
//                        @Override
//                        public void onTimeSet(TimePicker view, int hourOfDay,
//                                              int minute) {
//
//                            txtTime.setText(hourOfDay + ":" + minute);
//                        }
//                    }, mHour, mMinute, false);
//            timePickerDialog.show();
//        }
    }


    @Override
    public void clearAnswer() {

    }

    @Override
    public void initView(View v) {
        // Initialize components
        this.btnDatePicker = v.findViewById(R.id.btn_date);
//        this.btnTimePicker = v.findViewById(R.id.btn_time);
        this.txtDate = v.findViewById(R.id.in_date);
//        this.txtTime = v.findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
//        btnTimePicker.setOnClickListener(this);


        if (this.configPage.background != 0) {
            this.btnDatePicker.setBackgroundResource(this.configPage.background);
        }

        if (this.configPage.background != 0) {
            this.btnTimePicker.setBackgroundResource(this.configPage.background);
        }

//        if (this.configPage.buttonText != 0) {
//            this.button.setText(this.configPage.buttonText);
//        } else if (this.configPage.buttonTextStr != null &&
//                !this.configPage.buttonTextStr.isEmpty()) {
//            this.button.setText(this.configPage.buttonTextStr);
//        }

        if (configPage.colorText != 0) {
            this.btnDatePicker.setTextColor(this.configPage.colorText);
        }
        if (configPage.colorText != 0) {
            this.btnTimePicker.setTextColor(this.configPage.colorText);
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
        return null;
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

    /**
     * Class config page.
     */
    public static class Config extends BaseConfigQuestion<Config> implements Parcelable {
        @DrawableRes
        private int background;
        @ColorInt
        int colorText, colorBackgroundTint;
        @StringRes
        private int hint;
        private String answerInit, hintStr;
        private int inputType;

        public Config() {
            super.layout(R.layout.question_date_layout);
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
        public DateTimerPicker build() {
            return DateTimerPicker.builder(this);
        }
    }
}
