package knormal.justcart.JustCart_ver4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.JustCart_ver4.R;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public CategoryAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView Name;
        protected TextView Price;
        protected TextView Desc;
        protected TextView Location;
        protected ImageView Image_view;


        public CustomViewHolder(View view) {
            super(view);
            this.Name = (TextView) view.findViewById(R.id.tv_list_name);
            this.Price = (TextView) view.findViewById(R.id.tv_list_price);
            this.Desc = (TextView) view.findViewById(R.id.tv_list_context);
            this.Location = (TextView) view.findViewById(R.id.tv_list_location);
            this.Image_view = (ImageView) view.findViewById(R.id.iv);
        }
    }


    //리스트뷰가 처음으로 생성될 때 생명주기
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    //각 아이템들에 대한 매칭
    @Override
    //실제 추가될 때에 대한 생명주기
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        Glide.with(viewholder.itemView)
                .load(mList.get(position).getImage())
                .into(viewholder.Image_view);
        viewholder.Name.setText(mList.get(position).getName());
        viewholder.Price.setText(mList.get(position).getPrice());
        viewholder.Desc.setText(mList.get(position).getDesc());
        viewholder.Location.setText(mList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}