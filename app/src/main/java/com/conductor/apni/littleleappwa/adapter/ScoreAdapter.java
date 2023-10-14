package com.conductor.apni.littleleappwa.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.conductor.apni.littleleappwa.HomeActivity;
import com.conductor.apni.littleleappwa.R;
import com.conductor.apni.littleleappwa.data.ScorePojo;
import com.conductor.apni.littleleappwa.data.StoryPojo;
import com.conductor.apni.littleleappwa.utils.SessionManager;

import java.util.List;

/**
 * Created by Saipro on 25-02-2017.
 */

public class ScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<ScorePojo> seriesArrayList;
    private SessionManager sessionManager;
    private final int VIEW_TYPE_NORMAL = 0;
    private final int VIEW_TYPE_IMAGE = 1;

    public ScoreAdapter(Activity activity, List<ScorePojo> payableArrayList) {
        this.seriesArrayList = payableArrayList;
        this.activity = activity;
        sessionManager=new SessionManager(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.score_view, parent, false);
            return new MyViewHolder(itemView);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mholder, int position) {
        ScorePojo adDetails = seriesArrayList.get(position);
        MyViewHolder holder=(MyViewHolder)mholder;
        holder.parentrelative.setTag(position);

        holder.titleText.setText(adDetails.getName());

        holder.messageText.setText("Score : "+adDetails.getCorrect_words()+"/"+adDetails.getTotal_words());

    }



    @Override
    public int getItemCount() {
        return seriesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText, messageText, dateText, statusText;
        private ImageView notiView,shareview;
        private CardView parentrelative;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            notiView = (ImageView) itemView.findViewById(R.id.adimage);
            shareview = (ImageView) itemView.findViewById(R.id.shareimage);
           // shareview.setImageDrawable(Util.setVectorForPreLollipop(R.drawable.ic_share_black_24dp,activity));
            messageText = (TextView) itemView.findViewById(R.id.messageText);
            dateText = (TextView) itemView.findViewById(R.id.dateText);
            statusText = (TextView) itemView.findViewById(R.id.statusText);
            parentrelative = (CardView) itemView.findViewById(R.id.parent);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    AlertDialog alert;
    private void showMessage(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Content");
        builder.setMessage(content)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        alert.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();
    }

//    public Dialog alertDialog;
//    public ImageView dialog_image_view;
//    private RelativeLayout image_view_dialog;
//
//    public void showProfileFullImage(Activity activity, String BlobId) {
//
//        alertDialog = new Dialog(activity);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        alertDialog.setContentView(R.layout.image_view);
//
//        dialog_image_view = (ImageView) alertDialog.findViewById(R.id.dialog_imageview);
//        image_view_dialog = (RelativeLayout) alertDialog.findViewById(R.id.image_view_dialog);
//
//
//        String imageUrl = BlobId;
//        if(imageUrl!=null && imageUrl.length()>0){
//            Picasso.with(activity)
//                    .load(imageUrl)
//                    .placeholder(R.drawable.noimage) // optional
//                    .error(R.drawable.noimage)
//                    // optional
//                    .into(dialog_image_view, new Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            // Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//                            return;
//
//                        }
//                    });
//
//        }
//        alertDialog.show();
//
//        image_view_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//    }

}
