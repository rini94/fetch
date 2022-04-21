public class fetch {

	public static void main (String[] args) throws Exception {

		try {

			int argsLen = args.length;
			boolean metadata = false;
			int count = 0;

			//Show the metadata of all urls if first argument is '--metadata'
			if (argsLen > 0 && args[0].equals ("--metadata")) {

				metadata = true;
				count = 1;
			}
			
			if (argsLen < 1 || (metadata && argsLen < 2)) {
				throw new Exception ("No urls passed...");
			}
			if (metadata) {
				System.out.println ("\n--- METADATA ---");
			}
			else {
				System.out.println ("\n--- DOWNLOADING WEB PAGES ---");
			}

			//List of urls to be downloaded can be sent as space separated arguments
			while (count < argsLen) {

				String url = args[count];
				WebPage webPage = new WebPage (url);
				if (metadata) {
					webPage.showMetadata ();
				}
				else {
					webPage.downloadWebPage ();
				}
				count++;
			}
			System.out.println ("\n--- PROCESS COMPLETE ---");
		}
		catch (Exception e) {
			System.out.println ("Exception: " + e);
		}
	}
}