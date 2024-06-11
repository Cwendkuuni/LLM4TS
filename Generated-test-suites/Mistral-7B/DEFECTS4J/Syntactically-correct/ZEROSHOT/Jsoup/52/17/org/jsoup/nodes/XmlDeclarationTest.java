package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.IOException;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

    @org.junit.Test
    public void testXmlDeclarationWithNameAndBaseUri() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);

        String expectedXmlDeclaration = "<declaration name=\"" + xmlDeclarationName + "\" baseuri=\"" + baseUri + "\">";
        assertEquals(expectedXmlDeclaration, xmlDeclaration.toString());
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameProcessingInstructionFalse() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);

        String expectedXmlDeclaration = "<declaration name=\"" + xmlDeclarationName + "\" baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.outerHtml();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameProcessingInstructionTrue() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);

        String expectedXmlDeclaration = "<?xml name=\"" + xmlDeclarationName + "\" baseuri=\"" + baseUri + "\"%>";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameVersionAttribute() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");

        String expectedXmlDeclaration = "<declaration name=\"" + xmlDeclarationName + "\" version=\"1.0\" baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameEncodingAttribute() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        String expectedXmlDeclaration = "<declaration name=\"" + xmlDeclarationName + "\" encoding=\"UTF-8\" baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameVersionAndEncodingAttributes() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        String expectedXmlDeclaration = "<declaration name=\"" + xmlDeclarationName + "\" version=\"1.0\" encoding=\"UTF-8\" baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithNameVersionAndEncodingAttributesWhenIsProcessingInstructionTrue() throws IOException {
        String xmlDeclarationName = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        String expectedXmlDeclaration = "<?xml name=\"" + xmlDeclarationName + "\" version=\"1.0\" encoding=\"UTF-8\" %>";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithEmptyName() throws IOException {
        String xmlDeclarationName = "";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(xmlDeclarationName, baseUri, isProcessingInstruction);

        String expectedXmlDeclaration = "<declaration baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }

    @org.junit.Test
    public void testXmlDeclarationWithoutName() throws IOException {
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = false;
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, isProcessingInstruction);

        String expectedXmlDeclaration = "<declaration baseuri=\"" + baseUri + "\">";
        String outerHtml = xmlDeclaration.toString();
        assertEquals(expectedXmlDeclaration, outerHtml);
    }
}