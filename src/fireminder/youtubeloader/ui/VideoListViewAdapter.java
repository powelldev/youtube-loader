package fireminder.youtubeloader.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.Volley;

import fireminder.youtubeloader.R;
import fireminder.youtubeloader.valueobjects.YoutubeVideo;

public class VideoListViewAdapter extends BaseAdapter{

	Context mContext;
	YoutubeVideo[] mObjects;
	ImageLoader mImageLoader;

	public VideoListViewAdapter(Context context, int resource, YoutubeVideo[] objects) {

		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapImageCache(10));
		mContext = context;
		mObjects = objects;

	}
	
	public void setArray(YoutubeVideo[] moreObjects){
		mObjects = moreObjects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (null == rowView) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_view_video_item, parent,
					false);
		}

		TextView videoTitleTv = (TextView) rowView
				.findViewById(R.id.list_view_video_title);
		TextView videoViewCountTv = (TextView) rowView
				.findViewById(R.id.list_view_video_count);
		final ImageView videoThumbnailIv = (ImageView) rowView
				.findViewById(R.id.list_view_video_thumbnail);

		videoTitleTv.setText(mObjects[position].getTitle());
		videoViewCountTv.setText(NumberFormat.getNumberInstance(Locale.US).format(mObjects[position].getViewCount()));
		
		/** Load images asynchronously for smoother animation */
		mImageLoader.get(mObjects[position].getThumbnailLink(), new ImageLoader.ImageListener() {
			
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onResponse(ImageContainer response, boolean arg1) {
				videoThumbnailIv.setImageBitmap(response.getBitmap());
			}
		});
		return rowView;
	}
	
	public class BitmapImageCache extends LruCache<String, Bitmap>implements ImageCache{

		public BitmapImageCache(int maxSize) {
			super(maxSize);
		}

		@Override
		public Bitmap getBitmap(String arg0) {
			return get(arg0);
		}

		@Override
		public void putBitmap(String arg0, Bitmap arg1) {
			put(arg0, arg1);
		}
		
	}

	@Override
	public int getCount() {
		return mObjects.length;
	}

	@Override
	public Object getItem(int position) {
		return mObjects[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

}
