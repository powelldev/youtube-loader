package fireminder.youtubeloader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fireminder.youtubeloader.R;
import fireminder.youtubeloader.valueobjects.YoutubeVideo;

public class VideoListViewAdapter extends ArrayAdapter<YoutubeVideo> {

	Bitmap mThumbnailBitmap;
	Context mContext;
	YoutubeVideo[] mObjects;

	public VideoListViewAdapter(Context context, int resource, YoutubeVideo[] objects) {
		super(context, resource, objects);

		mContext = context;
		mObjects = objects;

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
		ImageView videoThumbnailIv = (ImageView) rowView
				.findViewById(R.id.list_view_video_thumbnail);

		videoTitleTv.setText(mObjects[position].getTitle());
		videoViewCountTv.setText("" + mObjects[position].getViewCount());
		
		return rowView;
	}

}
