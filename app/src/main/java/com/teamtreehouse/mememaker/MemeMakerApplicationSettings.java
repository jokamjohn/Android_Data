package com.teamtreehouse.mememaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.teamtreehouse.mememaker.utils.StorageType;

/**
 * Created by Evan Anger on 8/13/14.
 */
public class MemeMakerApplicationSettings {
    private static final String STORAGE_TYPE = "storage_type";
    private SharedPreferences mSharedPreferences;

    public MemeMakerApplicationSettings(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getStoragePreference()
    {
        return mSharedPreferences.getString(STORAGE_TYPE, StorageType.INTERNAL);
    }

    public void setSharedPreferences(String storageType)
    {
        mSharedPreferences
                .edit()
                .putString(STORAGE_TYPE,storageType)
                .apply();
    }

}
