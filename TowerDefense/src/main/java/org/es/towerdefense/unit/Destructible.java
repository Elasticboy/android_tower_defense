package org.es.towerdefense.unit;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import org.es.engine.graphics.animation.AnimationCallback;
import org.es.engine.graphics.drawable.DrawableElement;
import org.es.engine.graphics.sprite.Sprite;
import org.es.engine.graphics.utils.DrawTextUtils;
import org.es.engine.graphics.utils.DrawingParam;
import org.es.towerdefense.settings.PreferenceKey;

import static org.es.engine.graphics.utils.DrawTextUtils.HorizontalAlign.CENTER;
import static org.es.engine.graphics.utils.DrawTextUtils.VerticalAlign.TOP;

/**
 * Parent class for all destructible elements.
 *
 * @author Cyril Leroux
 *         Created on 17/02/14.
 */
public class Destructible<AnimationId extends Enum<AnimationId>>
        implements DrawableElement, AnimationCallback {

    /**
     * The sprite is the element drawn on the screen.<br />
     * Its position is not the same as the holder for it depends on the drawing parameters (coefficient and offset).
     */
    private final Sprite<AnimationId> mSprite;
    /** Position on the battleground. Expressed in tiles. */
    private final PointF mPosition;
    /** Width of the destructible element. Expressed in tiles. */
    private final float mWidth;
    /** Height of the destructible element. Expressed in tiles. */
    private final float mHeight;
    /** Weighting of the unit. Used to calculate the unit cost or reward. */
    protected final int mWeight;
    /** Health points of the unit. */
    protected int mHealth;

    public Destructible(Sprite<AnimationId> sprite, float width, float height, int weight, int health) {
        mSprite = sprite;
        mWidth = width;
        mHeight = height;
        mWeight = weight;
        mHealth = health;

        mPosition = new PointF();
    }

    //
    // New functions
    //

    public void receiveDamages(Offensive attacker) {
        mHealth -= attacker.mDamage;
    }

    public boolean isDead() { return mHealth <= 0; }

    // TODO comment
    public boolean isOutOfPlay() {
        return isDead();
    }

    /** Draw the Head-up display. */
    public void drawHUD(Canvas canvas, DrawingParam param) { }

    /** Draw the debug Head-up display. */
    public void drawDebugHUD(Canvas canvas, DrawingParam param, Paint paint, SharedPreferences pref) {

        boolean displayDebugHUD = pref.getBoolean(PreferenceKey.KEY_DESTRUCTIBLE, true);
        if (!displayDebugHUD) { return; }

        // Save and change paint color.
        int initialColor = paint.getColor();
        paint.setColor(Color.RED);

        // Text to display the remaining life of the element.
        final String hpText = String.valueOf(mHealth) + " HP";

        // Draw the text centered above the element.
        DrawTextUtils.drawText(hpText, canvas,
                getCenterX() * param.coef() + param.offsetX(),
                getPosY() * param.coef() + param.offsetY(),
                CENTER, TOP, paint);

        // restore paint color.
        paint.setColor(initialColor);
    }

    /** @return The weight of the destructible. Used to calculate the unit cost or reward. */
    public int getWeight() { return mWeight; }

    /** @return The centerX position on the grid. */
    public float getCenterX() { return getPosX() + getWidth() / 2f; }

    /** @return The centerY position on the grid. */
    public float getCenterY() { return getPosY() + getHeight() / 2f; }

    //
    // Drawable functions
    //

    @Override
    public void onUpdateSurfaceSize(int surfaceWidth, int surfaceHeight) {
        //mSprite.onUpdateSurfaceSize(surfaceWidth, surfaceHeight);
    }

    @Override
    public void draw(Canvas canvas, DrawingParam param) {

        float left = mPosition.x * param.coef() + param.offsetX();
        float top = mPosition.y * param.coef() + param.offsetY();
        float right = left + mWidth * param.coef();
        float bottom = top + mHeight * param.coef();

        // Update sprite dimensions.
        mSprite.setDimensions(left, top, right, bottom);
        mSprite.draw(canvas, null);
    }

    @Override
    public float getPosX() { return mPosition.x; }

    @Override
    public float getPosY() { return mPosition.y; }

    @Override
    public void setPosition(float x, float y) {
        mPosition.set(x, y);
    }

    @Override
    public void offsetPosition(float dx, float dy) {
        mPosition.offset(dx, dy);
    }

    @Override
    public float getWidth() { return mWidth; }

    @Override
    public float getHeight() { return mHeight; }

    //
    // Sprite functions
    //

    protected void updateAnimation() { mSprite.updateAnimationFrame(); }

    /**
     * Set the current animation id.<br />
     * Does nothing if the passed animation id is the same as the current one.
     *
     * @param animationId The new animation Id.
     */
    public void changeAnimation(AnimationId animationId) {
        if (animationId == mSprite.getAnimationId()) {
            return;
        }
        mSprite.setAnimationId(animationId);
    }

    @Override
    public void onAnimationStopped() { }
}
