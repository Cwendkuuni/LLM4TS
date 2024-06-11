import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("sample.html"); // Replace with a valid HTML file
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
}