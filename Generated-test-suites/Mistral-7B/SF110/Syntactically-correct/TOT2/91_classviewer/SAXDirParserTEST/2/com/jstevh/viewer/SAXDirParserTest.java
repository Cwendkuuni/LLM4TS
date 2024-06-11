import org.junit.Test;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testGetWebDataAfterParsingXML() throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser myParser = saxFactory.newSAXParser();
        XMLReader myReader = myParser.getXMLReader();
        myReader.setContentHandler(new SAXDirParser());
        myReader.parse("packagedirectory.xml");
        assertEquals("Expected web data", parser.getWebData());
    }
}