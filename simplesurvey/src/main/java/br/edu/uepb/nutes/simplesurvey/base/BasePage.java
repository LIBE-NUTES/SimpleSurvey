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
package br.edu.uepb.nutes.simplesurvey.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroViewPager;

import java.util.ArrayList;
import java.util.List;

import br.edu.uepb.nutes.simplesurvey.R;

/**
 * BasePage implementation.
 *
 * @param <T>
 */
public abstract class BasePage<T extends BaseConfigPage> extends Fragment implements IBasePage<T> {
    protected boolean isBlocked;
    protected int pageNumber;
    protected OnPageListener mPageListener;

    private final String SEPARATOR_ITEMS = "#";

    public TextView titleTextView;
    public TextView descTextView;
    public PhotoView questionImageView;
    public ImageButton closeImageButton;
    public LinearLayout boxTitle;
    public LinearLayout boxDescription;
    public LinearLayout boxImage;
    public LinearLayout boxInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        // Initialize components
        this.titleTextView = view.findViewById(R.id.question_title);
        this.descTextView = view.findViewById(R.id.question_description);
        this.questionImageView = view.findViewById(R.id.question_image);
        this.closeImageButton = view.findViewById(R.id.close_imageButton);
        this.boxTitle = view.findViewById(R.id.box_title);
        this.boxDescription = view.findViewById(R.id.box_description);
        this.boxImage = view.findViewById(R.id.box_image);
        this.boxInput = view.findViewById(R.id.box_input);

        if (boxTitle != null && titleTextView != null) {
            if (getConfigsPage().title != 0) {
                titleTextView.setText(getConfigsPage().title);
            } else if (getConfigsPage().titleStr != null && !getConfigsPage().titleStr.isEmpty()) {
                titleTextView.setText(getConfigsPage().titleStr);
            } else {
                boxTitle.setVisibility(View.GONE);
            }

            if (getConfigsPage().titleColor != 0) {
                titleTextView.setTextColor(getConfigsPage().titleColor);
            }
        }

        if (boxDescription != null && descTextView != null) {
            if (getConfigsPage().description != 0) {
                descTextView.setText(getConfigsPage().description);
            } else if (getConfigsPage().descriptionStr != null
                    && !getConfigsPage().descriptionStr.isEmpty()) {
                descTextView.setText(getConfigsPage().descriptionStr);
            } else {
                boxDescription.setVisibility(View.GONE);
            }

            if (getConfigsPage().descriptionColor != 0) {
                descTextView.setTextColor(getConfigsPage().descriptionColor);
            }
        }

        if (closeImageButton != null) {
            if (getConfigsPage().drawableClose != 0) {
                closeImageButton.setImageResource(getConfigsPage().drawableClose);
                closeImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPageListener.onClosePage();
                    }
                });
            } else {
                closeImageButton.setVisibility(View.GONE);
            }
        }

        if (boxImage != null && questionImageView != null) {
            if (getConfigsPage().image != 0) {
                questionImageView.setImageResource(getConfigsPage().image);

                // enable/disable zoom
                questionImageView.setZoomable(!getConfigsPage().zoomDisabled);
            } else boxImage.setVisibility(View.GONE);
        }

        initView(view);

        return view;
    }

    /**
     * Get instance the library app intro.
     *
     * @return AppIntro
     */
    private AppIntro getAppIntroInstance() {
        return (AppIntro) getContext();
    }

    /**
     * Get instance current page.
     *
     * @return AppIntroViewPager
     */
    private AppIntroViewPager getPageInstance() {
        return getAppIntroInstance().getPager();
    }

    @Override
    public void blockPage() {
        this.isBlocked = true;
        getAppIntroInstance().setNextPageSwipeLock(this.isBlocked);
    }

    @Override
    public void unlockPage() {
        this.isBlocked = false;
        getAppIntroInstance().setNextPageSwipeLock(this.isBlocked);
    }

    /**
     * Next page.
     */
    @Override
    public void nextPage() {
        unlockPage();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getPageInstance().goToNextSlide();
            }
        });
    }

    /**
     * Check if page is blocked.
     *
     * @return boolean
     */
    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * Select page number.
     *
     * @return int
     */
    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Retrieve extra items saved in sharedPreferences.
     *
     * @param key {@link String}
     * @return List<String>
     */
    public List<String> getItemsExtraSharedPreferences(String key) {
//        String extra = session.getString(key);
//        return Arrays.asList(extra.split(SEPARATOR_ITEMS));
        return null;
    }

    /**
     * Save extra items in sharedPreferences.
     * Items are saved as {@link String} separated by {@link #SEPARATOR_ITEMS}
     *
     * @param key  {@link String}
     * @param item {@link String}
     * @return boolean
     */
    public boolean saveItemExtraSharedPreferences(String key, String item) {
//        if (getItemsExtraSharedPreferences(key).size() > 0)
//            return session.putString(key, session.getString(key)
//                    .concat(SEPARATOR_ITEMS).concat(item));
//        return session.putString(key, item);
        return false;
    }

    /**
     * Remove all items in sharedPreferences.
     *
     * @param key  {@link String}
     * @param item {@link String}
     */
    public void removeItemExtraSharedPreferences(String key, String item) {
        List<String> _temp = new ArrayList<>(getItemsExtraSharedPreferences(key));
        if (_temp.size() <= 0) return;

        _temp.remove(new String(item));
        if (_temp.equals(getItemsExtraSharedPreferences(key))) return;

        /**
         * Clean items.
         * Save items without the item removed.
         */
        cleanItemsExtraSharedPreferences(key);
        for (String s : _temp)
            saveItemExtraSharedPreferences(key, s);
    }

    /**
     * Remove all items in sharedPreferences.
     *
     * @param key {@link String}
     * @return boolean
     */
    public boolean cleanItemsExtraSharedPreferences(String key) {
//        return session.removeString(key);
        return false;
    }

}
