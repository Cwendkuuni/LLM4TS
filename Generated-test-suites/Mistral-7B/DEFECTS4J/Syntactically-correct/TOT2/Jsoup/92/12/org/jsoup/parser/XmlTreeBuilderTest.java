package org.jsoup.parser; // adjust the package if necessary

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("", ""); // empty xml
        assertNotNull(doc);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testParseSimpleXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child>Hello</child></root>";
        Document doc = builder.parse(xml, "");
        Element root = doc.select("root").first();
        Element child = root.select("child").first();
        assertEquals("Hello", child.text());
    }

    // Add test cases for each method and potential edge cases/exception handling scenarios

    @Test
    public void testParseInvalidXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child></child><!--comment-->"; // missing end tag for child element
        Document doc = builder.parse(xml, "");
        Element root = doc.select("root").first();
        assertEquals(1, root.childNodes().size()); // child node should be the comment, not the missing child element
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><child/>";
        builder.parse(xml, null);
    }

    // Add more test cases as needed
}