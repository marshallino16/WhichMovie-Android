package genyus.com.whichmovie.utils;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;

/**
 * Created by GENyUS on 28/01/16.
 */
public class ThemeUtils {

    public static void revealColorAnimateStatusBar(final Activity activity, final int toColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != activity) {
            final Window window = activity.getWindow();

            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();
                    int blended = blendColors(window.getStatusBarColor(), toColor, position);
                    window.setStatusBarColor(blended);
                }
            });

            anim.setDuration(300).start();
        }
    }

    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
