package binarysailor.tetrese.ui.menu;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.R;
import binarysailor.tetrese.model.GameLifecycle;
import binarysailor.tetrese.ui.Dimensions;
import binarysailor.tetrese.ui.Scene;

public class MenuScene implements Scene {

    private final MenuRenderer renderer;
    private final View.OnTouchListener touchListener;
    private final Menu menu;

    public MenuScene(Resources res, Dimensions dimensions, GameLifecycle lifecycle) {
        MenuGraphicalObjects graphicalObjects = new MenuGraphicalObjects();
        this.menu = Menu.inPanel(dimensions.getSurfaceRect())
                .withTextPaint(graphicalObjects.optionTextPaint)
                .addOption(MenuAction.PLAY, res.getString(R.string.menu_play))
                .addOption(MenuAction.OPTIONS, res.getString(R.string.menu_options))
                .build();
        this.renderer = new MenuRenderer(dimensions, this.menu, graphicalObjects);
        this.touchListener = new MenuTouchListener(menu, lifecycle);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Canvas canvas) {
        renderer.render(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return touchListener.onTouch(view, motionEvent);
    }
}
