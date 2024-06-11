import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // When converting the jsoup Document to a W3C Document
        Document w3cDoc = W3CDom.getInstance().fromJsoup(jsoupDoc);

        // Then the W3C Document should not be null
        assertNotNull(w3cDoc);

        // Then the W3C Document should have the same content as the jsoup Document
        String w3cDocContent = W3CDom.getInstance().asString(w3cDoc);
        String jsoupDocContent = jsoupDoc.body().text();
        assertEquals(jsoupDocContent, w3cDocContent);
    }

    // ...
}