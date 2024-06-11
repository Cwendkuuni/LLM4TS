package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        // When converting the jsoup Document to a W3C Document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then the resulting W3C Document should be valid
        assertEquals(w3cDoc.getDocumentElement().getTagName(), "html");

        // And should contain an h1 element with the text "Hello World"
        Element h1Element = w3cDoc.getDocumentElement().getElementsByTagName("h1").item(0);
        assertEquals(h1Element.getTextContent(), "Hello World");
    }

    @Test
    public void testConvert() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        // And a blank W3C Document
        Document w3cDoc = new Document();

        // When converting the jsoup Document to the W3C Document
        new W3CDom().convert(jsoupDoc, w3cDoc);

        // Then the resulting W3C Document should be equal to the original jsoup Document
        assertEquals(new W3CDom().asString(jsoupDoc), new W3CDom().asString(w3cDoc));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullJsoupDoc() {
        // Given a null jsoup Document
        // When trying to convert a null jsoup Document
        new W3CDom().fromJsoup(null);

        // Then an IllegalArgumentException should be thrown
    }
}