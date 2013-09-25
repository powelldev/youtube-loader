package fireminder.youtubeloader.valueobjects;

public class YoutubeVideo {

	private String title;
	private String link;
	private int viewCount;
	private String thumbnailLink;
	
	public YoutubeVideo(String title, String link, int viewCount, String thumbnailLink){
		this.title = title;
		this.viewCount = viewCount;
		this.thumbnailLink = thumbnailLink;
		this.link = link;
	}

	public String getThumbnailLink() {
		return thumbnailLink;
	}
	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public String getKey(){
		return link.subSequence(link.indexOf("?")+3, link.indexOf("&")).toString();
	}
	
	
	
}
