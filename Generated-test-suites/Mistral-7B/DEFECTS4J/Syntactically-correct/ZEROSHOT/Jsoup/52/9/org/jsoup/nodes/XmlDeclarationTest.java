package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for XmlDeclaration.java
 * @author [YourName]
 */
public class XmlDeclarationTest {

    /**
     * Test constructor for XmlDeclaration class
     */
    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xmlns", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xmlns", xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xmlns:xsi", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xmlns:xsi", xmlDeclaration.name());
        assertNull(xmlDeclaration.getWholeDeclaration());
    }

    /**
     * Test getWholeDeclaration method
     */
    @Test
    public void testGetWholeDeclaration() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<xml />", xmlDeclaration.toString());
        assertEquals("<xml />", xmlDeclaration.outerHtml());
        assertEquals("<xml />", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", null, true);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.getWholeDeclaration());

        Attributes attrs = new Attributes();
        attrs.put("version", "1.0");
        attrs.put("encoding", "UTF-8");
        xmlDeclaration = new XmlDeclaration("xml", null, false, attrs);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xmlns", null, false);
        assertEquals("xmlns", xmlDeclaration.name());
        assertEquals("<xmlns />", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xmlns:xsi", null, false);
        assertEquals("xmlns:xsi", xmlDeclaration.name());
        assertEquals("<xmlns:xsi />", xmlDeclaration.getWholeDeclaration());
    }

    /**
     * Test outerHtmlHead method
     */
    @Test
    public void testOuterHtmlHead() throws IOException {
        Document doc = new Document();
        Element root = doc.createElement("root");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable appendable = new StringBuilder();

        xmlDeclaration.outerHtmlHead(appendable, 0, null);
        assertEquals("<!xml version=\"1.0\" encoding=\"UTF-8\">", appendable.toString());

        xmlDeclaration = new XmlDeclaration("xmlns", null, false);
        xmlDeclaration.outerHtmlHead(appendable, 0, null);
        assertEquals("<xmlns />", appendable.toString());

        xmlDeclaration = new XmlDeclaration("xmlns:xsi", null, false);
        xmlDeclaration.outerHtmlHead(appendable, 0, null);
        assertEquals("<xmlns:xsi />", appendable.toString());
    }

    /**
     * Test outerHtmlTail method
     */
    @Test
    public void testOuterHtmlTail() throws IOException {
        Document doc = new Document();
        Element root = doc.createElement("root");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable appendable = new StringBuilder();

        xmlDeclaration.outerHtml(appendable);
        xmlDeclaration.outerHtmlTail(appendable, 0, null);
        assertEquals("</xml>", appendable.toString());

        xmlDeclaration = new XmlDeclaration("xmlns", null, false);
        xmlDeclaration.outerHtml(appendable);
        xmlDeclaration.outerHtmlTail(appendable, 0, null);
        assertEquals("</xmlns>", appendable.toString());

        xmlDeclaration = new XmlDeclaration("xmlns:xsi", null, false);
        xmlDeclaration.outerHtml(appendable);
        xmlDeclaration.outerHtmlTail(appendable, 0, null);
        assertEquals("</xmlns:xsi>", appendable.toString());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() throws IOException {
        Document doc = new Document();
        Element root = doc.createElement("root");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        Appendable appendable = new StringBuilder();

        xmlDeclaration.outerHtml(appendable);
        assertEquals("<xml />", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("xmlns", null, false);
        xmlDeclaration.outerHtml(appendable);
        assertEquals("<xmlns />", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("xmlns:xsi", null, false);
        xmlDeclaration.outerHtml(appendable);
        assertEquals("<xmlns:xsi />", xmlDeclaration.toString());
    }

    /**
     * Test validate constructor arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testValidateConstructorArguments() {
        new XmlDeclaration(null, null, false);
    }
}