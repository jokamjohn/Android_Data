package com.teamtreehouse.mememaker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.teamtreehouse.mememaker.MemeMakerApplicationSettings;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Evan Anger on 7/28/14.
 */
public class FileUtilities {

    //copy files to internal storage.
    public static void saveAssetImage(Context context, String assetName) {
        File fileDirectory = getFilesDirectory(context);//File directory
        File fileToWrite = new File(fileDirectory,assetName);

        //Asset manager to access files in the assets folder
        AssetManager assetManager = context.getAssets();

        try {
            InputStream in = assetManager.open(assetName); //Read the file
            FileOutputStream out = new FileOutputStream(fileToWrite); //Where to write

            copyFile(in,out);

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Copy files
    public static void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Return a list of jpg files
     * @param context
     * @return
     */
    public static File [] listedFiles(Context context)
    {
        //Get the file directory
        final File fileDirectory = getFilesDirectory(context);
        //Get the jpg stored in the files folder.
        File[] filteredFiles = fileDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().contains(".jpg");
            }
        });

        return filteredFiles;
    }

    /**
     * Get the files Directory.
     *
     * @param context Context
     * @return File
     */
    public static File getFilesDirectory(Context context) {
        MemeMakerApplicationSettings settings = new MemeMakerApplicationSettings(context);
        String storageType = settings.getStoragePreference();

        if (storageType.equals(StorageType.INTERNAL))
        {
            return context.getFilesDir();
        }
        else {
            if (isExternalStorageAvailable())
            {
                if (storageType.equals(StorageType.PRIVATE_EXTERNAL))
                {
                    return context.getExternalFilesDir(null);
                }else {
                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                }
            }else {
                return context.getFilesDir();
            }
        }
    }

    /**
     *
     * @return External Media Availability
     */
    private static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static Uri saveImageForSharing(Context context, Bitmap bitmap,  String assetName) {
        File fileToWrite = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), assetName);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return Uri.fromFile(fileToWrite);
        }
    }


    public static void saveImage(Context context, Bitmap bitmap, String name) {
        File fileDirectory = getFilesDirectory(context);
        File fileToWrite = new File(fileDirectory, name);

        try {
            FileOutputStream outputStream = new FileOutputStream(fileToWrite);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
