package genyus.com.whichmovie;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import genyus.com.whichmovie.adapter.ImageSlideshowPagerAdapter;
import genyus.com.whichmovie.model.Image;
import genyus.com.whichmovie.session.GlobalVars;

@EActivity(R.layout.fragment_photo_viewer)
public class PhotoViewerActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String DOWNLOAD_FOLDER = "TonightMovies";
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Extra
    int positionImage;

    @Extra
    int vibrantRGB;

    @Extra
    ArrayList<Image> listImagesSlide = new ArrayList<>();

    @ViewById(R.id.quit)
    ImageView quit;

    @ViewById(R.id.save)
    FloatingActionButton save;

    @ViewById(R.id.viewpager)
    ViewPager slideShow;

    @AfterViews
    protected void afterViews() {
        quit.setOnClickListener(this);
        save.setOnClickListener(this);

        ImageSlideshowPagerAdapter slideshowPagerAdapter = new ImageSlideshowPagerAdapter(this, listImagesSlide);
        slideShow.setAdapter(slideshowPagerAdapter);

        if (0 != positionImage) {
            if (listImagesSlide.size() > positionImage) {
                slideShow.setCurrentItem(positionImage);
            }
        }

        if (-1 != vibrantRGB) {
            save.setBackgroundTintList(ColorStateList.valueOf(vibrantRGB));
        }
    }

    @Override
    public void onClick(View view) {
        if (R.id.quit == view.getId()) {
            this.finish();
        } else if (R.id.save == view.getId()) {
            if (null != slideShow) {

                boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    return;
                }

                final String url = GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(GlobalVars.configuration.getBackdrop_sizes().size() - 1) + listImagesSlide.get(slideShow.getCurrentItem()).getPath();
                new DownloadFile().execute(url);
            }
        }
    }


    class DownloadFile extends AsyncTask<String, Integer, String> {
        String strFolderName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL((String) aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
                String targetFileName = timeStamp + ".png";//Change name and subname
                int lenghtOfFile = conexion.getContentLength();

                File dcim = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toURI().toString().replace("file:", ""));
                if (!dcim.exists()) {
                    dcim.mkdir();
                }

                String PATH = dcim.getPath().toString() + "/" + DOWNLOAD_FOLDER + "/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(PATH + targetFileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aLong) {
            super.onPostExecute(aLong);
            if (null != aLong && aLong.equals("success")) {
                Toast.makeText(PhotoViewerActivity.this, getResources().getString(R.string.image_saved), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final String url = GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(GlobalVars.configuration.getBackdrop_sizes().size() - 1) + listImagesSlide.get(slideShow.getCurrentItem()).getPath();
                    new DownloadFile().execute(url);
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
