package com.example.adapter;

/**
 * Created by WZJSB-01 on 2017/12/5.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debristravels.R;

import java.util.ArrayList;

public class Adapter_Diary extends RecyclerView.Adapter<Adapter_Diary.ViewHolder> implements View.OnClickListener{
private ArrayList<String> datas= null;


        public Adapter_Diary(ArrayList<String> datas) {
        this.datas = datas;
        }
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(datas.get(position));
        holder.mTextView.setTag(position);
        holder.itemView.setTag(position);


        }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    //��ȡ���ݵ�����
@Override
public int getItemCount() {
        return datas.size();
        }




    class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public  ViewHolder(View view){
        super(view);
        mTextView = (TextView) view.findViewById(R.id.text);

        //ע������¼�
        view.setOnClickListener(Adapter_Diary.this);
        mTextView.setOnClickListener(Adapter_Diary.this);

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
        int position = (int)v.getTag();
        if(mOnItemClickListener != null){
            switch (v.getId()){
                case R.id.text:
                    mOnItemClickListener.onClick(v,ViewName.PRACTISE,position);
                    break;
                default:
                    mOnItemClickListener.onClick(v,ViewName.ITEM,position);
                    break;
            }
        }

    }

}
