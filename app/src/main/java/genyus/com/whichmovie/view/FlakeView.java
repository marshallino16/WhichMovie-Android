package genyus.com.whichmovie.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

import java.util.ArrayList;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.classes.Flake;

public class FlakeView extends View {

    Bitmap droid;
    int numFlakes = 0;
    ArrayList<Flake> flakes = new ArrayList<>();

    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime;
    Matrix m = new Matrix();

    public FlakeView(Context context) {
        super(context);
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.flake);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float)(nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += (flake.speed * secs);
                    if (flake.y > getHeight()) {
                        flake.y = 0 - flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    int getNumFlakes() {
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
    }

    /**
     * Add the specified number of flakes.
     */
    void addFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            flakes.add(Flake.createFlake(getWidth(), droid));
        }
        setNumFlakes(numFlakes + quantity);
    }

    /**
     * Subtract the specified number of flakes. We just take them off the end of the
     * list, leaving the others unchanged.
     */
    void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidflakes, then restart it with 8 flakes
        flakes.clear();
        numFlakes = 0;
        addFlakes(8);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width/2, -flake.height/2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width/2 + flake.x, flake.height/2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }

        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            startTime = nowTime;
        }
    }

    public void pause() {
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}