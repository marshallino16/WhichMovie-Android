package genyus.com.whichmovie.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class GaussianBlur {
    private final int DEFAULT_RADIUS = 25;
    private final float DEFAULT_MAX_IMAGE_SIZE = 400;

    private Context context;
    private int radius;
    private float maxImageSize;

    public GaussianBlur(Context context) {
    this.context = context;
    setRadius(DEFAULT_RADIUS);
    setMaxImageSize(DEFAULT_MAX_IMAGE_SIZE);
    } 

    public Bitmap render(Bitmap bitmap, boolean scaleDown) {
    RenderScript rs = RenderScript.create(context);

    if (scaleDown) {
        bitmap = scaleDown(bitmap);
    }

    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

    Allocation inAlloc = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
    Allocation outAlloc = Allocation.createFromBitmap(rs, output);

    ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, inAlloc.getElement()); // Element.U8_4(rs));
    script.setRadius(getRadius());
    script.setInput(inAlloc);
    script.forEach(outAlloc);
    outAlloc.copyTo(output);

    rs.destroy();

    return output;
}

public Bitmap scaleDown(Bitmap input) {
    float ratio = Math.min((float) getMaxImageSize() / input.getWidth(), (float) getMaxImageSize() / input.getHeight());
    int width = Math.round((float) ratio * input.getWidth());
    int height = Math.round((float) ratio * input.getHeight());

    return Bitmap.createScaledBitmap(input, width, height, true);
}

public int getRadius() {
    return radius;
}

public void setRadius(int radius) {
    this.radius = radius;
}

public float getMaxImageSize() {
    return maxImageSize;
}

public void setMaxImageSize(float maxImageSize) {
    this.maxImageSize = maxImageSize;
}
    }