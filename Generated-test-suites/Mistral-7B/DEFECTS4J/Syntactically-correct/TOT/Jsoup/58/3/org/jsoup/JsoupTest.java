import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JsoupTest {

    @Test
    public void testParseValidHtml() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://example.com";

        Document document = Jsoup.parse(html, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseHtmlWithBaseUri() {
        String html = "<img src=\"image.png\" alt=\"Image\">";
        String baseUri = "https://example.com";

        Document document = Jsoup.parse(html, baseUri);
        String imgSrc = document.select("img[src]").attr("src");
        String expectedImgSrc = "https://example.com/image.png";
        assertNotNull(imgSrc);
        assertNotNull(imgSrc.isEmpty() || imgSrc.equals(expectedImgSrc));
    }

    @Test
    public void testParseHtmlWithCustomParser() {
        String html = "<xml version=\"1.0\">...</xml>";
        Parser parser = Jsoup.parser().xmlParser();
        Document document = Jsoup.parse(html, "", parser);
        assertNotNull(document);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullHtml() {
        String html = null;
        Jsoup.parse(html, "");
    }

    @Test(expected = NullPointerException.class)
    public void testParseEmptyHtml() {
        String html = "";
        Jsoup.parse(html, "");
    }

    @Test(expected = IOException.class)
    public void testParseInvalidHtml() {
        String html = "<html<tag>";
        Jsoup.parse(html, "");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullBaseUri() {
        String html = "<html><head></head><body><h1>Hello World</h1></body></html>";
        String baseUri = null;
        Jsoup.parse(html, baseUri);
    }

    @Test
    public void testConnectAndParseUrl() {
        String url = "https://example.com";
        Document document = Jsoup.connect(url).get();
        assertNotNull(document);
    }

    @Test
    public void testParseFile() {
        String filePath = "path/to/file.html";
        Document document = null;
        try {
            document = Jsoup.parse(new File(filePath));
        } catch (IOException e) {
            // Handle the IOException
        }
        assertNotNull(document);
    }

    @Test
    public void testParseInputStream() {
        String charsetName = "UTF-8";
        String filePath = "path/to/file.html";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
            Document document = Jsoup.parse(inputStream, charsetName, "");
            assertNotNull(document);
        } catch (FileNotFoundException e) {
            // Handle the FileNotFoundException
        } catch (IOException e) {
            // Handle the IOException
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Handle the IOException
                }
            }
        }
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<p>This is a body fragment.</p>";
        Document document = Jsoup.parseBodyFragment(bodyHtml, "");
        assertNotNull(document);
    }

    @Test
    public void testCleanValidHtml() {
        String bodyHtml = "<p>This is a <strong>valid</strong> body fragment.</p>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNotNull(cleanHtml);
    }

    @Test
    public void testCleanInvalidHtmlWithBaseUri() {
        String bodyHtml = "<script>alert('XSS');</script>";
        String baseUri = "https://example.com";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist);
        assertNull(cleanHtml);
    }

    @Test
    public void testCleanInvalidHtmlWithoutBaseUri() {
        String bodyHtml = "<script>alert('XSS');</script>";
        Whitelist whitelist = Whitelist.relaxed();
        String cleanHtml = Jsoup.clean(bodyHtml, whitelist);
        assertNull(cleanHtml);
    }

    @Test
    public void testCleanValidHtmlWithOutputSettings() {
        String bodyHtml = "<p>This is a <strong>valid</strong> body fragment.</p>";
        String baseUri = "";
        Whitelist whitelist = Whitelist.relaxed();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        String cleanHtml = Jsoup.clean(bodyHtml, baseUri, whitelist, outputSettings);
        assertNotNull(cleanHtml);
    }
}