package genyus.com.whichmovie.utils;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import genyus.com.whichmovie.R;

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

    public static void revealColorAnimateViewTextColor(final Activity activity, final int toColor, final TextView view) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();
                    int blended = blendColors(view.getCurrentTextColor(), toColor, position);
                    view.setTextColor(blended);
                }
            });

            anim.setDuration(300).start();
    }

    public static void revealColorAnimateViewBackgroundColor(final Activity activity, final int toColor, final View view) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();
                    int blended = blendColors(((ColorDrawable)view.getBackground()).getColor(), toColor, position);
                    view.setBackgroundColor(blended);
                }
            });

            anim.setDuration(300).start();
    }

    public static void revealColorAnimateViewDrawable(final Activity activity, final int toColor, final Drawable view) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();

                    if (view instanceof ShapeDrawable) {
                        // cast to 'ShapeDrawable'
                        ShapeDrawable shapeDrawable = (ShapeDrawable)view;
                        int blended = blendColors(shapeDrawable.getPaint().getColor(), toColor, position);
                        shapeDrawable.getPaint().setColor(blended);
                    } else if (view instanceof GradientDrawable) {
                        // cast to 'GradientDrawable'
                        GradientDrawable gradientDrawable = (GradientDrawable)view;
                        int blended = blendColors(activity.getResources().getColor(R.color.progress), toColor, position);
                        gradientDrawable.setColor(blended);
                    }
                }
            });

            anim.setDuration(300).start();
    }

    public static void revealColorAnimateViewDrawableAlpha(final Activity activity, final int toColor, final Drawable view) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float position = animation.getAnimatedFraction();

                if (view instanceof ShapeDrawable) {
                    // cast to 'ShapeDrawable'
                    ShapeDrawable shapeDrawable = (ShapeDrawable)view;
                    int blended = blendColors(shapeDrawable.getPaint().getColor(), toColor, position);
                    shapeDrawable.getPaint().setColor(ThemeUtils.adjustAlpha(blended, 70f));
                } else if (view instanceof GradientDrawable) {
                    // cast to 'GradientDrawable'
                    GradientDrawable gradientDrawable = (GradientDrawable)view;
                    int blended = blendColors(activity.getResources().getColor(R.color.progress), toColor, position);
                    gradientDrawable.setColor(ThemeUtils.adjustAlpha(blended, 70f));
                }
            }
        });

        anim.setDuration(300).start();
    }


    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
