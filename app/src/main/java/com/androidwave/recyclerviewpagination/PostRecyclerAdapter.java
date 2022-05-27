package com.androidwave.recyclerviewpagination;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import android.widget.Toast;

import static com.androidwave.recyclerviewpagination.Main3Activity.totalPage;
import static com.androidwave.recyclerviewpagination.ScanManagerDemo.context;

public class PostRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
  private static final int VIEW_TYPE_LOADING = 0;
  private static final int VIEW_TYPE_NORMAL = 1;
  private boolean isLoaderVisible = false;
  int greenclr = Color.parseColor("#99ff99");
  int yellowclr = Color.parseColor("#ffff66");

  private List<PostItem> mPostItems;

  public PostRecyclerAdapter(List<PostItem> postItems) {
    this.mPostItems = postItems;
  }

  @NonNull @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    switch (viewType) {
      case VIEW_TYPE_NORMAL:
        return new ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));

      case VIEW_TYPE_LOADING:
        return new ProgressHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
      default:
        return null;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    holder.onBind(position);
  }

  @Override
  public int getItemViewType(int position) {
    if (isLoaderVisible) {
      return position == mPostItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
    } else {
      return VIEW_TYPE_NORMAL;
    }
  }

  @Override
  public int getItemCount() {
    return mPostItems == null ? 0 : mPostItems.size();
  }

  public void addItems(List<PostItem> postItems) {
    mPostItems.addAll(postItems);
    notifyDataSetChanged();
  }

  public void addLoading() {
    isLoaderVisible = true;
    mPostItems.add(new PostItem("", "", "", "", 0,"","","","","",""));
    notifyItemInserted(mPostItems.size() - 1);
  }

  public void removeLoading() {
    isLoaderVisible = false;
    int position = mPostItems.size() - 1;
    PostItem item = getItem(position);
    if (item != null) {
      mPostItems.remove(position);
      notifyItemRemoved(position);
    }
  }

  public void clear() {
    mPostItems.clear();
    notifyDataSetChanged();
  }

  PostItem getItem(int position) {
    return mPostItems.get(position);
  }

  public class ViewHolder extends BaseViewHolder {
    @BindView(R.id.id)
    TextView textViewId;
    @BindView(R.id.icon)
    ImageView imageView;
    @BindView(R.id.title)
    TextView textViewTitle;
    @BindView(R.id.desc)
    TextView textViewDescription;
   /* @BindView(R.id.time)
    TextView textViewTime;*/
    @BindView(R.id.comment)
    TextView textViewComment;
    @BindView(R.id.isfinished)
    TextView textViewIsfinished;
    @BindView(R.id.isreturned)
    TextView textViewIsreturned;
    @BindView(R.id.InPorogress)
    TextView textViewInProgress;
    @BindView(R.id.isTo)
    TextView textViewIsTo;
    @BindView(R.id.status)
    TextView textViewStatus;
    @BindView(R.id.statusTo)
    TextView textViewStatusTo;


    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    protected void clear() {

    }

    public void onBind(int position) {
      super.onBind(position);
      PostItem item = mPostItems.get(position);

      textViewId.setText(item.getId());
      textViewTitle.setText(item.getTitle());
      textViewDescription.setText(item.getDescription());
      //textViewTime.setText(item.getTime());
      imageView.setImageResource(item.getImageId());
      textViewComment.setText(item.getCmment());
      textViewIsfinished.setText(item.getIsfinished());
      textViewIsreturned.setText(item.getIsreturned());
      textViewInProgress.setText(item.getInProgress());
      textViewIsTo.setText(item.getIsTo());
      textViewStatus.setText(item.getStatus());
      textViewStatusTo.setText(item.getStatusTo());

      if(item.getInProgress().equalsIgnoreCase("true"))
      {
        itemView.setBackgroundColor(yellowclr);
      }
      else
      if(item.getStatus().equalsIgnoreCase("3") || item.getStatusTo().equalsIgnoreCase("3")){
        itemView.setBackgroundColor(greenclr);
      }
      else
        itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        //Toast.makeText(textViewId.getContext(), String.valueOf(item.getId()), Toast.LENGTH_LONG).show();
       // Toast.makeText(textViewId.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
   /*   if(item.getInProgress().equalsIgnoreCase("true"))
      {
          //Toast.makeText(textViewId.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
          //String aaa =item.getId();
        itemView.setBackgroundColor(yellowclr);

      }
      else
        itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));*/

/*      if(item.getStatus().equalsIgnoreCase("3") || item.getStatusTo().equalsIgnoreCase("3")){
        itemView.setBackgroundColor(greenclr);
      }
      else
        imageView.setImageResource(item.getImageId());*/


/*      if(item.getIsTo().equalsIgnoreCase("true")){
        if(item.getStatusTo().equalsIgnoreCase("3"))

          itemView.setBackgroundColor(greenclr);
        else
          imageView.setImageResource(item.getImageId());
        //return convertView;
      }
      else
      {
        if(item.getStatus().equalsIgnoreCase("3"))

          itemView.setBackgroundColor(greenclr);
        else

          imageView.setImageResource(item.getImageId());
      }*/

/*     if(item.getIsreturned().equalsIgnoreCase("true"))
      {
        imageView.setImageResource(R.drawable.icreturn);
        textViewDescription.setTextColor(Color.GRAY);
        textViewDescription.setText("--" + " "+item.getDescription()+" "+ "--");
      }*/

/*      if(item.getIsTo().equalsIgnoreCase("true")){
        if(item.getStatusTo().equalsIgnoreCase("3"))
          //imageView.setImageResource(R.drawable.checkmark);
          itemView.setBackgroundColor(greenclr);
        if(item.getStatusTo().equalsIgnoreCase("2"))
          itemView.setBackgroundColor(yellowclr);
        else
          imageView.setImageResource(item.getImageId());
      }
      else
      {
        if(item.getStatus().equalsIgnoreCase("3"))
          //imageView.setImageResource(R.drawable.checkmark);
          itemView.setBackgroundColor(greenclr);
        if(item.getStatusTo().equalsIgnoreCase("2"))
          itemView.setBackgroundColor(yellowclr);
        else
          imageView.setImageResource(item.getImageId());
      }*/


    }
  }

  public class ProgressHolder extends BaseViewHolder {
    ProgressHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @Override
    protected void clear() {
    }
  }
}
