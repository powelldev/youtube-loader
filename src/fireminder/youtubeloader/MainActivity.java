/**
 * @author Michael Powell
 * @link github.com/powelldev
 * @email powell.r.mike@gmail.com
 */
package fireminder.youtubeloader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import fireminder.youtubeloader.ui.VideoListViewAdapter;
import fireminder.youtubeloader.valueobjects.YoutubeVideo;

public class MainActivity extends ListActivity implements OnScrollListener{

	/** video array for list view adapter */
	YoutubeVideo[] array;

	/** List to store videos parsed from JSON */
	ArrayList<YoutubeVideo> mVideos = new ArrayList<YoutubeVideo>();

	static final String YOUTUBE_URL = "http://gdata.youtube.com/feeds/api/videos?alt=json&max-results=50";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getListView().setOnScrollListener(this);

		/** Query site for videos */
		performYoutubeApiQuery(0, 50);

		/** Setup click event */
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Intent intent = YouTubeStandalonePlayer.createVideoIntent(
						MainActivity.this,
						"AIzaSyA6pTR_lMnCqwKpJXTADsRjwRQhrQOA8T8",
						mVideos.get(position).getKey());
				startActivity(intent);
			}
		});
		
		

	}

	public void performYoutubeApiQuery(int start, int stop){
		String url = "http://gdata.youtube.com/feeds/api/videos?alt=json";
		if(start == 0){
				url = url + "&max-results=50";
		} else {
			url = url + "&start-index=" + start + "&max-results=" + stop;
		}
		StringRequest request = new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							JSONObject result = new JSONObject(response);
							JSONObject feed = result.getJSONObject("feed");
							JSONArray entries = feed.getJSONArray("entry");
							for (int i = 0; i < entries.length(); i++) {
								JSONObject entry = entries.getJSONObject(i);
								String link = entry.getJSONArray("link")
										.getJSONObject(0).getString("href");
								String title = entry.getJSONObject("title")
										.getString("$t");
								String thumbnail = entry
										.getJSONObject("media$group")
										.getJSONArray("media$thumbnail")
										.getJSONObject(2).getString("url");
								int viewCount = Integer.parseInt(entry
										.getJSONObject("yt$statistics")
										.getString("viewCount"));
								mVideos.add(
										new YoutubeVideo(title, 
										link,
										viewCount, 
										thumbnail));
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						new BackgroundLoaderAsyncTask().execute();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}

				});

		Volley.newRequestQueue(getApplicationContext()).add(request);
	}
	/** Load videos into list asynchronously */

	class BackgroundLoaderAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			array = new YoutubeVideo[mVideos.size()];
			for (int i = 0; i < mVideos.size(); i++) {
				array[i] = mVideos.get(i);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(new VideoListViewAdapter(getApplicationContext(),
					R.layout.list_view_video_item, array));
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		if(firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 10) {
			performYoutubeApiQuery(totalItemCount, 50);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		
		
	}
}
