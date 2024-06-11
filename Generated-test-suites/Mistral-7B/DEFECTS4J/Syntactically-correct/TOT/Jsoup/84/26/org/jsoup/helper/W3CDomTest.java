package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.rpc.encoding.ModelSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException, TransformerException, IOException {
        String html = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(html);
        W3CDom w3CDom = new W3CDom();
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        // Test conversion with empty document
        Document emptyW3cDoc = w3CDom.fromJsoup(org.jsoup.nodes.Document.empty());
        String emptyW3cDocString = w3CDom.asString(emptyW3cDoc);
        Assert.assertEquals("", emptyW3cDocString);

        // Test conversion with basic HTML document
        String w3cDocString = w3CDom.asString(w3cDoc);
        Assert.assertEquals("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>", w3cDocString);

        // Test conversion with namespaces
        String htmlWithNamespace = "<html xmlns:test=\"http://test.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Test</title></head><body><p xml:space=\"preserve\" xml:lang=\"en\">Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithNamespace = org.jsoup.Jsoup.parse(htmlWithNamespace);
        Document w3cDocWithNamespace = w3CDom.fromJsoup(jsoupDocWithNamespace);
        String w3cDocWithNamespaceString = w3CDom.asString(w3cDocWithNamespace);
        Assert.assertEquals(htmlWithNamespace, w3cDocWithNamespaceString);

        // Test conversion with comments
        String htmlWithComment = "<html><!-- This is a comment --><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithComment = org.jsoup.Jsoup.parse(htmlWithComment);
        Document w3cDocWithComment = w3CDom.fromJsoup(jsoupDocWithComment);
        String w3cDocWithCommentString = w3CDom.asString(w3cDocWithComment);
        Assert.assertTrue(w3cDocWithCommentString.contains("<!-- This is a comment -->"));

        // Test conversion with text nodes
        String htmlWithTextNodes = "<html><head><title>Test</title></head><body><p>Text 1 Text 2</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithTextNodes = org.jsoup.Jsoup.parse(htmlWithTextNodes);
        Document w3cDocWithTextNodes = w3CDom.fromJsoup(jsoupDocWithTextNodes);
        String w3cDocWithTextNodesString = w3CDom.asString(w3cDocWithTextNodes);
        Assert.assertTrue(w3cDocWithTextNodesString.contains("Text 1 Text 2"));

        // Test conversion with data nodes
        String htmlWithDataNodes = "<html><head><title>Test</title></head><body><p data-test=\"test\">Data 1 Data 2</p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithDataNodes = org.jsoup.Jsoup.parse(htmlWithDataNodes);
        Document w3cDocWithDataNodes = w3CDom.fromJsoup(jsoupDocWithDataNodes);
        String w3cDocWithDataNodesString = w3CDom.asString(w3cDocWithDataNodes);
        Assert.assertEquals("<html><head><title>Test</title></head><body><p data-test=\"test\">Data 1 Data 2</p></body></html>", w3cDocWithDataNodesString);

        // Test conversion with empty text nodes
        String htmlWithEmptyTextNodes = "<html><head><title>Test</title></head><body><p></p></body></html>";
        org.jsoup.nodes.Document jsoupDocWithEmptyTextNodes = org.jsoup.parse(htmlWithEmptyTextNodes);
        Document w3cDocWithEmptyTextNodes = w3CDom.fromJsoup(jsoupDocWithEmptyTextNodes);
        String w3cDocWithEmptyTextNodesString = w3CDom.asString(w3cDocWithEmptyTextNodes);
        Assert.assertEquals("<html><head><title>Test</title></head><body><p></p></body></html>", w3cDocWithEmptyTextNodesString);

        // Test serialization and parsing back to jsoup document
        Document serializationTestDoc = DocumentBuilderFactory.newInstance().newDocument();
        serializationTestDoc.createElement("root").setTextContent("test");
        String w3cDocForSerialization = w3CDom.asString(serializationTestDoc);
        InputStream inputStream = new ByteArrayInputStream(w3cDocForSerialization.getBytes());
        Document jsoupDocForSerialization = (org.jsoup.nodes.Document) org.jsoup.Jsoup.parse(inputStream, null, Parser.htmlParser());
        Assert.assertEquals(serializationTestDoc.getDocumentElement().getTextContent(), jsoupDocForSerialization.select("root").text());
    }
}