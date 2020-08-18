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

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import br.edu.uepb.nutes.simplesurvey.R;

/**
 * BaseConfigQuestion implementation.
 *
 * @param <T>
 */
public abstract class BaseConfigQuestion<T> {
    @LayoutRes
    private int layout;
    @StringRes
    private int title, description;
    @DrawableRes
    private int image, drawableClose;
    @ColorRes
    private int colorBackground, titleColor, descriptionColor;
    private boolean zoomDisabled, nextQuestionAuto, isRequired;
    private String titleStr, descriptionStr;
    private int pageNumber, titleTextSize, descriptionTextSize;
    private int textAlign;

    protected BaseConfigQuestion() {
        this.layout = 0;
        this.title = 0;
        this.titleTextSize = 0;
        this.description = 0;
        this.descriptionTextSize = 0;
        this.image = 0;
        this.colorBackground = Color.WHITE;
        this.titleColor = Color.BLACK;
        this.descriptionColor = Color.BLACK;
        this.drawableClose = R.drawable.ic_action_close_light;
        this.zoomDisabled = true;
        this.nextQuestionAuto = false;
        this.isRequired = true;
        this.textAlign = TextAlign.START;
    }

    public int getLayout() {
        return layout;
    }

    public int getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public int getDrawableClose() {
        return drawableClose;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getDescriptionColor() {
        return descriptionColor;
    }

    public boolean isZoomDisabled() {
        return zoomDisabled;
    }

    public boolean isNextQuestionAuto() {
        return nextQuestionAuto;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public String getDescriptionStr() {
        return descriptionStr;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public int getDescriptionTextSize() {
        return descriptionTextSize;
    }

    public int getTextAlign() {
        return textAlign;
    }

    /**
     * Set resource layout.
     *
     * @param layout @{@link LayoutRes}
     * @return T
     */
    public T layout(@LayoutRes int layout) {
        this.layout = layout;
        return (T) this;
    }

    /**
     * Set title.
     *
     * @param text int
     * @return T
     */
    public T title(@StringRes int text) {
        this.title = text;
        return (T) this;
    }

    /**
     * Set title.
     *
     * @param text {@link String}
     * @return T
     */
    public T title(String text) {
        this.titleStr = text;
        return (T) this;
    }

    /**
     * Set description.
     *
     * @param text int
     * @return T
     */
    public T description(@StringRes int text) {
        this.description = text;
        return (T) this;
    }

    /**
     * Set description.
     *
     * @param text {@link String}
     * @return T
     */
    public T description(String text) {
        this.descriptionStr = text;
        return (T) this;
    }

    /**
     * Set title and color.
     *
     * @param text  Title resource
     * @param color Title color
     * @return T
     */
    public T title(@StringRes int text, @ColorInt int color) {
        this.title = text;
        this.titleColor = color;
        return (T) this;
    }

    /**
     * Set title and color.
     *
     * @param text  {@link String}
     * @param color int
     * @return T
     */
    public T title(String text, @ColorInt int color) {
        this.titleStr = text;
        this.titleColor = color;
        return (T) this;
    }

    /**
     * Set title and color.
     *
     * @param text  Title resource
     * @param color Title color
     * @param size  Title size in sp
     * @return T
     */
    public T title(@StringRes int text, @ColorInt int color, int size) {
        this.title = text;
        this.titleColor = color;
        this.titleTextSize = size;
        return (T) this;
    }

    /**
     * Set title size in sp.
     *
     * @param size int Font size in sp
     * @return T
     */
    public T titleTextSize(int size) {
        this.titleTextSize = size;
        return (T) this;
    }

    /**
     * Set description, color and text size.
     *
     * @param text  Description text
     * @param color Description color
     * @param size  Description size in sp
     * @return T
     */
    public T description(@StringRes int text, @ColorInt int color, int size) {
        this.description = text;
        this.descriptionColor = color;
        this.descriptionTextSize = size;
        return (T) this;
    }

    /**
     * Set description and color.
     *
     * @param text  {@link String}
     * @param color int
     * @return T
     */
    public T description(@StringRes int text, @ColorInt int color) {
        this.description = text;
        this.descriptionColor = color;
        return (T) this;
    }


    /**
     * Set description and color.
     *
     * @param text  {@link String}
     * @param color int
     * @return T
     */
    public T description(String text, @ColorInt int color) {
        this.descriptionStr = text;
        this.descriptionColor = color;
        return (T) this;
    }

    /**
     * Set description font size in sp.
     *
     * @param size Font size in sp.
     * @return T
     */
    public T descriptionTextSize(int size) {
        this.descriptionTextSize = size;
        return (T) this;
    }

    /**
     * Set image image.
     *
     * @param drawable
     * @return T
     */
    public T image(@DrawableRes int drawable) {
        this.image = drawable;
        return (T) this;
    }

    /**
     * Set background color
     *
     * @param color
     * @return T
     */
    public T colorBackground(@ColorInt int color) {
        this.colorBackground = color;
        return (T) this;
    }

    /**
     * Set title color.
     *
     * @param color
     * @return T
     */
    public T titleColor(@ColorInt int color) {
        this.titleColor = color;
        return (T) this;
    }

    /**
     * Set description color.
     *
     * @param color
     * @return T
     */
    public T descriptionColor(@ColorInt int color) {
        this.descriptionColor = color;
        return (T) this;
    }

    /**
     * Set page number.
     *
     * @param number
     * @return T
     */
    public T pageNumber(int number) {
        this.pageNumber = number;
        return (T) this;
    }

    /**
     * Set image button close.
     *
     * @param drawable
     * @return T
     */
    public T buttonClose(int drawable) {
        this.drawableClose = drawable;
        return (T) this;
    }

    /**
     * Enable zoom in question image.
     *
     * @return Config
     */
    public T enableZoomImage() {
        this.zoomDisabled = false;
        return (T) this;
    }

    /**
     * Activate next page/question automatically after a valid response.
     *
     * @return Config
     */
    public T nextQuestionAuto() {
        this.nextQuestionAuto = true;
        return (T) this;
    }

    /**
     * Set text align
     *
     * @param align int {@link TextAlign}
     * @return T
     */
    public T inputTextAlign(int align) {
        this.textAlign = align;
        return (T) this;
    }

//    /**
//     * Enable answer required.
//     *
//     * @return Config
//     */
//    public T answerRequired() {
//        this.isRequired = true;
//        return (T) this;
//    }

    public abstract Fragment build();
}

