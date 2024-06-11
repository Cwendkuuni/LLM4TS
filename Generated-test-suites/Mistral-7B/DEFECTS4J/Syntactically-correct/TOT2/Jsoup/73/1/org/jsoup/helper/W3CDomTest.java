import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String w3cDocumentAsString = new W3CDom().asString(w3cDocument);
        String expectedJsoupHtml = "<html><body><h1>Test</h1></body></html>";
        Assert.assertEquals(expectedJsoupHtml, w3cDocumentAsString);
    }
}