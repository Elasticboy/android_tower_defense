package org.es.towerdefense.component;

import android.content.res.Resources;
import android.graphics.Paint;

import org.es.engine.graphics.utils.DrawTextUtils;
import org.es.engine.hud.HUD;
import org.es.engine.hud.Text;
import org.es.engine.hud.ToggleButton;
import org.es.towerdefense.R;
import org.es.towerdefense.object.Player;
import org.es.towerdefense.process.GameMgr;

/**
 * @author Cyril Leroux
 *         Created 06/03/14.
 */
public class HudHelper {

    // TODO init with a resource manager
    public static void initMainHud(HUD mainHud, final Player player, final GameMgr gameMgr, final Resources resources) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1f);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30f);

        final float screenRatio = 16f / 10f;
        final float marginY = 0.02f;
        final float marginX = 0.02f / screenRatio;
        final float buttonSideY = 0.1f;
        final float buttonSideX = buttonSideY / screenRatio;

        //
        // Play pause button
        //

        final ToggleButton playPause = new ToggleButton(
                1f - marginX - buttonSideX, marginY, 0f, buttonSideY,
                resources,
                R.drawable.ic_pause, R.drawable.ic_pause,
                R.drawable.ic_play, R.drawable.ic_play) {
            @Override
            protected void onClick() { gameMgr.pause(); }

            @Override
            protected void onClick2() { gameMgr.resume(); }
        };
        mainHud.addControl(playPause);


        final float textTop = buttonSideY / 2f + marginY;

        //
        // Score
        //

        final Text score = new Text(marginX, textTop, resources.getString(R.string.hud_score),
                DrawTextUtils.HorizontalAlign.RIGHT, DrawTextUtils.VerticalAlign.CENTER, paint) {
            @Override
            protected String getText() {
                return String.valueOf(player.getScore());
            }
        };
        mainHud.addControl(score);

        //
        // Money
        //

        final Text money = new Text(1f / 3f, textTop, resources.getString(R.string.hud_money),
                DrawTextUtils.HorizontalAlign.RIGHT, DrawTextUtils.VerticalAlign.CENTER, paint) {
            @Override
            protected String getText() {
                return String.valueOf(player.getMoney());
            }
        };
        mainHud.addControl(money);

        //
        // Health
        //

        final Text HEALTH = new Text(2f / 3f, textTop, resources.getString(R.string.hud_health),
                DrawTextUtils.HorizontalAlign.RIGHT, DrawTextUtils.VerticalAlign.CENTER, paint) {
            @Override
            protected String getText() {
                return String.valueOf(player.getHealth());
            }
        };
        mainHud.addControl(HEALTH);
    }
}
