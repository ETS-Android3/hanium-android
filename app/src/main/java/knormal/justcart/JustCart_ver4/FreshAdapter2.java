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

public class FreshAdapter2 extends RecyclerView.Adapter<FreshAdapter2.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public FreshAdapter2(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView Name;
        protected TextView Price;
        protected TextView incomeDate;
        protected TextView shelfLife;
        protected ImageView Image_view;


        public CustomViewHolder(View view) {
            super(view);
            this.Name = (TextView) view.findViewById(R.id.textView_list_Name2);
            this.Price = (TextView) view.findViewById(R.id.textView_list_Price2);
            this.incomeDate = (TextView) view.findViewById(R.id.textView_list_IncomeDate);
            this.shelfLife = (TextView) view.findViewById(R.id.textView_list_ShelfLife);
            this.Image_view = (ImageView) view.findViewById(R.id.iv2);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {     //리스트뷰가 처음으로 생성될 때 생명주기
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fresh_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override //각 아이템들에 대한 매칭
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {      //실제 추가될 때에 대한 생명주기
        Glide.with(viewholder.itemView)
                .load(mList.get(position).getImage())
                .into(viewholder.Image_view);
        viewholder.Name.setText(mList.get(position).getName());
        viewholder.Price.setText(mList.get(position).getPrice());
        viewholder.incomeDate.setText(mList.get(position).getincomeDate());
        viewholder.shelfLife.setText(mList.get(position).getshelfLife());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
