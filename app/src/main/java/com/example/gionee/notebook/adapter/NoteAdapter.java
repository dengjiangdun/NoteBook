package com.example.gionee.notebook.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gionee.notebook.R;
import com.example.gionee.notebook.model.Note;
import com.example.gionee.notebook.util.TimeUtils;

import java.util.List;

/**
 * Created by johndon on 2/25/17.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private Context mContext;
    private List<Note> mListNote;
    private LongClickListener mLongClickListener;
    private int[] mNoteTypeImages = new int[]{R.drawable.note_type_meeting,R.drawable.note_type_memory_day,
            R.drawable.note_type_remind,R.drawable.note_type_schedule};

    public NoteAdapter(Context context,LongClickListener longClickListener,List<Note> noteList){
        mContext = context;
        mLongClickListener = longClickListener;
        mListNote = noteList;
    }


    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item_layout,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, final int position) {
        Note note=mListNote.get(position);
        String content = note.getContent();
        holder.tvContent.setText(Html.fromHtml(content));
        String date = TimeUtils.progressDateUseMSReturnWithYear(note.getUpdateTime());
        holder.tvDate.setText(date);
        holder.tvType.setText(note.getType());
        holder.ivType.setImageResource(mNoteTypeImages[Integer.valueOf(note.getTypeId())]);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongClickListener.longClick(position,holder.tvContent.getText().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListNote==null?0:mListNote.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private TextView tvDate;
        private TextView tvType;
        private View view;
        private ImageView ivType;
        public NoteHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_item_content);
            tvDate = (TextView) itemView.findViewById(R.id.tv_item_date);
            tvType = (TextView) itemView.findViewById(R.id.tv_item_type);
            view = itemView;
            ivType = (ImageView) itemView.findViewById(R.id.iv_note_item_type);
        }
    }

    public interface LongClickListener{
        public void longClick(int position,String mgs);

    }

}
