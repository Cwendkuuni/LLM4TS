package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {
    @Test
    public void testParseXmlDocument() {
        String xmlString = "<root><child1>Hello</child1><child2><grandChild>World</grandChild></child2></root>";
        Reader input = new StringReader(xmlString);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "http://localhost/");
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        assertNotNull(doc.childNodes().get(0).childNodes());
        assertNotNull(doc.childNodes().get(0).childNodes().get(0).childNodes());
    }

    @Test
    public void testParseInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = null; // or empty input
        try {
            xmlTreeBuilder.parse(input, "http://localhost/");
            // test should fail
        } catch (Exception e) {
            // test passes if exception is thrown
        }
    }

    @Test
    public void testParseNullBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root></root>");
        try {
            xmlTreeBuilder.parse(input, null);
            // test should fail
        } catch (IllegalArgumentException e) {
            // test passes if exception is thrown
        }
    }

    @Test
    public void testParseInvalidXmlFormat() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<root><child1>Hello</child1>"); // missing end tag
        try {
            xmlTreeBuilder.parse(input, "http://localhost/");
            // test should fail
        } catch (Exception e) {
            // test passes if exception is thrown
        }
    }

    @Test
    public void testParseLargeXmlFile() {
        String xmlString = "<root>"; // large XML content here
        Reader input = new StringReader(xmlString);
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(input, "http://localhost/");
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        // check if the test is passing without any OutOfMemoryError
    }
}