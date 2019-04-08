package edu.apsu.drawingapp;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SaveImage {
     private static final File rootDir = new File(Environment.getExternalStorageDirectory()+File.separator+"huaban/");

    /** Save picture to file */
    public static boolean saveScreen(View view){
        //determine if SDCARD is available
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return false;
        }
        if(!rootDir.exists()){
            rootDir.mkdir();
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new
                    File(rootDir,System.currentTimeMillis()+".jpg")));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }finally{
            view.setDrawingCacheEnabled(false);
            bitmap = null;
        }
    }


}

