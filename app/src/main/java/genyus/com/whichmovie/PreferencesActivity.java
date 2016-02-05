package genyus.com.whichmovie;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.androidannotations.annotations.EActivity;
import org.apache.commons.lang3.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import genyus.com.whichmovie.classes.AppCompatPreferenceActivity;
import genyus.com.whichmovie.listener.NegativeReviewListener;
import genyus.com.whichmovie.listener.ReviewListener;
import genyus.com.whichmovie.utils.AnalyticsEventUtils;
import genyus.com.whichmovie.utils.PreferencesUtils;
import genyus.com.whichmovie.view.RatingDialog;

@EActivity
public class PreferencesActivity extends AppCompatPreferenceActivity {

    @SuppressWarnings("deprecation")
    // TODO use new API
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setTitle(R.string.preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findPreference("contact").setOnPreferenceClickListener(new CustomPreferenceClickListener() {
            @Override
            public boolean onClick(Preference preference) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "thnklystudio@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Tonight's movie] - Contact form");
                startActivity(Intent.createChooser(emailIntent, "Email us"));
                return false;
            }
        });


        findPreference("about").setOnPreferenceClickListener(new CustomPreferenceClickListener() {
            @Override
            public boolean onClick(Preference preference) {
                AboutActivity_.intent(PreferencesActivity.this).start();
                return false;
            }
        });

        findPreference("resetMoviePreferences").setOnPreferenceClickListener(new CustomPreferenceClickListener() {
            @Override
            public boolean onClick(Preference preference) {
                PreferencesUtils.setPagePreference(PreferencesActivity.this, 1);
                Toast.makeText(PreferencesActivity.this, getResources().getString(R.string.changes_next_time), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        findPreference("tos").setOnPreferenceClickListener(new CustomPreferenceClickListener() {
            @Override
            public boolean onClick(android.preference.Preference preference) {
                WebviewActivity_.intent(PreferencesActivity.this).movieName(getString(R.string.term_of_service_title)).link(getString(R.string.term_of_service_url)).start();
                return true;
            }
        });

        findPreference("checkboxToCheck").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean) newValue == true) {
                    new MaterialDialog.Builder(PreferencesActivity.this)
                            .content(getResources().getString(R.string.thank))
                            .positiveText(R.string.ok)
                            .show();
                }
                return true;
            }
        });

        Preference ratePreference = findPreference("rate");
        ratePreference.setOnPreferenceClickListener(new CustomPreferenceClickListener() {
            @Override
            public boolean onClick(android.preference.Preference preference) {
                final RatingDialog fiveStarsDialog = new RatingDialog(PreferencesActivity.this, "dev.genyus@gmail.com");
                fiveStarsDialog.setRateText("That kind of you!\nWhy don't you take 5 seconds to rate us? \nAnd do not forget, we love you very much!")
                        .setTitle("So rate us maybe")
                        .setForceMode(true)
                        .setUpperBound(4)
                        .setNegativeReviewListener(new NegativeReviewListener() {
                            @Override
                            public void onNegativeReview(int i) {
                                AnalyticsEventUtils.sendEventAction("Rate", "bad - " + i);
                            }
                        })
                        .setReviewListener(new ReviewListener() {
                            @Override
                            public void onReview(int i) {
                                if (i == 4 && i == 5) {
                                    AnalyticsEventUtils.sendEventAction("Rate", "good - " + i);
                                    final Uri uri = Uri.parse("market://details?id="
                                            + getApplicationContext().getPackageName());
                                    final Intent rateAppIntent = new Intent(
                                            Intent.ACTION_VIEW, uri);

                                    if (getPackageManager().queryIntentActivities(
                                            rateAppIntent, 0).size() > 0) {
                                        startActivity(rateAppIntent);
                                    }
                                }
                            }
                        })
                        .showAfter(2000);

                return true;
            }
        });

        try {
            PreferenceScreen version = (PreferenceScreen) findPreference("version");
            version.setOnPreferenceClickListener(new CustomPreferenceClickListener() {
                @Override
                public boolean onClick(android.preference.Preference preference) {
                    showVersionDialog();
                    return true;
                }
            });
        } catch (Exception ignore) {
        }

    }

    private abstract class CustomPreferenceClickListener implements android.preference.Preference.OnPreferenceClickListener {
        public abstract boolean onClick(Preference preference);

        @Override
        public final boolean onPreferenceClick(Preference preference) {
            return onClick(preference);
        }

    }

    long lastVersionClick = 0;
    int nbVersionClick = 0;

    private void showVersionDialog() {
        ZipFile zf = null;
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
            zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            java.util.Date date = new java.util.Date(time);

            TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
            String city = "Toulouse";
            SimpleDateFormat weekday = new SimpleDateFormat("EEEE", Locale.US);
            weekday.setTimeZone(timeZone);
            SimpleDateFormat month = new SimpleDateFormat("MMMM", Locale.US);
            month.setTimeZone(timeZone);
            SimpleDateFormat daynumber = new SimpleDateFormat("d", Locale.US);
            daynumber.setTimeZone(timeZone);
            SimpleDateFormat hourformat = new SimpleDateFormat("H", Locale.US);
            hourformat.setTimeZone(timeZone);
            int daynb = Integer.valueOf(daynumber.format(date));
            int hours = Integer.valueOf(hourformat.format(date));
            SimpleDateFormat hourFormat = new SimpleDateFormat("h", Locale.US);
            hourFormat.setTimeZone(timeZone);
            SimpleDateFormat minuteFormat = new SimpleDateFormat(getString(R.string.minute_date_format), Locale.US);
            minuteFormat.setTimeZone(timeZone);

            String content = getString(R.string.crafted_app_time_and_weekday_period, WordUtils.capitalize(weekday.format(date).toLowerCase(Locale.US)), getPeriod(hours))
                    + "\n\n"
                    + getString(R.string.crafted_app_precise_time_and_dom_month_hour_minute_city, getDayOfMonthWithSuffix(daynb), month.format(date), hourFormat.format(date),
                    minuteFormat.format(date), city);

            new MaterialDialog.Builder(this)
                    .content(content)
                    .positiveText(R.string.ok)
                    .show();

        } catch (Exception e) {
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /* from 0 to 23 */
    private String getPeriod(int hour) {
        if (hour <= 5) {
            return getString(R.string.night);
        } else if (hour <= 12) {
            return getString(R.string.morning);
        } else if (hour <= 18) {
            return getString(R.string.afternoon);
        } else if (hour <= 22) {
            return getString(R.string.evening);
        } else {
            return getString(R.string.night);
        }
    }

    /* from 1 to 31 */
    private String getDayOfMonthWithSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return getString(R.string.dom_th_and_dom, n);
        }
        switch (n % 10) {
            case 1:
                return getString(R.string.dom_st_and_dom, n);
            case 2:
                return getString(R.string.dom_nd_and_dom, n);
            case 3:
                return getString(R.string.dom_rd_and_dom, n);
            default:
                return getString(R.string.dom_th_and_dom, n);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

}