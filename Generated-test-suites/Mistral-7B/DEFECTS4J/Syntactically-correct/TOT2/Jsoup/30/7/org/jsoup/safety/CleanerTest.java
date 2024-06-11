import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Assert;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<div><p>Hello World</p></div>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("<p>Hello World</p>", cleanedDocument.body().html());
    }

    @Test
    public void testCleanerWithInvalidWhitelist() {
        Whitelist whitelist = Whitelist.simpleText();
        whitelist.addTags("script");
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        Assert.assertEquals("", cleanedDocument.body().html());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<div><p>Hello World</p></div>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);
    }
}