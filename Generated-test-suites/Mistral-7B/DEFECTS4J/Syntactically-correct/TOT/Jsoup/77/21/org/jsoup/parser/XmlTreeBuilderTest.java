import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXmlWithValidInput() {
        String xmlInput = "<root><child element=\"value\"/><anotherChild/></root>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        Element root = (Element) doc.childNodes().get(0);
        Element child = (Element) root.childNodes().get(0);
        Element anotherChild = (Element) root.childNodes().get(1);

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("value", child.attributes().get("element").getValue());
        assertNull(child.attributes().get("anotherAttribute"));
        assertEquals("anotherChild", anotherChild.nodeName());
        assertTrue(anotherChild.attributes().isEmpty());
    }

    @Test
    public void testParseXmlWithEmptyInput() {
        String xmlInput = "";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        assertNull(doc);
    }

    @Test(expected = ParseError.class)
    public void testParseXmlWithInvalidInput() {
        String xmlInput = "<root<child/>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        fail("Expected ParseError, but didn't get one.");
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<root><child element=\"value\"/></root>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        List<Node> nodes = parser.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), settings);

        Element root = (Element) nodes.get(0);
        Element child = (Element) root.childNodes().get(0);

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("value", child.attributes().get("element").getValue());
        assertTrue(child.attributes().isEmpty());
    }

    @Test
    public void testParseXmlWithDoctypeDeclaration() {
        String xmlInput = "<!DOCTYPE root [ <!ATTLIST child element CDATA #REQUIRED> ]><root><child element=\"value\"/></root>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        Element root = (Element) doc.childNodes().get(0);
        Element child = (Element) root.childNodes().get(0);

        assertEquals("root", root.nodeName());
        assertEquals("child", child.nodeName());
        assertEquals("value", child.attributes().get("element").getValue());
        assertTrue(child.attributes().isEmpty());

        DocumentType doctype = (DocumentType) root.childNodes().get(1);
        assertEquals("root", doctype.nodeName());
        assertEquals("[CDATA[ <!ATTLIST child element CDATA #REQUIRED> ]]", doctype.getSystemIdentifier());
        assertEquals("root", doctype.getPublicIdentifier());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xmlInput = "<root><element element=\"value\"/></root>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        Element root = (Element) doc.childNodes().get(0);
        Element element = (Element) root.childNodes().get(0);

        assertEquals("root", root.nodeName());
        assertEquals("element", element.nodeName());
        assertEquals("value", element.attributes().get("element").getValue());
        assertTrue(element.attributes().isEmpty());

        assertTrue(element.childNodes().isEmpty());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xmlInput = "<root><img src=\"image.jpg\" /> <child element=\"value\"/></root>";
        String baseUri = "http://example.com/";
        ParseSettings settings = XmlTreeBuilder.defaultSettings();

        XmlTreeBuilder parser = new XmlTreeBuilder();
        Document doc = parser.parse(new StringReader(xmlInput), baseUri);

        Element root = (Element) doc.childNodes().get(0);
        Element img = (Element) root.childNodes().get(0);
        Element child = (Element) root.childNodes().get(1);

        assertEquals("root", root.nodeName());
        assertEquals("img", img.nodeName());
        assertTrue(img.nodeName().equals("img") && img.isSelfClosing());
        assertTrue(img.attributes().isEmpty());
        assertEquals("child", child.nodeName());
        assertEquals("value", child.attributes().get("element").getValue());
        assertTrue(child.attributes().isEmpty());
    }
}