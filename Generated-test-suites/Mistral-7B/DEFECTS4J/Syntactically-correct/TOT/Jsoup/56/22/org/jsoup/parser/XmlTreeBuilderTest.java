import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        String input = "<xml/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        List<Node> nodeList = document.childNodes();
        assertEquals("Expected empty document", 1, nodeList.size());
        Element rootElement = (Element) nodeList.get(0);
        assertEquals("Expected root element to be xml", "xml", rootElement.nodeName());
    }

    @Test
    public void testParseSimpleXml() {
        String input = "<root><child>Hello World</child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        List<Node> nodeList = document.childNodes();
        assertEquals("Expected single root element", 1, nodeList.size());
        Element rootElement = (Element) nodeList.get(0);
        assertEquals("Expected root element to be root", "root", rootElement.nodeName());
        List<Node> childNodes = rootElement.childNodes();
        assertEquals("Expected single child element", 1, childNodes.size());
        Element childElement = (Element) childNodes.get(0);
        assertEquals("Expected child element to be child", "child", childElement.nodeName());
        TextNode textNode = (TextNode) childElement.childNodes().get(0);
        assertEquals("Expected text node to contain Hello World", "Hello World", textNode.text());
    }

    @Test
    public void testParseSelfClosingTag() {
        String input = "<img src=\"image.jpg\" />";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        List<Node> nodeList = document.childNodes();
        assertEquals("Expected single img element", 1, nodeList.size());
        Element imgElement = (Element) nodeList.get(0);
        assertEquals("Expected img element to be img", "img", imgElement.nodeName());
        assertTrue("Expected img element to be self-closing", imgElement.isSelfClosing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidXml() {
        String input = "<html><body><foo>"; // missing end tag for foo
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(input, "http://example.com");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "http://example.com");
    }
}