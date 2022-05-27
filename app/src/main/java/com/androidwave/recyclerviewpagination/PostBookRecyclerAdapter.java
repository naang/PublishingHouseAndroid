package com.androidwave.recyclerviewpagination;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostBookRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
  private static final int VIEW_TYPE_LOADING = 0;
  private static final int VIEW_TYPE_NORMAL = 1;
  private boolean isLoaderVisible = false;
  int redclr = Color.parseColor("#ffd8cc");
  int greenclr = Color.parseColor("#ccffcc");
  int selectedColor = Color.parseColor("#ffb3ff");
  private List<PostItemBook> mPostBookItems;
  public   int id =0;
  public PostBookRecyclerAdapter(List<PostItemBook> postBookItems) {
    this.mPostBookItems = postBookItems;
  }

  @NonNull @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    switch (viewType) {
      case VIEW_TYPE_NORMAL:
        return new ViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_post1, parent, false));
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
      return position == mPostBookItems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
    } else {
      return VIEW_TYPE_NORMAL;
    }
    //return VIEW_TYPE_NORMAL;
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public int getItemCount() {
    mPostBookItems.forEach((item) -> {
      if(item.getCode().equals(ScanManagerDemo.handlingIsbn)){
        id =Integer.parseInt(item.getBookId()) -1;
        //ScanManagerDemo.mRecyclerView.smoothScrollToPosition(id);
        if(item.getQuantity().equalsIgnoreCase(item.getHandlingQuantity()))
        {
          ScanManagerDemo.mRecyclerView.smoothScrollToPosition(id);
          ScanManagerDemo.mRecyclerView.smoothScrollToPosition(0);
        }
        else
        {
          ScanManagerDemo.mRecyclerView.smoothScrollToPosition(id);
        }
      }
    });

    return mPostBookItems == null ? 0 : mPostBookItems.size();
  }

  public void addItems(List<PostItemBook> postBookItems) {
    mPostBookItems.addAll(postBookItems);
    notifyDataSetChanged();
  }

  public void addLoading() {
    isLoaderVisible = true;
    mPostBookItems.add(new PostItemBook("","","","","","","",""));
    notifyItemInserted(mPostBookItems.size() - 1);
  }

    public void removeLoading() {
    isLoaderVisible = false;
    int position = mPostBookItems.size() - 1;
    PostItemBook item = getItem(position);
    if (item != null) {
      mPostBookItems.remove(position);
      notifyItemRemoved(position);
    }
  }

  public void clear() {
    mPostBookItems.clear();
    notifyDataSetChanged();
  }

  PostItemBook getItem(int position) {
    return mPostBookItems.get(position);
  }

  public class ViewHolder extends BaseViewHolder {
    @BindView(R.id.idbook)
    TextView textViewIdBook;
    @BindView(R.id.name)
    TextView textViewName;
    @BindView(R.id.isbn)
    TextView textViewIsbn;
    @BindView(R.id.price)
    TextView textViewPrice;
    @BindView(R.id.handling)
    TextView textViewHandling;
    @BindView(R.id.quantity)
    TextView textViewEdquantity;
    @BindView(R.id.shalf)
    TextView textViewShalf;
    @BindView(R.id.txtshalf)
    TextView textViewtxtShalf;
    @BindView(R.id.balance)
    TextView textViewBalance;

    ViewHolder(View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    protected void clear() {

    }


    public void onBind(int position) {
      super.onBind(position);
      PostItemBook item = mPostBookItems.get(position);

      textViewIdBook.setText(item.getBookId());
      textViewName.setText(item.getName());
      textViewIsbn.setText(item.getCode());
      textViewPrice.setText(item.getPrice());
      textViewHandling.setText(item.getHandlingQuantity());
      textViewEdquantity.setText(item.getQuantity());
      textViewShalf.setText(item.getShalfNumber());
      textViewBalance.setText(item.getBalance());

      if(item.getQuantity().equalsIgnoreCase(item.getHandlingQuantity()))
      {
        itemView.setBackgroundColor(greenclr);
      }
      else
      {
        itemView.setBackgroundColor(redclr);
      }
      if(item.getCode().equalsIgnoreCase(ScanManagerDemo.handlingIsbn))
      {
        if(item.getQuantity().equalsIgnoreCase(item.getHandlingQuantity()))
        {
          //ScanManagerDemo.mRecyclerView.smoothScrollToPosition(position);
          itemView.setBackgroundColor(greenclr);
          //Toast.makeText(textViewName.getContext(), String.valueOf(item.getCode()), Toast.LENGTH_LONG).show();
          //Toast.makeText(textViewName.getContext(), String.valueOf(ScanManagerDemo.handlingIsbn), Toast.LENGTH_LONG).show();
          //ScanManagerDemo.mRecyclerView.smoothScrollToPosition(0);
          //ScanManagerDemo.handlingIsbn ="";
        }
        else
        {
          //ScanManagerDemo.mRecyclerView.smoothScrollToPosition(id);
          itemView.setBackgroundColor(selectedColor);
          //Toast.makeText(textViewName.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
        }
      }

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
