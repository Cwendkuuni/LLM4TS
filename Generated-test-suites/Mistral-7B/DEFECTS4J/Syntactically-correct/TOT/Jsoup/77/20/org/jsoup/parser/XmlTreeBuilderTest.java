import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTree;
import org.jsoup.selenium.UnsupportedCommandException;

import java.io.StringReader;
import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("", "");
        assertNotNull(document);
        assertFalse(document.body().childNodes().isEmpty());
    }

    @Test
    public void testParseValidXml() {
        String xml = "<root><element1 attr1=\"value1\">Content 1</element1><element2 attr2=\"value2\"><subElement/></element2></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        Element root = document.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(2, children.size());
        Element element1 = (Element) children.get(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.attr("attr1"));
        assertEquals("Content 1", element1.text());
        Element element2 = (Element) children.get(1);
        assertEquals("element2", element2.tagName());
        assertEquals("value2", element2.attr("attr2"));
        List<Node> subElementChildren = element2.children();
        assertEquals(1, subElementChildren.size());
        assertNull(subElementChildren.get(0).text());
    }

    @Test(expected = UnsupportedCommandException.class)
    public void testParseInvalidXmlWithUnknownHtmlTag() {
        String xml = "<html><body><p>Invalid XML</p></body></html>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(xml, "");
        fail("XmlTreeBuilder should not support HTML tags when parsing XML.");
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<root><!-- This is a comment --><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        List<Node> children = document.rootElement().children();
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        List<Node> commentChildren = element.childNodes();
        assertEquals(1, commentChildren.size());
        Node comment = commentChildren.get(0);
        assertTrue(comment instanceof Comment);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xml = "<root><![CDATA[ <html><head><title>Test</title></head><body><p>Content in CData section</p></body></html> ]]></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        Element root = document.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertEquals("html", element.tagName());
        List<Node> htmlChildren = element.children();
        assertEquals(2, htmlChildren.size());
        Element head = (Element) htmlChildren.get(0);
        assertEquals("head", head.tagName());
        List<Node> headChildren = head.children();
        assertEquals(1, headChildren.size());
        Element title = (Element) headChildren.get(0);
        assertEquals("title", title.tagName());
        List<Node> titleChildren = title.children();
        assertEquals(1, titleChildren.size());
        assertEquals("Test", titleChildren.get(0).text());
        Element body = (Element) htmlChildren.get(1);
        assertEquals("body", body.tagName());
        List<Node> bodyChildren = body.children();
        assertEquals(1, bodyChildren.size());
        assertEquals("Content in CData section", bodyChildren.get(0).text());
    }

    @Test
    public void testParseXmlWithDoctypeDeclaration() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        assertEquals(1, document.childNodes().size());
        Node documentTypeNode = document.childNodes().get(0);
        assertTrue(documentTypeNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) documentTypeNode;
        assertEquals("html", documentType.nodeName());
        assertEquals("about:legacy-compat", documentType.systemId());
    }

    @Test
    public void testParseXmlWithEmptyCDataSection() {
        String xml = "<root><![CDATA[]]></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        Element root = document.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        assertEquals(1, children.size());
        Node node = children.get(0);
        assertTrue(node instanceof DocumentType);
        DocumentType documentType = (DocumentType) node;
        assertEquals("", documentType.text());
    }

    @Test
    public void testParseXmlWithInvalidDoctypeDeclaration() {
        String xml = "<!DOCTYPE html SYSTEM \"about:legacy-compat\" [ <!ELEMENT html EMPTY> ]>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        assertEquals(1, document.childNodes().size());
        Node documentTypeNode = document.childNodes().get(0);
        assertTrue(documentTypeNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) documentTypeNode;
        assertEquals("html", documentType.nodeName());
        assertEquals("about:legacy-compat", documentType.systemId());
        assertNull(documentType.internalSubset());
    }

    @Test
    public void testParseXmlWithPublicIdAndSystemId() {
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(xml, "");
        assertEquals(1, document.childNodes().size());
        Node documentTypeNode = document.childNodes().get(0);
        assertTrue(documentTypeNode instanceof DocumentType);
        DocumentType documentType = (DocumentType) documentTypeNode;
        assertEquals("html", documentType.nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", documentType.publicId());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.systemId());
        assertNull(documentType.internalSubset());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseXmlWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "");
        fail("XmlTreeBuilder should throw an exception when parsing empty input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "");
        fail("XmlTreeBuilder should throw an exception when parsing null input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", null);
        fail("XmlTreeBuilder should throw an exception when parsing null baseUri.");
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), XmlTreeBuilder.ParseSettings.preserveCase);
        Document document = new Document();
        document.appendChild(nodes.get(0));
        Element root = document.rootElement();
        assertEquals("root", root.tagName());
        List<Node> children = root.children();
        Element element = (Element) children.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals("Content", element.text());
    }

    @Test
    public void testParseFragmentWithInvalidXml() {
        String xmlFragment = "<!DOCTYPE html SYSTEM \"about:legacy-compat\"><html><body><p>Invalid XML</p></body></html>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        try {
            xmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), XmlTreeBuilder.ParseSettings.preserveCase);
            fail("XmlTreeBuilder should throw an exception when parsing invalid XML in a fragment.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}