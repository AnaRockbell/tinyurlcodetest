package tinyURL;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;


public class URLMaker {
	
	@NotNull
	@URL
	private String url;

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
}