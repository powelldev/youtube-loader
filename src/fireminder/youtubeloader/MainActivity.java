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

public class MainActivity extends ListActivity {
	YoutubeVideo[] array;
	ArrayList<YoutubeVideo> mVideos = new ArrayList<YoutubeVideo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Toast.makeText(getApplicationContext(), mVideos.get(position).getLink(), Toast.LENGTH_LONG).show();
				Intent intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, "AIzaSyA6pTR_lMnCqwKpJXTADsRjwRQhrQOA8T8", mVideos.get(position).getKey());
				Log.e("TAG", mVideos.get(position).getKey());
				startActivity(intent);
			}
		});

		StringRequest request = new StringRequest( Method.GET,
				"http://gdata.youtube.com/feeds/api/videos?alt=json&max-results=50",
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
								String link =  entry.getJSONArray("link").getJSONObject(0).getString("href");
								String title =  entry.getJSONObject("title").getString("$t");
								String thumbnail =  entry.getJSONObject("media$group").getJSONArray("media$thumbnail").getJSONObject(2).getString("url");
								int viewCount = Integer.parseInt(entry.getJSONObject("yt$statistics").getString("viewCount"));
								mVideos.add(new YoutubeVideo(title, link, viewCount, thumbnail));
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
