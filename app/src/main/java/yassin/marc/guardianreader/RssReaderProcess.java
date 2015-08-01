package yassin.marc.guardianreader;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Marc on 7/22/15.
 */
public class RssReaderProcess {
    private String rssUrl;

    public RssReaderProcess(String rssUrl) { // constructor for the url
        this.rssUrl = rssUrl;
    }

    // Get Rss feed
    public ArrayList<PostData> getItems() throws Exception {
        // At first we need to get an SAX Parser Factory object
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // Using factory we create a new SAX Parser instance
        SAXParser saxParser = factory.newSAXParser();
        // We need the SAX parser handler object
        RssParseHandler handler = new RssParseHandler();
        // We call the method parsing our RSS Feed
        saxParser.parse(rssUrl, handler);
        // The result of the parsing process is being stored in the handler object
        return handler.getItems();
    }

}
