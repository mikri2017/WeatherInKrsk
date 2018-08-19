package com.mikri.mikri.weatherinkrsk;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class weather_main extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "updateWeather";
    private static final String WIDGET_WEATHER_TEMP_CLICKED    = "goToWebSite";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_main);
        watchWidget = new ComponentName(context, weather_main.class);

        CharSequence widgetText = "+--.- °C";
        remoteViews.setTextViewText(R.id.widgetTemp, widgetText);
        remoteViews.setTextViewText(R.id.widgetPressure, "--");
        remoteViews.setTextViewText(R.id.widgetHumidity, "--%");

        remoteViews.setOnClickPendingIntent(R.id.imgBtnUpdate, getPendingSelfIntent(context, SYNC_CLICKED));
        remoteViews.setOnClickPendingIntent(R.id.widgetTemp, getPendingSelfIntent(context, WIDGET_WEATHER_TEMP_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        RemoteViews remoteViews;
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_main);

        if (SYNC_CLICKED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            ComponentName watchWidget;

            watchWidget = new ComponentName(context, weather_main.class);

            CharSequence widgetText = "+24.0 °C";
            remoteViews.setTextViewText(R.id.widgetTemp, widgetText);
            remoteViews.setTextViewText(R.id.widgetPressure, "753");
            remoteViews.setTextViewText(R.id.widgetHumidity, "100%");

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }

        if (WIDGET_WEATHER_TEMP_CLICKED.equals(intent.getAction())) {
            try {
                String siteURL = "http://www.somesite.ru/";
                Intent webIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(siteURL));
                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(webIntent);
            } catch (RuntimeException e) {
                // The url is invalid, maybe missing http://
                e.printStackTrace();
            }
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
