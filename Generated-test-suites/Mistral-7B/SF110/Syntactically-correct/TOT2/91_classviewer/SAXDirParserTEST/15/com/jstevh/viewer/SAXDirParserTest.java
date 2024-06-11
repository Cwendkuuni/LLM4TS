import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SAXDirParserTest {
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
}