package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.*;
import java.io.StringReader;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String xmlInput = "<root><child></child></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xmlInput), "");

        assertEquals(1, doc.getDocumentElement().getChildNodes().getLength());
        Element child = (Element) doc.getDocumentElement().getChildNodes().item(0);
        assertEquals("child", child.getNodeName());
    }

    @Test
    public void testParseEmptyInput() {
        Document doc = XmlTreeBuilder.parse("", "");
        assertNull(doc);
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child></child></root>";
        List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "", new ParseErrorList(), ParseSettings.preserveCase);

        assertEquals(2, nodes.size());
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);
        assertEquals("child", child.getNodeName());
        assertEquals("root", root.getNodeName());
    }
}