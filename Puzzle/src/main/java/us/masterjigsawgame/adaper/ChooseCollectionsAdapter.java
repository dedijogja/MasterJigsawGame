
package us.masterjigsawgame.adaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import us.masterjigsawgame.R;
import us.masterjigsawgame.asyncronus.SingleDecryption;
import us.masterjigsawgame.util.AppUtils;


public class ChooseCollectionsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflator;

	private int width = 0;
	private int height = 0;
    private RelativeLayout.LayoutParams params;
	private Context context;
    private String[] semuaPuzzle;
    private Bitmap[] semuaBitmapPuzzle;

	public ChooseCollectionsAdapter(Context context, String[] semuaPuzzle) {
		super();

		this.context = context;
        this.semuaPuzzle = semuaPuzzle;
        semuaBitmapPuzzle = new Bitmap[semuaPuzzle.length];

		mLayoutInflator = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int deviceWidth = AppUtils.getScreenWidth(context);
        int gridCount = 2;
        float spacing = context.getResources().getDimension(
                R.dimen.grid_view_spacing);
        float padding = context.getResources().getDimension(
                R.dimen.grid_view_padding);
		width = (int) ((deviceWidth - (((gridCount - 1) * spacing * 1.0) + (padding * 2 * 1.0))) / gridCount);
		height = width;
		params = new RelativeLayout.LayoutParams(width, height);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.favorite_gridview_item, null);
			ViewHolder viewholder = new ViewHolder();
			viewholder.thumbnail_imageview = view.findViewById(R.id.thumbnail_imageview);
			viewholder.thumbnail_imageview.setLayoutParams(params);
			view.setTag(viewholder);
		}

        final ViewHolder holder = (ViewHolder) view.getTag();
        if(semuaBitmapPuzzle[position]==null) {
            SingleDecryption singleDecryption = new SingleDecryption(context, width, height);
            singleDecryption.setListenerDecrypt(new SingleDecryption.ListenerDecrypt() {
                @Override
                public void onSelesaiDecrypt(Bitmap hasilDeskripsi) {
                    if (hasilDeskripsi != null) {
                        holder.thumbnail_imageview.setImageBitmap(hasilDeskripsi);
                        semuaBitmapPuzzle[position] = hasilDeskripsi;
                    } else {
                        holder.thumbnail_imageview.setImageResource(R.drawable.logo);
                    }
                }
            });
            singleDecryption.execute(semuaPuzzle[position]);
        }else{
            holder.thumbnail_imageview.setImageBitmap(semuaBitmapPuzzle[position]);
        }
		return view;
	}

	@Override
	public int getCount() {
		if (semuaPuzzle != null) {
			return semuaPuzzle.length;
		}
		return 0;
	}

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		ImageView thumbnail_imageview;
	}


}