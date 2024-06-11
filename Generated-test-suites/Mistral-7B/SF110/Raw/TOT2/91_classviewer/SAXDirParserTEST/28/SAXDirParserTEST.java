import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
        // Add more test cases as needed
    }
}