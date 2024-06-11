package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.namespace.QName;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document();

        // When
        Document out = new W3CDom().fromJsoup(in);

        // Then
        assertNotNull(out);
    }

    @Test
    public void testConvertWithEmptyDocument() {
        // Given
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document();
        Document out = new Document();

        // When
        new W3CDom().convert(in, out);

        // Then
        assertEquals("", out.getDocumentURI());
        assertNull(out.getDocumentElement());
    }

    @Test
    public void testConvertWithTextNode() {
        // Given
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document();
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello World!");
        in.appendChild(textNode);
        Document out = new Document();

        // When
        new W3CDom().convert(in, out);

        // Then
        Text text = (Text) out.getElementsByTagName("textNode").item(0);
        assertEquals("Hello World!", text.getNodeValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertWithNullDocument() {
        // Given
        Document out = null;

        // When
        new W3CDom().convert(new org.jsoup.nodes.Document(), out);

        // Then
        // Assertion error is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testConvertWithNullInputDocument() {
        // Given
        Document out = new Document();

        // When
        new W3CDom().convert(null, out);

        // Then
        // NullPointerException is thrown
    }

    @Test
    public void testAsStringWithEmptyDocument() {
        // Given
        Document doc = new Document();

        // When
        String result = new W3CDom().asString(doc);

        // Then
        assertEquals("", result);
    }

    @Test
    public void testAsStringWithTextNode() {
        // Given
        Document doc = new Document();
        Text textNode = doc.createTextNode("Hello World!");
        doc.appendChild(textNode);

        // When
        String result = new W3CDom().asString(doc);

        // Then
        assertEquals("Hello World!\n", result);
    }

    @Test(expected = TransformerException.class)
    public void testAsStringWithNullDocument() {
        // Given
        Document doc = null;

        // When
        String result = new W3CDom().asString(doc);

        // Then
        // TransformerException is thrown
    }
}