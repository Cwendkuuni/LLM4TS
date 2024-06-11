import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsoupTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Document doc = Jsoup.parse(html);
        Element head = doc.head();
        Element body = doc.body();
        Element h1 = doc.selectFirst("h1");

        Assert.assertNotNull(head);
        Assert.assertNull(head.text());
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World", h1.text());
    }

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href=\"/test\"></head><body><a href=\"about.html\"></a></body></html>";
        Document doc = Jsoup.parse(html, "http://example.com/test");
        Element link = doc.selectFirst("a");

        Assert.assertNotNull(link);
        Assert.assertEquals("about.html", link.attr("abs:href"));
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        Parser parser = new Parser.MalformedHtmlParser();
        Document doc = Jsoup.parse(html, "", parser);

        Element head = doc.head();
        Element body = doc.body();
        Element h1 = doc.selectFirst("h1");

        Assert.assertNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World", h1.text());
    }

    @Test
    public void testConnect() throws IOException {
        String url = "https://www.google.com";
        Document doc = Jsoup.connect(url).get();
        Element title = doc.title();

        Assert.assertNotNull(title);
        Assert.assertEquals("Google", title.text());
    }

    @Test
    public void testParseFile() throws IOException {
        String fileName = "test.html";
        String html = "<html><body><h1>Hello World</h1></body></html>";
        DataUtil.write(new File(fileName), html);

        Document doc = Jsoup.parse(new File(fileName));
        Element head = doc.head();
        Element body = doc.body();
        Element h1 = doc.selectFirst("h1");

        Assert.assertNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World", h1.text());

        DataUtil.delete(new File(fileName));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        ByteArrayInputStream in = new ByteArrayInputStream(html.getBytes());

        Document doc = Jsoup.parse(in, "UTF-8");
        Element head = doc.head();
        Element body = doc.body();
        Element h1 = doc.selectFirst("h1");

        Assert.assertNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World", h1.text());

        in.close();
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Jsoup.parseBodyFragment(bodyHtml);
        Element head = doc.head();
        Element body = doc.body();
        Element h1 = doc.selectFirst("h1");

        Assert.assertNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals("Hello World", h1.text());
    }

    @Test
    public void testClean() {
        String bodyHtml = "<script>alert('XSS')</script><h1>Hello World</h1>";
        Whitelist whitelist = Whitelist.simpleText();
        String cleanHtml = Jsoup.clean(bodyHtml, "", whitelist);

        Assert.assertNotNull(cleanHtml);
        Assert.assertEquals("Hello World", cleanHtml);
    }

}