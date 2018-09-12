package com.example.android.mygarden.ui;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.mygarden.provider.PlantContract;
import com.example.android.mygarden.utils.PlantUtils;

import static com.example.android.mygarden.provider.PlantContract.BASE_CONTENT_URI;
import static com.example.android.mygarden.provider.PlantContract.PATH_PLANTS;

/**
 * Created by angelov on 9/11/2018.
 */

public class PlantWateringService extends IntentService {

    public static final String WATER_PLANTS_ACTION = "com.example.android.mygarden.water_action";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PlantWateringService() {
        super("PlantWateringService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null && WATER_PLANTS_ACTION.equals(intent.getAction())) {
            handleActionWaterPlants();
        }
    }

    private void handleActionWaterPlants() {
        Uri plantsUri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
        ContentValues contentValues = new ContentValues();
        long timeNow = System.currentTimeMillis();
        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
        getContentResolver().update(plantsUri, contentValues,
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME  + ">?",
                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
    }

    public static void startWateringService(Context context) {
        Intent intent = new Intent(context, IntentService.class);
        intent.setAction(WATER_PLANTS_ACTION);
        context.startService(intent);
    }
}
