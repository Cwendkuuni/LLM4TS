import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.XmlDeclaration;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXML() {
        String xml = "<>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseXMLWithRootElement() {
        String xml = "<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXMLWithRootElementAndChildren() {
        String xml = "<root><child1><grandChild1></grandChild1></child1></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        Element child1 = (Element) children.get(0);
        assertEquals("child1", child1.nodeName());
        List<Node> grandChildren = child1.childNodes();
        assertEquals(1, grandChildren.size());
        Element grandChild1 = (Element) grandChildren.get(0);
        assertEquals("grandChild1", grandChild1.nodeName());
        assertEquals(0, grandChild1.childNodes().size());
    }

    @Test
    public void testParseXMLWithDoctype() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">\n<html></html>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element root = (Element) nodes.get(0);
        assertEquals("html", root.nodeName());
        List<Node> children = root.childNodes();
        assertEquals(1, children.size());
        DocumentType doctypeNode = (DocumentType) children.get(0);
        assertEquals("html", doctypeNode.nodeName());
    }

    @Test
    public void testParseXMLWithComment() {
        String xml = "<!-- This is a comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(0, nodes.size());
        Comment comment = (Comment) doc.getElementsByTag("comment").first();
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXMLWithEmptyElement() {
        String xml = "<empty/>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(xml, "baseURI");

        List<Node> nodes = doc.childNodes();
        assertEquals(1, nodes.size());
        Element emptyElement = (Element) nodes.get(0);
        assertEquals("empty", emptyElement.nodeName());
        assertEquals(0, emptyElement.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXMLWithInvalidStartTag() {
        String xml = "<invalid>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = null;
        try {
            doc = xmlTreeBuilder.parse(xml, "baseURI");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid tag name: invalid", e.getMessage());
            throw e;
        }
        fail("Expected IllegalArgumentException but got Document instead");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXMLWithInvalidEndTag() {
        String xml = "<root><child></root><invalid>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = null;
        try {
            doc = xmlTreeBuilder.parse(xml, "baseURI");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid tag name: invalid", e.getMessage());
            throw e;
        }
        Element root = (Element) doc.child(0);
        Element child = (Element) root.child(0);
        List<Node> nodes = child.childNodes();
        for (Node node : nodes) {
            if (node instanceof Element && node.nodeName().equals("invalid")) {
                throw new IllegalArgumentException("Invalid tag name: invalid");
            }
        }
        fail("Expected IllegalArgumentException but got no error");
    }
}