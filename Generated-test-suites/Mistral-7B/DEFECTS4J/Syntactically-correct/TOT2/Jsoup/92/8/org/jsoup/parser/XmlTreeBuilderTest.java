import org.junit.Test;
import org.jsoup.Jsoup;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element1>Hello World</element1></root>";
        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
        assertNotNull(xmlDoc);
    }
}