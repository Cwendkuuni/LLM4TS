package org.jsoup.helper;

import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoupAndConvert() {
        // Given: a valid jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // When: converting the jsoup Document to a W3C Document using both methods
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then: the W3C Document should not be null
        assertNotNull(w3cDoc);

        // Testing functionality
        String w3cDocAsString = new W3CDom().asString(w3cDoc);
        assertEquals("<html><body><p>Hello World</p></body></html>", w3cDocAsString);

        // Edge cases
        org.jsoup.nodes.Document emptyJsoupDoc = org.jsoup.Jsoup.parse("");
        Document emptyW3CDoc = new Document();
        new W3CDom().convert(emptyJsoupDoc, emptyW3CDoc);
        assertNotNull(emptyW3CDoc);

        org.jsoup.nodes.Document invalidJsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello <script>World</script></p></body></html>");
        Document invalidW3CDoc = new Document();
        try {
            new W3CDom().fromJsoup(invalidJsoupDoc);
            w3cDoc = new Document();
            new W3CDom().convert(invalidJsoupDoc, w3cDoc);
            fail("Expected ParserConfigurationException and NullPointerException");
        } catch (IllegalStateException | NullPointerException e) {
            // Expected
        }
    }
}