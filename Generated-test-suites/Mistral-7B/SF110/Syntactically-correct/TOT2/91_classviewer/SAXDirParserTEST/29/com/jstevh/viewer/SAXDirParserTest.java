import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertEquals("", saxDirParser.getWebData());
    }
}