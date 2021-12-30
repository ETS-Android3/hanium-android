package knormal.justcart.JustCart_ver4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.JustCart_ver4.R;

public class EventAdapter extends PagerAdapter {

    private int[] images = {R.drawable.event1, R.drawable.event2, R.drawable.event3};
    private int[] image1 = {R.drawable.event1};
    private int[] image2 = {R.drawable.event2};
    private int[] image3 = {R.drawable.event3};
    private LayoutInflater inflater;
    private Context context;

    public EventAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.iv);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        imageView.setImageResource(images[position]);
        textView.setText((position + 1) + "번째 이벤트");
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();
    }

}
