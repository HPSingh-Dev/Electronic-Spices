package com.conductor.apni.littleleappwa.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.data.StoryItemPojo;

import java.util.List;


/**
 * Created by Saipro on 25-02-2017.
 */
public class StoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<StoryItemPojo> seriesArrayList;
    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_IMAGE = 1;

    public StoryItemAdapter(Activity activity, List<StoryItemPojo> subCategoryList) {
        this.seriesArrayList = subCategoryList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.best_item, parent, false);
            return new MyViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.best_item, parent, false);
            return new MyImageViewHolder(itemView);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_IMAGE;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, int position) {
        MyImageViewHolder holder = (MyImageViewHolder) mholder;
        StoryItemPojo adSubCategory=seriesArrayList.get(position);
        //holder.categoryImage.setTe(adSubCategory.getCategory_name());
//        ServiceCategory avaialbleCityPojo=seriesArrayList.get(position);
        holder.categoryText.setText(adSubCategory.getLine());
        holder.priceText.setText(adSubCategory.getLine());
        holder.parent.setTag(position);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(Integer)view.getTag();
                StoryItemPojo adSubCategory=seriesArrayList.get(pos);
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
        private TextView categoryText, priceText;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
           // parent = (LinearLayout) itemView.findViewById(R.id.parent);
            categoryText = (TextView) itemView.findViewById(R.id.categoryText);
            priceText = (TextView) itemView.findViewById(R.id.priceText);
        }
    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private CardView parent;
        private TextView categoryText, priceText;

        public MyImageViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            parent = (CardView) itemView.findViewById(R.id.parent);
            categoryText = (TextView) itemView.findViewById(R.id.categoryText);
            priceText = (TextView) itemView.findViewById(R.id.priceText);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
