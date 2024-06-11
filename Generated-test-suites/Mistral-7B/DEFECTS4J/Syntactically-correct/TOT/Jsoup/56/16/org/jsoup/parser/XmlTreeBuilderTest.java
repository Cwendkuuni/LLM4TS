import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.xmlparser.XmlParser;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXML() {
        String input = "<>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseSimpleXML() {
        String input = "<root><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        TextNode textNode = (TextNode) child.childNodes().get(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParseXMLWithAttributes() {
        String input = "<root attr1='value1' attr2='value2'><child attr3='value3'></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        Map<String, String> attributes = root.attributes();
        assertEquals(2, attributes.size());
        assertEquals("value1", attributes.get("attr1"));
        assertEquals("value2", attributes.get("attr2"));
        Map<String, String> childAttributes = child.attributes();
        assertEquals(1, childAttributes.size());
        assertEquals("value3", childAttributes.get("attr3"));
    }

    @Test
    public void testParseXMLWithComments() {
        String input = "<root><!-- This is a comment --><child></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals(3, nodes.size());
        assertEquals(1, root.childNodes().size());
        Element comment = (Element) root.childNodes().get(0);
        assertEquals("!-- This is a comment --", comment.text());
        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
    }

    @Test
    public void testParseXMLWithEmptyElement() {
        String input = "<root><empty/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element empty = (Element) nodes.get(1);
        assertEquals(2, nodes.size());
        assertEquals("root", root.nodeName());
        assertEquals("empty", empty.nodeName());
        assertEquals(0, empty.childNodes().size());
    }

    @Test
    public void testParseXMLWithSelfClosingTag() {
        String input = "<root><img src='image.jpg'/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "baseURI");
        List<Node> nodes = doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element img = (Element) nodes.get(1);
        assertEquals(2, nodes.size());
        assertEquals("root", root.nodeName());
        assertEquals("img", img.nodeName());
        assertEquals("image.jpg", img.attributes().get("src"));
        assertEquals(0, img.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXMLWithInvalidInput() {
        String input = "<root><invalid>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(input, "baseURI");
    }
}