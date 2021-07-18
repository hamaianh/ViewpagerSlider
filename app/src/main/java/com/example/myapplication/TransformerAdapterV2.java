package com.example.myapplication;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.lib.SliderLayoutV2;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by daimajia on 14-5-29.
 */
public class TransformerAdapterV2 extends RecyclerView.Adapter<TransformerAdapterV2.ViewHolder> {
    private List<SliderLayoutV2.Transformer> mListAnimation;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public TransformerAdapterV2(List<SliderLayoutV2.Transformer> listAnimation) {
        mListAnimation = listAnimation;
    }

    @NonNull
    @NotNull
    @Override
    public TransformerAdapterV2.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TransformerAdapterV2.ViewHolder holder, int position) {
        holder.bindData(mListAnimation.get(position));
        holder.itemView.setOnClickListener(v -> {
            if(mOnItemClickListener != null)
                mOnItemClickListener.onItemAnimationClick(mListAnimation.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (mListAnimation != null && mListAnimation.size() > 0)
            return mListAnimation.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.name_animation);
        }

        public void bindData(SliderLayoutV2.Transformer animation) {
            if (animation != null && !TextUtils.isEmpty(animation.name()))
                mTextView.setText(animation.name());
        }
    }

    public interface OnItemClickListener{
        void onItemAnimationClick(SliderLayoutV2.Transformer animation);
    }
}
