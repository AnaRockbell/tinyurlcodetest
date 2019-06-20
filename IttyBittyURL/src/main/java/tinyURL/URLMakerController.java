package tinyURL;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class URLMakerController {
	private static final String VALID_RANDOM_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static Map<String,URLCountPair> ittyBittyURLS = new HashMap<String, URLCountPair>();
//	private static Map<String,Integer> ittyBittyURLHitCount = new HashMap<String, Integer>();

    @GetMapping("/urlmaker")
    public String urlmakerForm(Model model) {
        model.addAttribute("urlmaker", new URLMaker());
        model.addAttribute("ittyBittyURLS", ittyBittyURLS);
        return "urlmaker";
    }

    @PostMapping("/urlmaker")
    public String urlmakerSubmit(@ModelAttribute URLMaker uRLMaker) {
    	if(uRLMaker.getURL() != null && uRLMaker.getURL().length() != 0)
    	{
	    	String key = randomKeyGenerator();
	    	while(ittyBittyURLS.containsKey(key))
	    	{
	    		key = randomKeyGenerator();
	    	}
	    	ittyBittyURLS.put(key, new URLCountPair(uRLMaker.getURL(), 0));
    	}

        return "redirect:/urlmaker";
    }
    
    private String randomKeyGenerator()
    {
    	StringBuilder key = new StringBuilder();
    	int length = (int) (Math.random()*6.0 + 4);
    	for(int i = 0; i<length; i++) {
    		int character = (int)(Math.random()*VALID_RANDOM_CHARACTERS.length());
    		key.append(VALID_RANDOM_CHARACTERS.charAt(character));
    	}
    	return key.toString();
    }
    
	@RequestMapping(value = "/t/{key}", method = RequestMethod.GET) 
	public RedirectView redirect(@PathVariable String key, HttpServletResponse response) throws IOException
	{
		String url = ittyBittyURLS.get(key).getURL();
		RedirectView redirectView = new RedirectView();
		if(url != null)
		{
        	redirectView.setUrl(url);
        	ittyBittyURLS.get(key).setCount(ittyBittyURLS.get(key).getCount() + 1);
		}
		else
		{
			redirectView.setUrl("localhost:8080");
		}
    	return redirectView;
	}
	
	private class URLCountPair
	{
	    private String url;
	    private int count;
	    
	    public URLCountPair(String url, int count)
	    {
	        this.url = url;
	        this.count = count;
	    }
	    
	    public String getURL() {
	        return url;
	    }

	    public void setURL(String url) {
	        this.url = url;
	    }

	    public int getCount() {
	        return count;
	    }

	    public void setCount(int count) {
	        this.count = count;
	    }
	}
}