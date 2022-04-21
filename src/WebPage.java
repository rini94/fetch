import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class WebPage {

	private final String urlStr;
	private URL url;

	public WebPage (String urlStr) throws MalformedURLException {

		if (!urlStr.startsWith ("http")) {
			urlStr = "https://" + urlStr;
		}
		this.urlStr = urlStr;
		try {
			this.url = new URL (urlStr);
		}
		catch (MalformedURLException urlException) {
			System.out.println ("Improper URL entered: " + this.urlStr);
		}
	}

	//Get the webpage, and reset the url of all assets for offline viewing - only images for now
	public Document getWebPage () throws Exception {
        
		Document document = null;
		try {
        	document = Jsoup.connect (this.urlStr).get ();
		}
		catch (UnknownHostException uhe) {
			System.out.println ("Unknown web page URL...");
		}
		return document;
    }

	//Stores the HTML page received from url param and stores it in current directory
	public boolean downloadWebPage () throws Exception {

		System.out.println ("\nDownloading webpage: " + this.urlStr);
		Document document = getWebPage ();
		if (document != null) {
		
			downloadAssets (document);
			//Downloading html
			try (BufferedWriter writer = new BufferedWriter (new FileWriter (new File (this.url.getHost () + ".html")))) {
				
				writer.write (document.html ());
				System.out.println ("Download complete.\n---");
				return true;
			}
		}
		System.out.println ("\n---");
		return false;
	}

	public void downloadAssets (Document document) throws Exception {

		System.out.println ("Downloading assets...\n");
		//Create directory for assets
		new File (this.url.getHost ()).mkdirs ();

		Elements assets = document.select ("img");
        for (Element asset : assets) {
            
            String assetUrl = asset.attr ("abs:src");
			String assetName = assetUrl.substring (assetUrl.lastIndexOf ("/") + 1);
			if (assetName.trim ().equals ("")) { //For icons and images which are included through class
				continue;
			}
			asset.attr ("src", this.url.getHost () + "/" + assetName);
			asset.attr ("srcset", "");
            downloadAsset (assetUrl, assetName);
        }
		System.out.println ("Assets download complete...\n");
	}
    
	//Download each asset in the webpage - only images for now
    public void downloadAsset (String assetUrl, String assetName) throws MalformedURLException,Exception {
        
        try {
            
			URL urlAsset = new URL (assetUrl);
            byte[] buffer = new byte[4096];
            int n = -1;

            try (OutputStream os = new FileOutputStream (this.url.getHost() + "/" + assetName);
				InputStream in = urlAsset.openStream ();) {
            
            	while ((n = in.read(buffer)) != -1 ) {
                	os.write (buffer, 0, n);
            	}
			}
        }
		catch (MalformedURLException mue) {
			//Skipping the assets that don't have a proper url
		}
		catch (Exception e) {
            System.out.println ("Exception while downloading asset..." + e);
        }
    }

	//Displays the meta data of the HTML page received from url param
	public void showMetadata () throws IOException {

		try {

			Document document = getWebPage (); //Only get the webpage to get count
			if (document == null) {
				return;
			}
			Elements images = document.getElementsByTag ("img");
			Elements links = document.getElementsByAttribute ("href");

			System.out.println ("\n---\nURL entered: " + this.urlStr);
			boolean fileExists = true;
			Path file = Paths.get (this.url.getHost () + ".html");
			if (Files.notExists (file)) {
				fileExists = downloadWebPage (); //Download web page along with assets
			}
			if (!fileExists) {
				
				System.out.println ("Couldn't download the webpage: " + this.urlStr);
				return;
			}
			BasicFileAttributes attr = Files.readAttributes (file, BasicFileAttributes.class);
	        FileTime fileTime = attr.creationTime ();
			SimpleDateFormat sdf = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");

			System.out.println ("\nsite: " + this.url.getHost ()
					+ "\nnum_links: " + links.size ()
					+ "\nimages: " + images.size ()
					+ "\nlast_fetch: " + sdf.format (fileTime.toMillis ()));
		}
		catch (Exception e) {
			System.out.println ("Exception while getting metadata of the webpage: " + this.urlStr + ", Exception: " + e);
		}
	}
}
