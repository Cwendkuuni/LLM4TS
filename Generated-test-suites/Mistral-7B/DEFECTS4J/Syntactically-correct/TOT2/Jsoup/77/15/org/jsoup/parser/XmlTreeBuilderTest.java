import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xmlInput = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<root>\n" +
                "<element1 attr1='value1'>content 1</element1>\n" +
                "<element2 attr2='value2'>content 2</element2>\n" +
                "</root>";

        Document xmlDoc = XmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
        Element rootElement = xmlDoc.select("root").first();

        assertEquals("value1", rootElement.child(0).attr("attr1"));
        assertEquals("content 1", rootElement.child(0).text());
        assertEquals("value2", rootElement.child(1).attr("attr2"));
        assertEquals("content 2", rootElement.child(1).text());
    }
}