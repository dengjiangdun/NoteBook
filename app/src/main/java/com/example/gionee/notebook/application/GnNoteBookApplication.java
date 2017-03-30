package com.example.gionee.notebook.application;

import android.app.Application;

/**
 * Created by johndon on 2/25/17.
 */

public class GnNoteBookApplication extends Application {
    private static GnNoteBookApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static GnNoteBookApplication getInstance(){
        if (instance == null){
            instance = new GnNoteBookApplication();
        }
        return instance;
    }



}
