package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import java.io.StringReader;
import java.io.IOException;

public class XmlTreeBuilderTest {
    

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertTrue(settings.equals(ParseSettings.preserveCase));
    }

    @Test
    public void testParseReader() throws IOException {
        String xml = "<root><child><grandchild/></child></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), "http://example.com");

        assertEquals("<root><child><grandchild/></child></root>", doc.html());
        assertEquals("<root><child><grandchild/></child></root>", doc.xml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseReaderNullInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(null, "http://example.com");
    }

    @Test(expected = IOException.class)
    public void testParseReaderInvalidXml() throws IOException {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<root><child>"), "http://example.com");
    }

    // Add test cases for other methods and edge cases here

    
}