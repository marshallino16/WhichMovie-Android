package genyus.com.whichmovie.classes;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Flake {

    public float x, y;
    public float rotation;
    public float speed;
    public float rotationSpeed;
    public int width, height;
    public Bitmap bitmap;

    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    public static Flake createFlake(float xRange, Bitmap originalBitmap) {
        Flake flake = new Flake();

        flake.width = (int)(5 + (float)Math.random() * 50);
        float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
        flake.height = (int)(flake.width * hwRatio);

        flake.x = (float)Math.random() * (xRange - flake.width);
        flake.y = 0 - (flake.height + (float)Math.random() * flake.height);

        flake.speed = 50 + (float) Math.random() * 150;

        flake.rotation = (float) Math.random() * 180 - 90;
        flake.rotationSpeed = (float) Math.random() * 90 - 45;

        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null) {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int)flake.width, (int)flake.height, true);
            bitmapMap.put(flake.width, flake.bitmap);
        }
        return flake;
    }
}