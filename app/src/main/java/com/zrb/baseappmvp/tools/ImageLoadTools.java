package com.zrb.baseappmvp.tools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.Date;
import java.util.List;


/**
 * Created by zrb on 2017/6/21.
 */

public class ImageLoadTools {
    public static void LoadImage(Context context, String path, ImageView iv) {
        if (path == null || iv == null)
            return;
        if (!path.isEmpty())
            if (!path.substring(0, 1).equals("h") && !path.substring(0, 1).equals("H")) {
                path = SharedPreferencesTools.getInstance().getHost_view_img() + path;
            }
        Glide.with(context).load(path).into(iv);
    }





    public static void LoadCirclePicSmallImage(Context context, String path, ImageView iv) {
        if (path == null || iv == null) {
            LogUtil.e("path == null || iv == null");
            return;
        }
        if (!path.isEmpty())
            if (!path.substring(0, 1).equals("h") && !path.substring(0, 1).equals("H")) {
                path = SharedPreferencesTools.getInstance().getHost_view_img() + path;
            }
        path = path + "!importCircle";
        Glide.with(context).load(path).
                transform(new CenterCrop(context), new GlideRoundImage(context, 10)).into(iv);
    }
    public static void LoadImageNoRound(Context context, String path, ImageView iv) {
        if (path == null || iv == null) {
            LogUtil.e("path == null || iv == null");
            return;
        }
        if (!path.isEmpty())
            if (!path.substring(0, 1).equals("h") && !path.substring(0, 1).equals("H")) {
                path = SharedPreferencesTools.getInstance().getHost_view_img() + path;
            }
        Glide.with(context).load(path).into(iv);
    }

    public static void LoadSDImge(Context context, String path, ImageView iv) {
        if (path == null || iv == null) {
            return;
        }
        Glide.with(context).load(path).transform(new GlideRoundTransform(context, 10)).into(iv);
    }

    public static void LoadSDImgeNoRound(Context context, String path, ImageView iv) {
        if (path == null || iv == null) {
            return;
        }
        Glide.with(context).load(path).into(iv);
    }



    public static class GlideRoundImage extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundImage(Context context) {
            this(context, 4);
        }

        public GlideRoundImage(Context context, int dp) {
            super(context);
            radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }
}
