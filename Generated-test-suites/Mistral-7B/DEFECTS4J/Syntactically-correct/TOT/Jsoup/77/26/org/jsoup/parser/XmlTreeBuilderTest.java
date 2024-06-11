import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * XmlTreeBuilderTest class for testing the XmlTreeBuilder class.
 */
public class XmlTreeBuilderTest {

    @Test
    public void testParseXMLWithValidInput() {
        String xmlInput = "<root><element attr=\"value\">Hello World!</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(xmlInput), "http://example.com");

        assertNotNull(document);
        assertEquals("root", document.tagName());

        List<Node> children = document.childNodes();
        assertEquals(1, children.size());
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).getValue());
        assertEquals("Hello World!", element.childNodes().get(0).text());
    }

    @Test
    public void testParseXMLWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertEquals("", document.html());
    }

    @Test(expected = RuntimeException.class)
    public void testParseXMLWithInvalidInput() {
        String invalidXmlInput = "<root><element attr=\"value>Hello World!";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader(invalidXmlInput), "http://example.com");
    }

    @Test
    public void testParseXMLFragment() {
        String xmlFragment = "<element attr=\"value\">Hello World!</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute(0).getValue());
        assertEquals("Hello World!", element.childNodes().get(0).text());
    }

    @Test(expected = RuntimeException.class)
    public void testParseXMLFragmentWithInvalidInput() {
        String invalidXmlFragment = "<root><element attr=\"value>Hello World!";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(invalidXmlFragment, "http://example.com", ParseErrorList.noTracking(), XmlTreeBuilder.ParseSettings.preserveCase);
    }
}