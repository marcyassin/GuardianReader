package yassin.marc.guardianreader;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX tag handler. The Class contains a list of RssItems which is being filled while the parser is working
 * @author ITCuties
 */
public class RssParseHandler extends DefaultHandler {

    // List of items parsed
    private ArrayList<PostData> rssItems;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing
    private PostData currentItem;
    // We have two indicators which are used to differentiate whether a tag title or link is being processed by the parser
    // Parsing title indicator
    private boolean isParsingTitle;
    // Parsing link indicator
    private boolean isParsingLink;

    private boolean isParsingPubDate;

    private boolean isParsingThumbNail;

    public RssParseHandler() {
        rssItems = new ArrayList();
    }
    // We have an access method which returns a list of items that are read from the RSS feed. This method will be called when parsing is done.
    public ArrayList<PostData> getItems() {
        return rssItems;
    }
    // The StartElement method creates an empty RssItem object when an item start tag is being processed. When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName)) {
            currentItem = new PostData();
        }
        else if ("title".equals(qName)) {
            isParsingTitle = true;
        }
        else if ("link".equals(qName)) {
            isParsingLink = true;
        }
        else if ("pubDate".equals(qName)) {
            isParsingPubDate = true;
        }
        else if (localName.equalsIgnoreCase("thumbnail")) {
            currentItem.setPostThumbUrl(attributes.getValue("url"));
        }
    }
    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed. It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;
        }
        else if ("title".equals(qName)) {
            isParsingTitle = false;
        }
        else if ("link".equals(qName)) {
            isParsingLink = false;
        }
        else if ("pubDate".equals(qName)) {
            isParsingPubDate = false;
        }
    }
    // Characters method fills current RssItem object with data when title and link tag content is being processed
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isParsingTitle) {
            if (currentItem != null) {
                if (currentItem.getPostTitle() == null) {
                    currentItem.setPostTitle(new String(ch, start, length));
                } else {
                    currentItem.setPostTitle(currentItem.getPostTitle() + new String(ch, start, length));
                }
            }
        }
        else if (isParsingLink) {
            if (currentItem != null) {
                currentItem.setPostLink(new String(ch, start, length));
                isParsingLink = false;
            }
        }
        else if (isParsingThumbNail) {
            if (currentItem != null){
                currentItem.setPostThumbUrl(new String(ch, start, length));
            }
        }
        else if (isParsingPubDate){
            if (currentItem != null) {
                currentItem.setPostDate(new String(ch, start, length));
            }
        }
    }
}

