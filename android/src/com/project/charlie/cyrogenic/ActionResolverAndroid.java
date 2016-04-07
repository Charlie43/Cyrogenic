package com.project.charlie.cyrogenic;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.*;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Charlie on 19/03/2016.
 */
public class ActionResolverAndroid {

    Handler handler;
    Context context;
    GoogleApiClient mClient = null;

    final int FITNESS_REQUEST = 1;

    public ActionResolverAndroid(Context context) {
        handler = new Handler();
        this.context = context;
    }

    public void connectToFitnessApi(final AndroidLauncher androidLauncher) {


// todo check permissions
        if (mClient == null) {

            mClient = new GoogleApiClient.Builder(context)
                    .addApi(Fitness.HISTORY_API)
                    .useDefaultAccount()
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            Gdx.app.log("AndroidActionResolver", "Connected to fitness API");
                            // todo make fitness api calls

                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            Gdx.app.log("AAR", "Connection suspended");

                            // todo handle connection callbacks
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Gdx.app.log("AAR", "Connection failed");
                            if (connectionResult.getErrorMessage() != null) {
                                Gdx.app.log("AAR", connectionResult.getErrorMessage());
                            }
                            if(connectionResult.getErrorCode() == FitnessStatusCodes.SIGN_IN_REQUIRED) {
                                try {
                                    connectionResult.startResolutionForResult(androidLauncher, 4);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                            Gdx.app.log("AAR", "" + connectionResult.getErrorCode());
                        }


                    })
                    .build();
            mClient.connect();
        }
        if (mClient == null) {
            Gdx.app.log("AAR", "Null MCLIEnT");
        } else {
            Gdx.app.log("AAR", "Not null");
            Gdx.app.log("AAR", "ed" + mClient.isConnected());
            Gdx.app.log("AAR", "ing" + mClient.isConnecting());
        }
    }


    public void readData() {
        Gdx.app.log("AAR", "Reading data...");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA,
                        DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        DataReadResult readResult = Fitness.HistoryApi
                .readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA).await();
        if (result.getTotal() == null) {
            Gdx.app.log("AAP", "Result total = null");
        } else {
            if (result.getTotal().getDataPoints() != null) {
                Gdx.app.log("DailyTotal", result.getTotal().getDataPoints().get(0).getValue(Field.FIELD_STEPS).toString());
            }
        }
        Gdx.app.log("AndroidActionResolver", "Requests completed");
        for (DataSet data : readResult.getDataSets()) {
            for (DataPoint dataPoint : data.getDataPoints()) {
                Gdx.app.log("S", "Type: " + dataPoint.getDataType().getName());
            }
        }
    }

    public int readTotalSteps() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA).await();
        if(result.getTotal() != null && result.getTotal().getDataPoints() != null) {
            return result.getTotal().getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
        } else {
            return 0;
        }

    }

    public ArrayList<Integer> readWeeklySteps() {
        Gdx.app.log("AAR", "Reading weekly steps...");
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        ArrayList<Integer> steps = new ArrayList<>();
        DataReadResult readResult = Fitness.HistoryApi.readData(mClient, readRequest).await();
        for(Bucket bucket : readResult.getBuckets()) {
            Gdx.app.log("AAR", "DS Size " + bucket.getDataSets().size());
            for (DataSet ds : bucket.getDataSets()) {
                for (DataPoint dp : ds.getDataPoints()) {
                    Gdx.app.log("AAR", "Data point:");
                    Gdx.app.log("AAR", "\tType: " + dp.getDataType().getName());
                    for (Field field : dp.getDataType().getFields()) {
                        Gdx.app.log("AAR", "\tField: " + field.getName() +
                                " Value: " + dp.getValue(field));
                        steps.add(dp.getValue(Field.FIELD_STEPS).asInt());
                    }
                }
            }
        }
//        return readResult.getDataSets().get(0).getDataPoints().get(0).;
        return steps;
    }
}
