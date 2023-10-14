package com.conductor.apni.littleleappwa.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.data.AnswerPojo;

import java.util.List;


/**
 * Created by Saipro on 25-02-2017.
 */
public class AnswerAdapterTwo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<AnswerPojo> seriesArrayList;
    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_IMAGE = 1;

    public AnswerAdapterTwo(Activity activity) {
        this.activity = activity;
    }

    public List<AnswerPojo> getSeriesArrayList() {
        return seriesArrayList;
    }

    public void setSeriesArrayList(List<AnswerPojo> seriesArrayList) {
        this.seriesArrayList = seriesArrayList;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answer_item, parent, false);
            return new MyImageViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.duet_item_one, parent, false);
            return new MyImageViewHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
//        DuetItemPojo duetItemPojo=seriesArrayList.get(position);
//        if(duetItemPojo.getUser_type()==1){
//            return VIEW_TYPE_NORMAL;
//        }else{
//            return VIEW_TYPE_IMAGE;
//        }
        return VIEW_TYPE_NORMAL;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, int position) {
        MyImageViewHolder holder = (MyImageViewHolder) mholder;
        AnswerPojo adSubCategory=seriesArrayList.get(position);
        //holder.categoryImage.setTe(adSubCategory.getCategory_name());
//        ServiceCategory avaialbleCityPojo=seriesArrayList.get(position);
       // holder.categoryText.setText(adSubCategory.getLine());
        holder.resultEdit.setText(adSubCategory.getLine(), TextView.BufferType.SPANNABLE);
        if(position%2==0){
            holder.resultEdit.setTextColor(activity.getResources().getColor(R.color.black));
        }else{
            holder.resultEdit.setTextColor(activity.getResources().getColor(R.color.storycolorone));
        }
        holder.categoryText.setTag(position);
    }

    @Override
    public int getItemCount() {
        //return 4;
        return seriesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        //private LinearLayout parent;
        private TextView resultEdit, categoryText;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
           // parent = (LinearLayout) itemView.findViewById(R.id.parent);
            categoryText = (TextView) itemView.findViewById(R.id.categoryText);
            resultEdit = (TextView) itemView.findViewById(R.id.resultEdit);
        }
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        //private LinearLayout parent;
        private TextView resultEdit, categoryText;

        public MyImageViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            // parent = (LinearLayout) itemView.findViewById(R.id.parent);
            categoryText = (TextView) itemView.findViewById(R.id.categoryText);
            resultEdit = (TextView) itemView.findViewById(R.id.resultEdit);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
