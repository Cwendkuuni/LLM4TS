import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {
    
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }
}