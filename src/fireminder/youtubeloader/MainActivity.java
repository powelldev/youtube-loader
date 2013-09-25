package fireminder.youtubeloader;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import fireminder.youtubeloader.ui.VideoListViewAdapter;
import fireminder.youtubeloader.valueobjects.YoutubeVideo;

public class MainActivity extends ListActivity {
	YoutubeVideo[] array;
	ArrayList<YoutubeVideo> mVideos = new ArrayList<YoutubeVideo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StringRequest request = new StringRequest( Method.GET,
				"http://gdata.youtube.com/feeds/api/videos?alt=json",
				 new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.e("TAG", response.toString());
						
						try {
							JSONObject result = new JSONObject(response);
							JSONObject feed = result.getJSONObject("feed");
							JSONArray entries = feed.getJSONArray("entry");
							for(int i = 0; i < entries.length(); i++){
								JSONObject entry = entries.getJSONObject(i);
								Log.e("Link", entry.getJSONArray("link").getJSONObject(0).getString("href"));
								Log.e("Title", entry.getJSONObject("title").getString("$t"));
								Log.e("thumbnail", entry.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(1).getString("url"));
								Log.e("viewcount", entry.getJSONObject("yt$statistics").getString("viewCount"));
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

	public class BackgroundLoaderAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {

			mVideos.add(new YoutubeVideo("title", "link", 10, "link"));
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
}
