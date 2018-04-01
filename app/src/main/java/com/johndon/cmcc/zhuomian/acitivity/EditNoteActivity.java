package com.johndon.cmcc.zhuomian.acitivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bright.cmcc.umclib.JumpUmcActivity;
import com.johndon.cmcc.zhuomian.R;
import com.johndon.cmcc.zhuomian.model.Note;
import com.johndon.cmcc.zhuomian.util.StyleUtil;
import com.johndon.cmcc.zhuomian.util.TimeUtils;

import net.tsz.afinal.FinalDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by johndon on 2/28/17.
 */

public class EditNoteActivity extends GnBaseActivity {
    private RichEditor mEditor;
    private TextView mPreview;
    private LinearLayout mLlNoteType;
    private ImageView mIvNoteType;
    private TextView mTvUpdateDate;
    private TextView mTvChooseLabel;
    private ImageView mIvBack;
    private View mVInputDialog;
    private EditText mEtLinkContent;
    private EditText mEtLinkAddress;
    private TextView mTvSave;
    private AlertDialog mAlertDialog;
    private static final String APP_ID = "300011857608";
    //suishenriji"300011857607";
    //wangwangbeiwang"300011857606";
    //zhimabianjian"300011857605";
    //"300011847471";
    private static final String APP_KEY = "2597E1B3A9F4805E242D2513F88B2D7B";
    //suishenrij"467071946C8C8DC744BAFAC103684A05";
    //wangwangbeiwang"C1030785259F868F3137532A488BDF7C";
    //zhimabianjian"D97BA497ABB2A577D9E2634475B94CF0";
    //"FFB2BE445A4D8CD8151C7959B477D8AF";

    private final static int ALBUM_REQUEST_CODE = 1;
    private final static int CAMERE_REQUEST_CODE = 3;
    private final static int REQUEST_STORAGE_PERMITON = 4;
    private int[] mNoteTypeImages = new int[]{R.drawable.note_type_meeting,R.drawable.note_type_memory_day,
            R.drawable.note_type_remind,R.drawable.note_type_schedule};
    private static final String[] noteType = new String[]{"会议","纪念日","备忘","计划"};
    private int position = -1;
    private Note mNote;
    private static  final String KEY_GET_INDEX = "key";
    private boolean isUpdate = false;
    private FinalDb mFinalDb;
    @Override
    protected int getLayoutId() {
        return R.layout.edit_note_activity_layout;
    }

    @Override
    protected void initListener() {
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                mPreview.setText(text);
            }
        });

        if (StyleUtil.getTheme(this) == R.style.style_night){
            mEditor.setEditorBackgroundColor(getResources().getColor(R.color.night_mode_content_background));
        } else {
            mEditor.setEditorBackgroundColor(getResources().getColor(R.color.day_mode_content_background));
        }

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
                });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        }
        );

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ALBUM_REQUEST_CODE);


            }
        }
        );

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                createDialogView();
                builder.setView(mVInputDialog);
                mAlertDialog = builder.create();
                mAlertDialog.show();

            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
        findViewById(R.id.iv_notes_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUmcActivity.StartActivity(EditNoteActivity.this, APP_ID, APP_KEY);
            }
        });

        mLlNoteType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseTypeDialog();
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNoteActivity.this.finish();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMITON){

        }
    }

    private void showChooseTypeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose type").
                setItems(noteType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIvNoteType.setImageResource(mNoteTypeImages[which]);
                        mTvChooseLabel.setText(noteType[which]);
                        position = which;
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void createDialogView(){
        mVInputDialog = LayoutInflater.from(this).inflate(R.layout.dialog_input_laypout,null);
        mEtLinkContent = (EditText) mVInputDialog.findViewById(R.id.et_link_content);
        mEtLinkAddress = (EditText) mVInputDialog.findViewById(R.id.et_link_address);
        mTvSave = (TextView) mVInputDialog.findViewById(R.id.tv_save_link);
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = mEtLinkAddress.getText().toString();
                String content = mEtLinkContent.getText().toString();
                if (!TextUtils.isEmpty(link) && !TextUtils.isEmpty(content)){
                    mEditor.insertLink(link,content);
                }
                if (mAlertDialog != null && mAlertDialog.isShowing()){
                    mAlertDialog.dismiss();
                }
            }
        });
    }



    @Override
    protected void initView() {
        super.initView();
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder(getString(R.string.inster_content));
        mPreview = (TextView) findViewById(R.id.preview);
        mTvChooseLabel = (TextView) findViewById(R.id.tv_choose_note_label);
        mLlNoteType = (LinearLayout) findViewById(R.id.ll_note_layout);
        mIvNoteType = (ImageView) findViewById(R.id.iv_note_type);
        mTvUpdateDate = (TextView) findViewById(R.id.tv_note_update_date);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setVisibility(View.VISIBLE);
        mPreview.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        checkPermition();
        int index = getIntent().getIntExtra(KEY_GET_INDEX,-1);
        mFinalDb = FinalDb.create(this,true);
        mNote = new Note();
        if (index != -1){
            mNote = mFinalDb.findById(index,Note.class);
            for (int i = 0;i < noteType.length; ++i) {
                if (mNote.getType().equals(noteType[i])) {
                    position = i;
                    mIvNoteType.setImageResource(mNoteTypeImages[position]);
                    mTvChooseLabel.setText(noteType[position]);
                    mEditor.setHtml(mNote.getContent());
                    mTvUpdateDate.setText(TimeUtils.progressDateUseMSReturnWithYear(mNote.getUpdateTime()));
                    isUpdate = true;
                    mPreview.setText(mNote.getContent());
                    break;
                }
            }
        }
        String time = String.valueOf(System.currentTimeMillis());
        mNote.setUpdateTime(time);
        mTvUpdateDate.setText(TimeUtils.progressDateUseMSReturnWithYear(time));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERE_REQUEST_CODE){
            }else if (requestCode == ALBUM_REQUEST_CODE){
                String path = getAbsolutePath(this,data.getData());
                String newPath = zoomInPhoto(path);
                mEditor.insertImage(newPath,newPath);



            }
        }


    }
    private void checkPermition(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
        } else {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE_PERMITON);
           }
        }
    }




    private String zoomInPhoto(String preFileName){
        BitmapFactory.Options op = new BitmapFactory.Options();
        Bitmap bipMap = BitmapFactory.decodeFile(preFileName);
        int width = bipMap.getWidth();
        int height = bipMap.getHeight();
        int newWidth = 90;
        int newHeight = 90;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        bipMap = Bitmap.createBitmap(bipMap,0,0,width,height,matrix,true);
        FileOutputStream fileOut =null;
        String newFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/NoteBook/";
        File file = new File(newFilePath.trim());
        if ( !file.exists()){
            file.mkdirs();
        }
        String fileName =System.currentTimeMillis() + ".png";
        File myFile = new File(file.getAbsolutePath()+"/",fileName.trim());
        if (myFile.exists()){
            myFile.delete();
        }

        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOut = new FileOutputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bipMap != null&& fileOut != null){
            bipMap.compress(Bitmap.CompressFormat.PNG,50,fileOut);
        }
        return file.getAbsolutePath()+"/"+fileName;

    }



    private String getAbsolutePath(Context context,Uri uri) {
        if (uri == null) {
            return null;
        }
        String data = null;
        String scheme =uri.getScheme();
        if (scheme == null){
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)){
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},null,null,null);
            if (null != cursor){
                if (cursor.moveToFirst()){
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1){
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onBackPressed() {
         showIsSaveDialog();
    }

    private void showIsSaveDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.is_save_note)).setPositiveButton(getString(R.string.is_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (checkIsEmpaty()) {
                    showShortToast(getString(R.string.input_can_not_empty));
                } else {
                    mNote.setType(noteType[position]);
                    mNote.setContent(mEditor.getHtml());
                    mNote.setTypeId(String.valueOf(position));
                    saveNote();
                }
            }
        }).setNegativeButton(getString(R.string.is_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                EditNoteActivity.this.finish();
            }
        });
        builder.create().show();
    }

    private void saveNote(){
        if (isUpdate){
            mFinalDb.update(mNote,"id="+mNote.getId());
        } else {
            if (mNote.getContent().isEmpty() || mNote.getType().isEmpty()){
                showShortToast(getResources().getString(R.string.input_can_not_empty));
            }
            mFinalDb.save(mNote);
        }
        Log.d("EditActivity", "saveNote: "+mNote.getTypeId());
        this.finish();
    }

    private boolean checkIsEmpaty(){
        if (position == -1 || mEditor.getHtml().isEmpty()){
            return true;
        }
        return false;
    }
}
