package bih.nic.in.policesoft.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import bih.nic.in.policesoft.R;
import bih.nic.in.policesoft.entity.SliderModel;


public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private SliderModel model;
    public ImageSliderAdapter(Context context, SliderModel model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        viewHolder.textViewDescription.setText("This is slider item " + position);

        switch (position) {
            case 0:
                String IMGg= model.getSlider1();
               // Picasso.get().load(IMGg).into(viewHolder.imageViewBackground);
                Glide.with(context)
                        .load(IMGg)
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                String IMGg2= model.getSlider2();
               // Picasso.get().load(IMGg2).into(viewHolder.imageViewBackground);
                Glide.with(context)
                        .load(IMGg2)
                        .into(viewHolder.imageViewBackground);

                break;
            case 2:
                String IMGg3= model.getSlider3();
                //Picasso.get().load(IMGg3).into(viewHolder.imageViewBackground);
                Glide.with(context)
                        .load(IMGg3)
                        .into(viewHolder.imageViewBackground);

                break;
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 3;
    }
    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            textViewDescription.setVisibility(View.GONE);
            this.itemView = itemView;
        }
    }
}