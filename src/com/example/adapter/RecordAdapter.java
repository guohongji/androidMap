package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.debristravels.R;

/**
 * Created by Administrator on 2018/4/1 0001.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    //չʾ����
     String[] texts;
    //��ʾͼƬ
    Bitmap[] bitmaps;

public RecordAdapter(Context context,String[] texts, Bitmap[] bitmaps){
    this.mContext = context;
    this.texts = texts;
    this.bitmaps = bitmaps;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_layout,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setTag(position);
        holder.mImageView.setTag(position);
        holder.mLinearLayoutl.setTag(position);
        holder.mRelativeLayout.setTag(position);



//�����ֺ�ͼƬ��ӵ���Ŀ��
        holder.mTextView.setText(texts[position]);
        holder.mImageView.setImageBitmap(bitmaps[position]);

    }
    @Override
    public int getItemCount() {
        //��ѯ����Ҫ�������ٸ���Ŀ
        if(texts != null){
            return texts.length;
        }else{
            return 0;
        }



    }

    @Override
    public long getItemId(int position) {
        //������Ŀid
        return super.getItemId(position);
    }



    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // ��ȡ��չ��
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

//�ڲ���:
    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public RelativeLayout mRelativeLayout;
        public LinearLayout mLinearLayoutl;

        public  ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.diary_text);
            mImageView = (ImageView) view.findViewById(R.id.diary_image);
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mRecordRelativeLayout);
            mLinearLayoutl = (LinearLayout) view.findViewById(R.id.mRecordLinearLayout);


            //ע������¼�
            //view.setOnClickListener(RecordAdapter.this);

            mRelativeLayout.setOnClickListener(RecordAdapter.this);
            mLinearLayoutl.setOnClickListener(RecordAdapter.this);
           mTextView.setOnClickListener(RecordAdapter.this);
            mImageView.setOnClickListener(RecordAdapter.this);
        }
    }
    ////////////////////////////����Ϊitem�������///////////////////////////////

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    /**item�����ж���ؼ����Ե��**/
    public enum ViewName{
        ITEM,
        PRACTISE
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public interface OnRecyclerViewItemClickListener{
        void onClick(View view,ViewName viewName,int position);
    }
    //ʵ�ּ������ӿڵĳ��󷽷�
    @Override
    public void onClick(View v) {
        int position = 0;
        if(v.getTag() != null) {
             position = (int) v.getTag();
        }
        if(mOnItemClickListener != null){
            switch (v.getId()){
                case R.id.diary_image:
                    mOnItemClickListener.onClick(v,ViewName.PRACTISE,position);
                    break;
                default:
                    mOnItemClickListener.onClick(v,ViewName.ITEM,position);
                    break;
            }
        }
    }
}
