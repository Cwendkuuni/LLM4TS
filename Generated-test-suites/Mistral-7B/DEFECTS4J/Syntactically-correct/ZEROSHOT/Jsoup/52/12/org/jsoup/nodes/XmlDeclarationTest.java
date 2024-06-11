package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

public class XmlDeclarationTest {

    @org.junit.Test
    public void testXmlDeclarationCreation() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertNull(xmlDeclaration.toString().trim().substring(0, 1));

        XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", null, true);
        assertEquals("#declaration", xmlProcessingInstruction.nodeName());
        assertEquals("xml", xmlProcessingInstruction.name());
        assertEquals("<?xml", xmlProcessingInstruction.toString().trim());
    }

    @org.junit.Test
    public void testXmlDeclarationWithAttributes() throws IOException {
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");

        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        xmlDeclaration.attributes = attributes;

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString().trim());
    }

    @org.junit.Test
    public void testXmlDeclarationOuterHtml() throws IOException {
        Document document = Jsoup.parse("<root><declaration version=\"1.0\" encoding=\"UTF-8\"/></root>");
        Element root = document.selectFirst("root");

        XmlDeclaration xmlDeclaration = (XmlDeclaration) root.child(0);
        StringBuilder expectedOutput = new StringBuilder();
        xmlDeclaration.outerHtmlHead(expectedOutput, 0, null);
        expectedOutput.append(" version=\"1.0\" encoding=\"UTF-8\"");
        xmlDeclaration.outerHtmlTail(expectedOutput, 0, null);

        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());
        assertEquals("<? Declaration version=\"1.0\" encoding=\"UTF-8\"?>\n", xmlDeclaration.outerHtml());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testXmlDeclarationWithNullName() throws IOException {
        new XmlDeclaration(null, null, false);
    }
}