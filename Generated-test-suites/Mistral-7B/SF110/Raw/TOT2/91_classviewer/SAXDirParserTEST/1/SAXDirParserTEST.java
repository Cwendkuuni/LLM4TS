import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser saxDirParser = new SAXDirParser();
        assertEquals("", saxDirParser.getWebData());
    }

    // Edge case: Test when webData is set to a non-empty string in the constructor
    @Test
    public void testGetWebData_initWithData() {
        SAXDirParser saxDirParser = new SAXDirParser();
        saxDirParser.webData = "testData";
        assertEquals("testData", saxDirParser.getWebData());
    }
}