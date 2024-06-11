import org.junit.Test;
import static org.junit.Assert.*;
import com.jstevh.viewer.SAXDirParser;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }
}