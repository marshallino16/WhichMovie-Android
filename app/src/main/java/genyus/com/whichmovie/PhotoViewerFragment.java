package genyus.com.whichmovie;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
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

@EFragment(R.layout.fragment_photo_viewer)
public class PhotoViewerFragment extends Fragment implements View.OnClickListener {

    private final static String DOWNLOAD_FOLDER = "TonightMovies";

    @FragmentArg
    int positionImage;

    @FragmentArg
    int vibrantRGB;

    @FragmentArg
    ArrayList<Image> listImagesSlide = new ArrayList<>();

    @ViewById(R.id.quit)
    ImageView quit;

    @ViewById(R.id.save)
    FloatingActionButton save;

    @ViewById(R.id.viewpager)
    ViewPager slideShow;

    @AfterViews
    protected void afterViews(){
        quit.setOnClickListener(this);
        save.setOnClickListener(this);

        ((MainActivity)getActivity()).getSupportActionBar().hide();
        ImageSlideshowPagerAdapter slideshowPagerAdapter = new ImageSlideshowPagerAdapter(getActivity(), listImagesSlide);
        slideShow.setAdapter(slideshowPagerAdapter);

        if (0 != positionImage) {
            if (listImagesSlide.size() > positionImage) {
                slideShow.setCurrentItem(positionImage);
            }
        }

        if(-1 != vibrantRGB){
            save.setBackgroundTintList(ColorStateList.valueOf(vibrantRGB));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getFragmentManager().beginTransaction().remove(PhotoViewerFragment.this).commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (R.id.quit == view.getId()) {
            ((MainActivity)getActivity()).getSupportActionBar().show();
            getActivity().getFragmentManager().beginTransaction().remove(PhotoViewerFragment.this).commit();
        } else if (R.id.save == view.getId()) {
            if(null != slideShow){
                String url = GlobalVars.configuration.getBase_url() + GlobalVars.configuration.getBackdrop_sizes().get(1) + listImagesSlide.get(slideShow.getCurrentItem()).getPath();
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

                File rootsd = Environment.getExternalStorageDirectory();
                File dcim = new File(rootsd.getAbsolutePath() + "/DCIM");
                if(!dcim.exists()){
                    dcim.mkdir();
                }
                String PATH = dcim.getPath().toString() + "/" + DOWNLOAD_FOLDER + "/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();//If there is no folder it will be created.
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
            if(null != aLong && aLong.equals("success")){
                Toast.makeText(getContext(), getResources().getString(R.string.image_saved), Toast.LENGTH_LONG).show();
            }
        }
    }
}
