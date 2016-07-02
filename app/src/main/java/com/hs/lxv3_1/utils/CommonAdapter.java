package com.hs.lxv3_1.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by Holy-Spirit on 2015/12/29.
 */
public abstract  class CommonAdapter<T> extends BaseAdapter
{


    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context,List<T> datas){
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * ��ʵ����ֱ�ӳ��������������д�������µķ��������е㲻���һ���Եı��
     *          ��ΪViewHolder�������Ļ�ȡ��Ҫ�Լ����ò���������������Ͳ��ܽ��ж���Adapter�ĸ�����
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public  View getView(int position, View convertView, ViewGroup parent){

        int layoutId = getLayoutId();

        ViewHolder mHolder = ViewHolder.get(convertView,mContext,layoutId,parent,position);

        convert(mHolder,getItem(position));

        return mHolder.getConvertView();
    }


    /**
     * ����˷�����ֱ����ɿ���ʱ��Ҫ��Item��������
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder,T t);


    /**
     * ���󷽷�����ȡItem����Ҫ�Ĳ��ֵ�id
     * @return
     */
    public abstract int getLayoutId();
}
