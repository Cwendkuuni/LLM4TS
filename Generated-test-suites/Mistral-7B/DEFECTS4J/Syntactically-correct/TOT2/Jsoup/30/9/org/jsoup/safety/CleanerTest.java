import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.safety.*;
import org.junit.Test;

public class CleanerTest {
  @Test
  public void testConstructor() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
  }

  @Test
  public void testCleanMethod() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
  }

  @Test
  public void testIsValidMethod() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
    assertTrue(cleaner.isValid(dirtyDocument));
  }

  @Test
  public void testEmptyWhitelist() {
    Cleaner cleaner = new Cleaner(new Whitelist() {});
    Document dirtyDocument = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><body></body></html>", cleanDocument.body().html());
  }

  @Test(expected = NullPointerException.class)
  public void testNullDocument() {
    Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
    Document dirtyDocument = null;
    cleaner.clean(dirtyDocument);
  }

  @Test
  public void testFramesetDocument() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parse("<html><head><frameset></frameset></head><body></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><head></head><body></body></html>", cleanDocument.body().html());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDocument() {
    Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
    Document dirtyDocument = Jsoup.parse("<html><head></head></html>");
    cleaner.clean(dirtyDocument);
  }

  @Test
  public void testUnallowedTagsAndAttributes() {
    Whitelist whitelist = new Whitelist() {
        @Override
        public boolean isSafeTag(String name) {
            return name.equalsIgnoreCase("h1");
        }

        @Override
        public boolean isSafeAttribute(String tag, Element context, Attribute attr) {
            return false;
        }
    };
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parse("<html><body><h1 id='test' class='test'><script>alert('XSS')</script></h1></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
  }

  @Test
  public void testScriptAndStyleTags() {
    Whitelist whitelist = new Whitelist() {
        @Override
        public boolean isSafeTag(String name) {
            return name.equalsIgnoreCase("script") || name.equalsIgnoreCase("style");
        }

        @Override
        public boolean isSafeAttribute(String tag, Element context, Attribute attr) {
            return false;
        }
    };
    Cleaner cleaner = new Cleaner(whitelist);
    Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><body></body></html>", cleanDocument.body().html());
  }

  @Test
  public void testMalformedHTML() {
    Cleaner cleaner = new Cleaner(Whitelist.simpleDefault());
    Document dirtyDocument = Jsoup.parse("<html><body><h1><script>alert('XSS')</script></h1>");
    Document cleanDocument = cleaner.clean(dirtyDocument);
    assertEquals("<html><body><h1></h1></body></html>", cleanDocument.body().html());
  }

  @Test
  public void testCopySafeNodesMethod() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
    Element root = new Element("html");
    Element destination = new Element("body");
    int numDiscarded = cleaner.copySafeNodes(root, destination);
    assertEquals(0, numDiscarded);
  }

  @Test
  public void testCreateSafeElementMethod() {
    Whitelist whitelist = Whitelist.simpleDefault();
    Cleaner cleaner = new Cleaner(whitelist);
    Element sourceEl = new Element("h1");
    sourceEl. besk("class", "test");
    ElementMeta meta = cleaner.createSafeElement(sourceEl);
    assertEquals("<h1></h1>", meta.el.html());
    assertEquals(1, meta.numAttribsDiscarded);
  }
}