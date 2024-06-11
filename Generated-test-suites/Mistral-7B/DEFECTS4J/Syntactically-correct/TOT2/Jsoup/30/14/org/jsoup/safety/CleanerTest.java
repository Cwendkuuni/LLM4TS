import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CleanerTest {
   private Document dirtyDocument;
   private Cleaner cleaner;
   private Document cleanDocument;
   private Whitelist whitelist;

   @Before
   public void setup() {
       whitelist = Whitelist.simpleTextOnly();
       dirtyDocument = Jsoup.parse("<html><body><div id='test' onclick='alert(\"XSS\")'>Test</div></body></html>");
       cleaner = new Cleaner(whitelist);
   }

   @Test
   public void testCleanMethod() {
       cleanDocument = cleaner.clean(dirtyDocument);
       assertEquals(cleanDocument.body().text(), "");
   }

   // Test cases for edge cases and exception handling go here
}