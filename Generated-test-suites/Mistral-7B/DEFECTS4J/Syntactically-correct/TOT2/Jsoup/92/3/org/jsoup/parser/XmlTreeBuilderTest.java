package org.jsoup.parser; // or the package where XmlTreeBuilder is located

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParseValidXml() {
        String xmlString = "<root><element>Hello</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString));
        Element root = doc.rootElement();
        Element element = root.selectFirst("element");
        TextNode textNode = (TextNode) element.text();
        assertEquals("Hello", textNode.text());
    }

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(""));
        assertTrue(doc.bodyElements().isEmpty());
    }

    @Test
    public void testParseInvalidXml() {
        String xmlString = "<root><element><</script>"; // XSS attack
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        try {
            Document doc = xmlTreeBuilder.parse(new StringReader(xmlString));
            fail("Expected parse exception");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testParseXmlWithComments() {
        String xmlString = "<root><element><!-- This is a comment --></element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlString));
        Element root = doc.rootElement();
        Element element = root.selectFirst("element");
        // Check comment is present
        assertTrue(element.childNodes().size() > 1);
        Comment comment = (Comment) element.child(1);
        assertEquals("This is a comment", comment.data());
    }
}