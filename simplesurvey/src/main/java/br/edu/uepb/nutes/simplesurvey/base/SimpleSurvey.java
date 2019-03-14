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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;

import br.edu.uepb.nutes.simplesurvey.R;

/**
 * Simple Survey implementation.
 * Extend AppInto class by {https://github.com/AppIntro/AppIntro}
 */
public abstract class SimpleSurvey extends AppIntro {
    private final String TAG = "SimpleSurvey";

    private IBaseQuestion currentQuestion;
    private Snackbar snackbarMessageBlockedPage;
    private String messageQuestionBlocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageQuestionBlocked = getString(R.string.message_blocked_page);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Config pages.
        setColorTransitionsEnabled(true);
        setFadeAnimation();
        showSeparator(false);
        showSkipButton(false);
        setNextPageSwipeLock(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setImmersive(true);
        }

        this.initView();
    }

    /**
     * Add question.
     *
     * @param question {@link Fragment}
     */
    public void addQuestion(Fragment question) {
        addSlide(question);
    }

    /**
     * Opens the next page/question.
     */
    public void nextQuestion() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getPager().goToNextSlide();
            }
        });
    }

    /**
     * Opens page/question according to index
     *
     * @param index int
     */
    public void goToQuestion(final int index) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getPager().setCurrentItem(index);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        if (snackbarMessageBlockedPage != null)
            snackbarMessageBlockedPage.dismiss();

        if (newFragment instanceof IBaseQuestion) {
            currentQuestion = (IBaseQuestion) newFragment;
            Log.d(TAG, "onSlideChanged() - isBlocked: "
                    + currentQuestion.isBlocked() + " |  page: " + currentQuestion.getQuestionNumber());

            setNextPageSwipeLock(currentQuestion.isBlocked());
            // Capture event onSwipeLeft
            currentQuestion.getView().setOnTouchListener(new OnSwipeQuestionTouchListener(this) {
                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    if (currentQuestion.isBlocked()) showMessageBlocked();
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
//                    setNextPageSwipeLock(currentQuestion.isBlocked());
                }
            });
        }
    }

    /**
     * Show message page blocked.
     */
    protected void showMessageBlocked() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create snackbar
                snackbarMessageBlockedPage = Snackbar.make(currentQuestion.getView(),
                        messageQuestionBlocked,
                        Snackbar.LENGTH_LONG);
                snackbarMessageBlockedPage.setAction(R.string.text_ok,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbarMessageBlockedPage.dismiss();
                            }
                        });
                snackbarMessageBlockedPage.show();
            }
        });
    }

    /**
     * Set blocked message page/question.
     * Default value: Answer the current question to go to next...
     *
     * @param message int
     */
    public void setMessageBlocked(@StringRes int message) {
        this.messageQuestionBlocked = getString(message);
    }

    /**
     * Set blocked message page/question.
     * Default value: Answer the current question to go to next...
     *
     * @param message {@link String}
     */
    public void setMessageBlocked(@NonNull String message) {
        this.messageQuestionBlocked = message;
    }

    /**
     * Init view
     */
    protected abstract void initView();
}
