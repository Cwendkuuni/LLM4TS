import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDocument = new Document(); // create an empty jsoup document
        Document w3cDocument = W3CDom.fromJsoup(jsoupDocument); // call the method under test
        assertNotNull(w3cDocument); // check that the method didn't return null
    }
}