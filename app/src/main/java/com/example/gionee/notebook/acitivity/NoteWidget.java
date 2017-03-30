package com.example.gionee.notebook.acitivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.gionee.notebook.R;
import com.example.gionee.notebook.model.Note;

import net.tsz.afinal.FinalDb;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by johndon on 2/27/17.
 */

public class NoteWidget extends AppWidgetProvider {
    private static final String KEY_INTENT_ACTION = "com.gionee.notebook";
    private RemoteViews mRomoteViews;
    private static HashSet mHashSet = new HashSet();
    private int size = 0;
    private final static String[] KEY_NOTE_TYPES = {"meeting","memory_day","remind","schedule"};
    private static int[] typeNums = new int[4];
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("widget", intent.getAction()+"onReceive");
        if (KEY_INTENT_ACTION.equals(intent.getAction())){
            int size;
            for (int i = 0;i < 4; ++i){
                size = intent.getIntExtra(KEY_NOTE_TYPES[i],0);
                typeNums[i] = size;
            }
            updateAllAppWidgets(context,AppWidgetManager.getInstance(context),mHashSet);

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidget : appWidgetIds) {
            mHashSet.add(Integer.valueOf(appWidget));
        }
        updateAllAppWidgets(context,appWidgetManager,mHashSet);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        FinalDb finaDb = FinalDb.create(context);
        List<Note> noteList;
        for (int i = 0;i < 4;++i){
            noteList = finaDb.findAllByWhere(Note.class,"typeId = \""+i+"\"");
            typeNums[0] = noteList.size();
        }
        updateAllAppWidgets(context,AppWidgetManager.getInstance(context),mHashSet);


    }
    private void updateAllAppWidgets(Context context, AppWidgetManager appWidgetManagner, Set set){
        int appId;
        Iterator iterator = set.iterator();
        while (iterator.hasNext()){
            appId = ((Integer)iterator.next()).intValue();
            mRomoteViews = new RemoteViews(context.getPackageName(), R.layout.note_widget_layout);
            mRomoteViews.setTextViewText(R.id.tv_widget_meeting,String.valueOf(typeNums[0]));
            mRomoteViews.setTextViewText(R.id.tv_widget_memory_day,String.valueOf(typeNums[1]));
            mRomoteViews.setTextViewText(R.id.tv_widget_remind,String.valueOf(typeNums[2]));
            mRomoteViews.setTextViewText(R.id.tv_widget_schedule,String.valueOf(typeNums[3]));
            Intent intent = new Intent(context,HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            mRomoteViews.setOnClickPendingIntent(R.id.ll_widget_layout,pendingIntent);
            appWidgetManagner.updateAppWidget(appId,mRomoteViews);
        }
    }

}
