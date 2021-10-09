package com.muflar_driver.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.UpdateLocationInfo;
import com.muflar_driver.model.UpdateLocationResponse;
import com.muflar_driver.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

       final public static String ONE_TIME = "onetime";
       APIInterface apiInterface;

       @Override
       public void onReceive(final Context context, Intent intent) {
           PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
           PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmManger");
           //Acquire the lock
           wl.acquire();

           apiInterface = APIClient.getClient().create(APIInterface.class);

           UpdateLocationInfo body = new UpdateLocationInfo();
           body.busID = "S987654322";
           body.busRoute = "2B";
           body.currentLatitude = "1.34665";
           body.CurrentLongitude = "103.697784";

           Call<UpdateLocationResponse> call = apiInterface.updateSelfLocation(body);
           call.enqueue(new Callback<UpdateLocationResponse>() {
               @Override
               public void onResponse(Call<UpdateLocationResponse> call, Response<UpdateLocationResponse> response) {
                   Toast.makeText(context, "alarm manager alarm success", Toast.LENGTH_LONG).show();
                   UpdateLocationResponse body = response.body();

               }

               @Override
               public void onFailure(Call<UpdateLocationResponse> call, Throwable t) {
                   Toast.makeText(context, "alarm manager alarm failure", Toast.LENGTH_LONG).show();
                   int errorCode = 500;
                   return;
               }
           });
           //Release the lock
           wl.release();
       }

      public void SetAlarm(Context context)
      {
          AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
          Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
          intent.putExtra(ONE_TIME, Boolean.FALSE);
          PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
          //After after 5 seconds
          am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 90 , pi);
      }

      public void CancelAlarm(Context context)
      {
          Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
          PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
          AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
          alarmManager.cancel(sender);
      }

      public void setOnetimeTimer(Context context){
          AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
          Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
          intent.putExtra(ONE_TIME, Boolean.TRUE);
          PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
          am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
      }
}