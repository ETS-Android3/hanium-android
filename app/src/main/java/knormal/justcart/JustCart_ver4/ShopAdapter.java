package knormal.justcart.JustCart_ver4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JustCart_ver4.R;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public ShopAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView productName;
        protected TextView productPrice;
        protected TextView classNum;

        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.tv_productName);
            this.productPrice = (TextView) view.findViewById(R.id.tv_productPrice);
            this.classNum = (TextView) view.findViewById(R.id.tv_classNum);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {         //리스트뷰가 처음으로 생성될 때 생명주기
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list2, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override //각 아이템들에 대한 매칭
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {          //실제 추가될 때에 대한 생명주기
        viewholder.productName.setText(mList.get(position).getproductName());
        viewholder.productPrice.setText(mList.get(position).getproductPrice());
        viewholder.classNum.setText(mList.get(position).getclassNum());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
