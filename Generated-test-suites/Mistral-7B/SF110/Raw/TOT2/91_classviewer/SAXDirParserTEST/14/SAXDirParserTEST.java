import org.junit.Test;
import static org.junit.Assert.*;

public class SAXDirParserTest {
    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
}