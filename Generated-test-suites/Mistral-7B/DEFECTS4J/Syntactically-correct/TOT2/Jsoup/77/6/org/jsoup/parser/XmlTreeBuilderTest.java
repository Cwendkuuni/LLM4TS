import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<xml><tag attr='value'/></xml>";
        Document document = XmlTreeBuilder.parse(new StringReader(xmlString), "baseUrl");
        assertNotNull(document);
    }
}