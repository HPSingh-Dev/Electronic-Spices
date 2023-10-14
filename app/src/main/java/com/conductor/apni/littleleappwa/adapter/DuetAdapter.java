package com.conductor.apni.littleleappwa.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.conductor.apni.littleleappwa.DuetActivity;
import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.data.DuetItemPojo;
import com.conductor.apni.littleleappwa.data.QItemPojo;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;

import java.util.List;


/**
 * Created by Saipro on 25-02-2017.
 */
public class DuetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<DuetItemPojo> seriesArrayList;
    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_IMAGE = 1;

    public DuetAdapter(Activity activity) {
        this.activity = activity;
    }

    public List<DuetItemPojo> getSeriesArrayList() {
        return seriesArrayList;
    }

    public void setSeriesArrayList(List<DuetItemPojo> seriesArrayList) {
        this.seriesArrayList = seriesArrayList;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.duet_item, parent, false);
            return new MyImageViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.duet_item_one, parent, false);
            return new MyImageViewHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        DuetItemPojo duetItemPojo=seriesArrayList.get(position);
        if(duetItemPojo.getUser_type()==1){
            return VIEW_TYPE_NORMAL;
        }else{
            return VIEW_TYPE_IMAGE;
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, int position) {
        MyImageViewHolder holder = (MyImageViewHolder) mholder;
        DuetItemPojo adSubCategory=seriesArrayList.get(position);
        //holder.categoryImage.setTe(adSubCategory.getCategory_name());
//        ServiceCategory avaialbleCityPojo=seriesArrayList.get(position);
       // holder.categoryText.setText(adSubCategory.getLine());
        holder.resultEdit.setText(adSubCategory.getText(), TextView.BufferType.SPANNABLE);
        holder.categoryText.setTag(position);
        if(adSubCategory.getUser_type()==2 && position<seriesArrayList.size()-1){
            int length=adSubCategory.getText().length();
            ((DuetActivity)activity).setColorWord(holder.resultEdit,0, length);
        }else if(adSubCategory.getUser_type()==2){
            holder.resultEdit.setTextColor(activity.getResources().getColor(R.color.black));
        }else if(adSubCategory.getUser_type()==1 && position<seriesArrayList.size()-1){
            int length=adSubCategory.getText().length();
            ((DuetActivity)activity).setColorWordOne(holder.resultEdit,0, length);
        }else{
            holder.resultEdit.setTextColor(activity.getResources().getColor(R.color.black));
        }

        holder.categoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(Integer)view.getTag();
                DuetItemPojo adSubCategory=seriesArrayList.get(pos);
                if(adSubCategory.getUser_type()==1){
                    String txt=adSubCategory.getText();
                    if(!txt.isEmpty()) {
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                        ((DuetActivity) activity).speak(txt, view);
                    }
                }else{
//                    if(!DuetActivity.isPlay){
//                        ((DuetActivity)activity).startRecording(view);
//                    }else{
//                        ((DuetActivity)activity).stopRecording(view);
//                    }
                    String txt=adSubCategory.getText();
                    if(!txt.isEmpty()) {
                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0);
                        ((DuetActivity) activity).speak(txt, view);
                    }
                }

            }
        });
//
//        String urlImage=adSubCategory.getCategory_image_path();
//        urlImage=urlImage.trim();
//      //  Log.d("checkLogin","url is "+urlImage);
//        if(urlImage!=null && urlImage.length()>0) {
//            Picasso.with(activity).load(urlImage).placeholder(R.mipmap.category_dummy).error(R.mipmap.category_dummy).into(holder.categoryImage);
//        }else{
//            holder.categoryImage.setImageResource(R.mipmap.category_dummy);
//        }
    }

    public void callProduct(String tag_Id,String name) {

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
