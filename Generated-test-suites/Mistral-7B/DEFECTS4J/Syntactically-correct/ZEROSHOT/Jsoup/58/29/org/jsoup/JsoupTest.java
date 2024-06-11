import org.junit.Test;
import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.HttpConnection;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {

    @Test
    public void testParseHTML() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testParseHTMLWithBaseURI() {
        String html = "<html><body><a href=\"about.html\">About</a></body></html>";
        String baseURI = "http://example.com";
        Document doc = Jsoup.parse(html, baseURI);
        Assert.assertEquals("http://example.com/about.html", doc.select("a[href]").attr("href"));
    }

    @Test
    public void testParseHTMLWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = org.jsoup.parser.Jsoup.xmlParser();
        Document doc = Jsoup.parse(html, "", parser);
        Assert.assertEquals("Hello World", doc.title());
        Assert.assertEquals(1, doc.body().childNodeSize());
    }

    @Test
    public void testConnectAndParseURL() throws IOException {
        Document doc = Jsoup.connect("http://www.example.com").get();
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testParseFile() throws IOException {
        File inputFile = new File("input.html");
        Document doc = Jsoup.parse(inputFile, "UTF-8");
        Assert.assertEquals("Title", doc.title());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/input.html");
        Document doc = Jsoup.parse(inputStream, "UTF-8");
        Assert.assertEquals("Title", doc.title());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>Hello World</p>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Assert.assertEquals("Hello World", doc.body().text());
    }

    @Test
    public void testParseBodyFragmentWithBaseURI() {
        String bodyHtml = "<p><img src=\"image.jpg\"></p>";
        String baseURI = "http://example.com";
        Document doc = Jsoup.parseBodyFragment(bodyHtml, baseURI);
        Element imgElement = doc.select("img").first();
        Assert.assertEquals("http://example.com/image.jpg", imgElement.attr("src"));
    }

    @Test
    public void testParseURLWithTimeout() {
        Connection connection = Jsoup.connect("http://www.slow-loading-site.com");
        connection.timeout(500);
        Document doc = connection.get();
        Assert.assertNotNull(doc.title());
    }

    @Test
    public void testCleanHTML() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><h1>Hello World</h1></body></html>";
        String baseURI = "http://example.com";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String safeHTML = Jsoup.clean(bodyHtml, baseURI, whitelist);
        Assert.assertEquals("Hello World", safeHTML);
    }

    @Test
    public void testCleanHTMLWithWhitelist() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><h1>Hello World</h1></a></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        String safeHTML = Jsoup.clean(bodyHtml, whitelist);
        Assert.assertEquals("", safeHTML);
    }

    @Test
    public void testIsValidHTML() {
        String bodyHtml = "<html><body><script>alert('XSS')</script><h1>Hello World</h1></a></body></html>";
        Whitelist whitelist = Whitelist.simpleTextOnly();
        boolean isValid = Jsoup.isValid(bodyHtml, whitelist);
        Assert.assertEquals(false, isValid);
    }
}