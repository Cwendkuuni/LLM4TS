import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class JsoupTest {
    

    @Test
    public void testParseWithBaseUri() {
        String html = "<html><head><base href='https://example.com'></base></head><body><a href='/about.html'>About</a></body></html>";
        String baseUri = "https://example.com";
        Document document = Jsoup.parse(html, baseUri);
        String linkHref = document.select("a[href]").first().attr("href");
        assert linkHref.equals("/about.html");
    }

    @Test
    public void testParseWithEmptyBaseUri() {
        String html = "<html><head><base href=''></base></head><body><a href='/about.html'>About</a></body></html>";
        Document document = Jsoup.parse(html);
        String linkHref = document.select("a[href]").first().attr("href");
        assert linkHref.equals("/about.html");
    }

    @Test
    public void testParseWithSimpleHtml() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Document document = Jsoup.parse(html);
        String bodyText = document.body().text();
        assert bodyText.equals("Hello World");
    }

    @Test(expected = IOException.class)
    public void testParseWithInvalidHtml() throws IOException {
        String html = "<html><body><p>Hello <script>alert('XSS')</script> World</p></body></html>";
        Document document = Jsoup.parse(html);
    }

    @Test
    public void testParseWithParser() {
        String html = "<html><body><p>Hello World</p></body></html>";
        Parser parser = org.jsoup.parser.Jsoup.parser();
        Document document = Jsoup.parse(html, "", parser);
        String bodyText = document.body().text();
        assert bodyText.equals("Hello World");
    }

    
}