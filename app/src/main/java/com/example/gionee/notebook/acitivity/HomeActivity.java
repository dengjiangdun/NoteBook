package com.example.gionee.notebook.acitivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gionee.notebook.R;
import com.example.gionee.notebook.adapter.NoteAdapter;
import com.example.gionee.notebook.model.Note;
import com.example.gionee.notebook.service.NoteService;
import com.example.gionee.notebook.util.StyleUtil;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by johndon on 2/25/17.
 */

public class HomeActivity extends GnBaseActivity implements NoteAdapter.LongClickListener{
    private RecyclerView mRvNote;
    private ProgressDialog mProgressDialog;
    private ImageView mIvDeleteAllNotes;
    private ImageView mIvAddNotes;
    private ImageView mIvMode;
    private TextView mTvTitle;
    
    private List<Note> mNoteList;
    private NoteAdapter mNoteAdapter;
    private int position = 0;
    private int totalNotes;
    private final static String[] operation = new String[]{"Delete","Edit","Share"};
    private final static String KEY_INTENT = "key";
    private final static String KEY_SMG_TO = "smsto:";
    private final static String KEY_SMG_BODY = "sms_body";
    private final static String KEY_SEND_BROADCAST = "com.gionee.notebook";
    private final static String[] KEY_NOTE_TYPES = {"meeting","memory_day","remind","schedule"};
    private static final String[] noteType = new String[]{"meeting","memory day","remind","schedule"};
    @Override
    protected int getLayoutId() {
        return R.layout.home_activity;
    }

    @Override
    protected void initView() {
        mRvNote = (RecyclerView) findViewById(R.id.rv_note);
        mRvNote.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager =new GridLayoutManager(this,2);
        mRvNote.setLayoutManager(gridLayoutManager);
        mRvNote.setItemAnimator( new DefaultItemAnimator());
        mIvDeleteAllNotes = (ImageView) findViewById(R.id.iv_delete_all_notes);
        mIvDeleteAllNotes.setVisibility(View.VISIBLE);
        mIvAddNotes = (ImageView) findViewById(R.id.iv_add_note);
        mIvMode = (ImageView) findViewById(R.id.iv_notes_mode);
        mTvTitle = (TextView) findViewById(R.id.tv_title_note_book);
        if (StyleUtil.getTheme(this) == R.style.style_night) {
            mIvMode.setImageResource(R.drawable.day_mode);
        } else {
            mIvMode.setImageResource(R.drawable.night_mode);
        }
        mIvMode.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mIvAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,EditNoteActivity.class);
                startActivity(intent);
            }
        });

        mIvDeleteAllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyleUtil.changeTheme(HomeActivity.this,R.style.style_night);

            }
        });

        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NoteService.class);
                startService(intent);
                HomeActivity.this.sendBroadcast(intent);
            }
        });

        mIvMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StyleUtil.getTheme(HomeActivity.this) == R.style.style_night){
                    StyleUtil.changeTheme(HomeActivity.this,R.style.style_day);
                } else {
                    StyleUtil.changeTheme(HomeActivity.this,R.style.style_night);
                }
            }
        });
    }



    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.sure_to_delete)).
                setPositiveButton(getString(R.string.is_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllNote();
                        dialog.dismiss();
                    }
                }).setNegativeButton(getString(R.string.is_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void deleteAllNote()
    {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        totalNotes = mNoteList.size();
        mProgressDialog.setMax(totalNotes);
        mProgressDialog.show();
        final FinalDb finalDb = FinalDb.create(this);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mNoteList.size()>0)
                {
                    finalDb.delete(mNoteList.get(0));
                    mNoteList.remove(0);
                }

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mProgressDialog.setProgress(position+1);
                       position++;
                       mNoteAdapter.notifyDataSetChanged();
                       if (position == totalNotes)
                       {
                           mProgressDialog.dismiss();
                           timer.cancel();

                       }
                   }
               });
            }
        },0,500);

    }

    @Override
    protected void initData() {
        super.initData();
        FinalDb finalDb = FinalDb.create(this);
        mNoteList = finalDb.findAll(Note.class);
        mNoteAdapter = new NoteAdapter(this,this,mNoteList);
        mRvNote.setAdapter(mNoteAdapter);
        updateWidget();

    }

    @Override
    public void longClick(final int position,String mgs) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_please_choose)).setItems(operation, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switchLongClick(which,position);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void switchLongClick(int type,int position){
        switch (type) {
            case 0:
            {
                //delete the note
                FinalDb finalDb = FinalDb.create(this);
                finalDb.delete(mNoteList.get(position));
                mNoteList.remove(position);
                mRvNote.removeViewAt(position);
                updateWidget();
                break;
            }

            case 1:
            {
                //Edit the note
                Intent intent = new Intent(this,EditNoteActivity.class);
                intent.putExtra(KEY_INTENT,mNoteList.get(position).getId());
                startActivity(intent);
                break;
            }
           
            case 2:
            {
                 //share the note
                Uri uri = Uri.parse(KEY_SMG_TO);
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra(KEY_SMG_BODY,mNoteList.get(position).getContent());
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog !=null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
    }

    private void updateWidget(){
       FinalDb finalDb = FinalDb.create(this,true);
        List<Note> notes;
        Intent intent = new Intent(KEY_SEND_BROADCAST);
        int size;
        for (int i = 0;i < 4;++i){
            notes = finalDb.findAllByWhere(Note.class,"typeId = \""+i+"\"");
            size = (notes == null ? 0 : notes.size());
            intent.putExtra(KEY_NOTE_TYPES[i],size);
        }
        sendBroadcast(intent);
    }

}
