package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for XmlDeclaration class
 */
public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationCreation() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertEquals(baseUri, xmlDeclaration.baseUri());
        assertTrue(xmlDeclaration.isProcessingInstruction());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlDeclarationNullName() {
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);
        fail("Expected NullPointerException");
    }

    @Test
    public void testXmlDeclarationGetWholeDeclaration() {
        String name = "xml";
        String baseUri = "baseUri";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, false);
        assertEquals(name, xmlDeclaration.getWholeDeclaration());

        Attribute version = new Attribute("version", "1.0");
        Attribute encoding = new Attribute("encoding", "UTF-8");
        xmlDeclaration.attributes().add(version);
        xmlDeclaration.attributes().add(encoding);
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Document document = new Document();
        Appendable accumulator = document.outputSettings().indentAmount(4).buffer();
        xmlDeclaration.outerHtmlHead(accumulator, 0, document.outputSettings());
        assertEquals("<?xml?>\n", accumulator.toString());

        Attribute version = new Attribute("version", "1.0");
        Attribute encoding = new Attribute("encoding", "UTF-8");
        xmlDeclaration.attributes().add(version);
        xmlDeclaration.attributes().add(encoding);
        document.outputSettings().setOmitXmlDeclaration(false);
        xmlDeclaration.outerHtmlHead(accumulator, 0, document.outputSettings());
        assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml/\"><xml version=\"1.0\" encoding=\"UTF-8\"/>\n", accumulator.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Document document = new Document();
        Appendable accumulator = document.outputSettings().indentAmount(4).buffer();
        xmlDeclaration.outerHtmlTail(accumulator, 0, document.outputSettings());
        assertEquals("", accumulator.toString());

        Attribute version = new Attribute("version", "1.0");
        Attribute encoding = new Attribute("encoding", "UTF-8");
        xmlDeclaration.attributes().add(version);
        xmlDeclaration.attributes().add(encoding);
        document.outputSettings().setOmitXmlDeclaration(false);
        xmlDeclaration.outerHtmlTail(accumulator, 0, document.outputSettings());
        assertEquals("", accumulator.toString());
    }

    @Test
    public void testXmlDeclarationToString() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        Document document = new Document();
        assertEquals(xmlDeclaration.toString(), xmlDeclaration.outerHtml());

        Attribute version = new Attribute("version", "1.0");
        Attribute encoding = new Attribute("encoding", "UTF-8");
        xmlDeclaration.attributes().add(version);
        xmlDeclaration.attributes().add(encoding);
        document.outputSettings().setOmitXmlDeclaration(false);
        assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", xmlDeclaration.toString());
    }
}