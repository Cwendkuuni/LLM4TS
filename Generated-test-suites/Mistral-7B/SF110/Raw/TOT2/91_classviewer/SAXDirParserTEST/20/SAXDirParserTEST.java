import org.junit.Assert;
import org.junit.Test;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String expected = "";
        String actual = parser.getWebData();
        Assert.assertEquals(expected, actual);
    }
}