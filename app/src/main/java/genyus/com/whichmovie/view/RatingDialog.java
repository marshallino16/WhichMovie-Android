package genyus.com.whichmovie.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import genyus.com.whichmovie.R;
import genyus.com.whichmovie.listener.NegativeReviewListener;
import genyus.com.whichmovie.listener.ReviewListener;

/**
 * Created by genyus on 15/12/15.
 */

public class RatingDialog implements DialogInterface.OnClickListener {
    private static final String DEFAULT_TITLE = "Rate this app";
    private static final String DEFAULT_TEXT = "How much do you love our app?";
    private static final String DEFAULT_POSITIVE = "Ok";
    private static final String DEFAULT_NEGATIVE = "Not Now";
    private static final String DEFAULT_NEVER = "Never";
    private static final String SP_NUM_OF_ACCESS = "numOfAccess";
    private static final String SP_DISABLED = "disabled";
    private static final String TAG = RatingDialog.class.getSimpleName();
    private final Context context;
    private boolean isForceMode = false;
    SharedPreferences sharedPrefs;
    private String supportEmail;
    private TextView contentTextView;
    private RatingBar ratingBar;
    private String title = null;
    private String rateText = null;
    private AlertDialog alertDialog;
    private View dialogView;
    private int upperBound = 4;
    private NegativeReviewListener negativeReviewListener;
    private ReviewListener reviewListener;

    public RatingDialog(Context context, String supportEmail) {
        this.context = context;
        this.sharedPrefs = context.getSharedPreferences(context.getPackageName(), 0);
        this.supportEmail = supportEmail;
    }

    private void build() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme_Dialog));
        LayoutInflater inflater = LayoutInflater.from(this.context);
        this.dialogView = inflater.inflate(angtrim.com.fivestarslibrary.R.layout.stars, (ViewGroup)null);
        String titleToAdd = this.title == null?"Rate this app":this.title;
        String textToAdd = this.rateText == null?"How much do you love our app?":this.rateText;
        this.contentTextView = (TextView)this.dialogView.findViewById(angtrim.com.fivestarslibrary.R.id.text_content);
        this.contentTextView.setText(textToAdd);
        this.ratingBar = (RatingBar)this.dialogView.findViewById(angtrim.com.fivestarslibrary.R.id.ratingBar);
        this.ratingBar.setRating(5);
        this.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.d(RatingDialog.TAG, "Rating changed : " + v);
                if(RatingDialog.this.isForceMode && v >= (float)RatingDialog.this.upperBound) {
                    RatingDialog.this.openMarket();
                    if(RatingDialog.this.reviewListener != null) {
                        RatingDialog.this.reviewListener.onReview((int)ratingBar.getRating());
                    }
                }

            }
        });
        this.alertDialog = builder.setTitle(titleToAdd).setView(this.dialogView).setNegativeButton("Not Now", this).setPositiveButton("Ok", this).setNeutralButton("Never", this).create();
    }

    private void disable() {
        SharedPreferences shared = this.context.getSharedPreferences(this.context.getPackageName(), 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("disabled", true);
        editor.apply();
    }

    private void openMarket() {
        String appPackageName = this.context.getPackageName();

        try {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException var3) {
            this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

    }

    private void sendEmail() {
        Intent emailIntent = new Intent("android.intent.action.SEND");
        emailIntent.setType("plain/text");
        emailIntent.putExtra("android.intent.extra.EMAIL", this.supportEmail);
        emailIntent.putExtra("android.intent.extra.SUBJECT", "App Report (" + this.context.getPackageName() + ")");
        emailIntent.putExtra("android.intent.extra.TEXT", "");
        this.context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private void show() {
        boolean disabled = this.sharedPrefs.getBoolean("disabled", false);
        if(!disabled) {
            this.build();
            this.alertDialog.show();
        }

    }

    public void showAfter(int numberOfAccess) {
        this.build();
        this.show();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == -1) {
            if(this.ratingBar.getRating() < (float)this.upperBound) {
                if(this.negativeReviewListener == null) {
                    this.sendEmail();
                } else {
                    this.negativeReviewListener.onNegativeReview((int)this.ratingBar.getRating());
                }
            } else if(!this.isForceMode) {
                this.openMarket();
            }

            //this.disable();
            if(this.reviewListener != null) {
                this.reviewListener.onReview((int)this.ratingBar.getRating());
            }
        }

        if(i == -3) {
            //this.disable();
        }

        if(i == -2) {
            SharedPreferences.Editor editor = this.sharedPrefs.edit();
            editor.putInt("numOfAccess", 0);
            editor.apply();
        }

        this.alertDialog.hide();
    }

    public RatingDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public RatingDialog setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
        return this;
    }

    public RatingDialog setRateText(String rateText) {
        this.rateText = rateText;
        return this;
    }

    public RatingDialog setForceMode(boolean isForceMode) {
        this.isForceMode = isForceMode;
        return this;
    }

    public RatingDialog setUpperBound(int bound) {
        this.upperBound = bound;
        return this;
    }

    public RatingDialog setNegativeReviewListener(NegativeReviewListener listener) {
        this.negativeReviewListener = listener;
        return this;
    }

    public RatingDialog setReviewListener(ReviewListener listener) {
        this.reviewListener = listener;
        return this;
    }
}
