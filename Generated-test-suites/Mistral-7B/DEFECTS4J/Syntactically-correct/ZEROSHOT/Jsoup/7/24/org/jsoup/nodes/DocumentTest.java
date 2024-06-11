import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.util.List;

public class DocumentTest {

 private Document createDocument(String baseUri) {
 return new Document(baseUri);
 }

 @Test
 public void testCreateDocument() {
 Document doc = createDocument("http://example.com");
 Assert.assertEquals("http://example.com", doc.baseUri());
 Assert.assertEquals(Tag.valueOf("#root"), doc.tagName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Assert.assertNotNull(html);
 Assert.assertEquals(Tag.HTML, html.tagName());
 html = doc.body();
 Assert.assertNotNull(html);
 Assert.assertEquals(Tag.BODY, html.tagName());
 }

 @Test
 public void testGetHeadElement() {
 Document doc = createDocument("http://example.com");
 Element head = doc.head();
 Assert.assertNotNull(head);
 Assert.assertEquals(Tag.HEAD, head.tagName());
 }

 @Test
 public void testGetBodyElement() {
 Document doc = createDocument("http://example.com");
 Element body = doc.body();
 Assert.assertNotNull(body);
 Assert.assertEquals(Tag.BODY, body.tagName());
 }

 @Test
 public void testGetTitle() {
 Document doc = createDocument("http://example.com");
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = createDocument("http://example.com");
 doc.title(null);
 Assert.assertEquals("", doc.title());
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocument("http://example.com");
 Element element = doc.createElement("div");
 Assert.assertNotNull(element);
 Assert.assertEquals(Tag.DIV, element.tagName());
 Assert.assertEquals("http://example.com", element.baseUri());
 }

 @Test
 public void testNormalise() {
 Document doc = createDocument("http://example.com");
 Element title = doc.appendElement("title").text("Test Title");
 Element body = doc.appendElement("body").text("Test Body");
 doc.normalise();
 List<Element> titleList = doc.getElementsByTag("title");
 List<Element> bodyList = doc.getElementsByTag("body");
 Assert.assertEquals(1, titleList.size());
 Assert.assertEquals(1, bodyList.size());
 Assert.assertEquals(title, titleList.get(0));
 Assert.assertEquals(body, bodyList.get(0));
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = createDocument("http://example.com");
 Element html = doc.findFirstElementByTagName("html", doc);
 Assert.assertNotNull(html);
 Assert.assertEquals(Tag.HTML, html.tagName());
 Element head = doc.findFirstElementByTagName("head", doc);
 Assert.assertNotNull(head);
 Assert.assertEquals(Tag.HEAD, head.tagName());
 Element body = doc.findFirstElementByTagName("body", doc);
 Assert.assertNotNull(body);
 Assert.assertEquals(Tag.BODY, body.tagName());
 }

 @Test
 public void testOuterHtml() {
 Document doc = createDocument("http://example.com");
 doc.head().appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1");
 doc.body().appendElement("div").text("Test");
 String html = doc.outerHtml();
 Assert.assertTrue(html.contains("<html"));
 Assert.assertTrue(html.contains("<head>"));
 Assert.assertTrue(html.contains("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\""));
 Assert.assertTrue(html.contains("<body>"));
 Assert.assertTrue(html.contains("<div>Test</div>"));
 }

 @Test
 public void testText() {
 Document doc = createDocument("http://example.com");
 doc.text("Test");
 String text = doc.text();
 Assert.assertEquals("Test", text);
 }
}