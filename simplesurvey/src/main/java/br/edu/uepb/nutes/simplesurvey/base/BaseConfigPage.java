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

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

import androidx.fragment.app.Fragment;

/**
 * BaseConfigPage implementation.
 * @param <T>
 */
public abstract class BaseConfigPage<T> {
    public String titleStr,
            descriptionStr;

    public int layout,
            title,
            description,
            image,
            colorBackground,
            titleColor,
            descriptionColor,
            pageNumber,
            drawableClose,
            drawablePause;

    public boolean zoomDisabled;

    public BaseConfigPage() {
        this.layout = 0;
        this.title = 0;
        this.description = 0;
        this.image = 0;
        this.colorBackground = 0;
        this.titleColor = 0;
        this.descriptionColor = 0;
        this.drawableClose = 0;
        this.drawablePause = 0;
        this.zoomDisabled = true;
    }

    /**
     * Set resource layout.
     *
     * @param layout
     * @return T
     */
    public T layout(@LayoutRes int layout) {
        this.layout = layout;
        return (T) this;
    }

    /**
     * Set title.
     *
     * @param title int
     * @return T
     */
    public T title(@StringRes int title) {
        this.title = title;
        return (T) this;
    }

    /**
     * Set title.
     *
     * @param title {@link String}
     * @return T
     */
    public T title(String title) {
        this.titleStr = title;
        return (T) this;
    }

    /**
     * Set description.
     *
     * @param description int
     * @return T
     */
    public T description(@StringRes int description) {
        this.description = description;
        return (T) this;
    }

    /**
     * Set description.
     *
     * @param description {@link String}
     * @return T
     */
    public T description(String description) {
        this.descriptionStr = description;
        return (T) this;
    }

    /**
     * Set title and color.
     *
     * @param title
     * @param titleColor
     * @return T
     */
    public T title(@StringRes int title, @ColorInt int titleColor) {
        this.title = title;
        this.titleColor = titleColor;
        return (T) this;
    }

    /**
     * Set title and color.
     *
     * @param title      {@link String}
     * @param titleColor int
     * @return T
     */
    public T title(String title, @ColorInt int titleColor) {
        this.titleStr = title;
        this.titleColor = titleColor;
        return (T) this;
    }

    /**
     * Set description and color.
     *
     * @param description
     * @param descriptionColor
     * @return T
     */
    public T description(@StringRes int description, @ColorInt int descriptionColor) {
        this.description = description;
        this.descriptionColor = descriptionColor;
        return (T) this;
    }

    /**
     * Set description and color.
     *
     * @param description      {@link String}
     * @param descriptionColor int
     * @return T
     */
    public T description(String description, @ColorInt int descriptionColor) {
        this.descriptionStr = description;
        this.descriptionColor = descriptionColor;
        return (T) this;
    }

    /**
     * Set image image.
     *
     * @param image
     * @return T
     */
    public T image(@DrawableRes int image) {
        this.image = image;
        return (T) this;
    }

    /**
     * Set background color
     *
     * @param colorBackground
     * @return T
     */
    public T colorBackground(@ColorInt int colorBackground) {
        this.colorBackground = colorBackground;
        return (T) this;
    }

    /**
     * Set title color.
     *
     * @param titleColor
     * @return T
     */
    public T titleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return (T) this;
    }

    /**
     * Set description color.
     *
     * @param descriptionColor
     * @return T
     */
    public T descriptionColor(@ColorInt int descriptionColor) {
        this.descriptionColor = descriptionColor;
        return (T) this;
    }

    /**
     * Set page number.
     *
     * @param pageNumber
     * @return T
     */
    public T pageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return (T) this;
    }

    /**
     * Set image button close.
     *
     * @param drawableClose
     * @return T
     */
    public T buttonClose(int drawableClose) {
        this.drawableClose = drawableClose;
        return (T) this;
    }

    /**
     * Set image button pause.
     *
     * @param drawablePause
     * @return T
     */
    public T buttonPause(int drawablePause) {
        this.drawablePause = drawablePause;
        return (T) this;
    }

    /**
     * Enable zoom in question image.
     *
     * @return ConfigPage
     */
    public T enableZoomImage() {
        this.zoomDisabled = false;
        return (T) this;
    }

    public abstract Fragment build();
}
