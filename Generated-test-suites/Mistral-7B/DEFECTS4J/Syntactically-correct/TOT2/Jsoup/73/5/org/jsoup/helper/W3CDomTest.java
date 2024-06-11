import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.assertNotNull;

public class ForJsoupMethodTest {
   @Test
   public void testFromJsoup() {
      org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("test.html");
      Document w3cDocument = W3CDom.fromJsoup(jsoupDocument);
      assertNotNull(w3cDocument);
   }
}