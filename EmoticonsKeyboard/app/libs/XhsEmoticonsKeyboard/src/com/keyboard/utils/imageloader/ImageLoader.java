package com.keyboard.utils.imageloader;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader implements ImageBase {

    protected final Context context;

    private volatile static ImageLoader instance;

    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader(context);
                }
            }
        }
        return instance;
    }

    public ImageLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     *
     * @param imageUri
     * @return
     */
    public Drawable getDrawable(String imageUri){
        switch (Scheme.ofUri(imageUri)) {
            case HTTP:
            case HTTPS:
                return null;
            case FILE:
                return null;
            case CONTENT:
                return null;
            case ASSETS:
                String filePath = Scheme.ASSETS.crop(imageUri);
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
                    return new BitmapDrawable(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            case DRAWABLE:
                String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
                int resID = context.getResources().getIdentifier(drawableIdString, "drawable", context.getPackageName());
                return context.getResources().getDrawable((int) resID);
            case UNKNOWN:
            default:
                return null;
        }
    }

    /**
     *
     * @param uriStr
     * @param imageView
     * @throws IOException
     */
    @Override
    public void displayImage(String uriStr, ImageView imageView) throws IOException {
        switch (Scheme.ofUri(uriStr)) {
            case HTTP:
            case HTTPS:
                displayImageFromNetwork(uriStr, imageView);
                return ;
            case FILE:
                displayImageFromFile(uriStr, imageView);
                return ;
            case CONTENT:
                displayImageFromContent(uriStr, imageView);
                return ;
            case ASSETS:
                displayImageFromAssets(uriStr, imageView);
                return ;
            case DRAWABLE:
                displayImageFromDrawable(uriStr, imageView);
                return ;
            case UNKNOWN:
            default:
                displayImageFromOtherSource(uriStr, imageView);
                return ;
        }
    }

    /**
     * From Net
     * @param imageUri
     * @param extra
     * @throws IOException
     */
    protected void displayImageFromNetwork(String imageUri, Object extra) throws IOException {
        return ;
    }

    /**
     * From File
     * @param imageUri
     * @param imageView
     * @throws IOException
     */
    protected void displayImageFromFile(String imageUri, ImageView imageView) throws IOException {
        String filePath = Scheme.FILE.crop(imageUri);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        return ;
    }

    /**
     * From Content
     * @param imageUri
     * @param imageView
     * @throws IOException
     */
    protected void displayImageFromContent(String imageUri, ImageView imageView) throws FileNotFoundException {
        ContentResolver res = context.getContentResolver();
        Uri uri = Uri.parse(imageUri);
        InputStream inputStream = res.openInputStream(uri);
        return ;
    }

    /**
     * From Assets
     * @param imageUri
     * @param imageView
     * @throws IOException
     */
    protected void displayImageFromAssets(String imageUri, ImageView imageView) throws IOException {
        String filePath = Scheme.ASSETS.crop(imageUri);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        return ;
    }

    /**
     * From Drawable
     * @param imageUri
     * @param imageView
     * @throws IOException
     */
    protected void displayImageFromDrawable(String imageUri, ImageView imageView) {
        String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
        int resID = context.getResources().getIdentifier(drawableIdString, "drawable", context.getPackageName());

        if (imageView != null) {
            imageView.setImageResource(resID);
        }
        return ;
    }

    /**
     * From OtherSource
     * @param imageUri
     * @param imageView
     * @throws IOException
     */
    protected void displayImageFromOtherSource(String imageUri, ImageView imageView) throws IOException {
        return ;
    }
}
