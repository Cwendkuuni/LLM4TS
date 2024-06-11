import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document document = new Document("http://example.com");
 Assert.assertNotNull(document);
 Assert.assertEquals("#document", document.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("http://example.com");
 Element html = document.head();
 Assert.assertNotNull(html);
 Assert.assertEquals("html", html.nodeName());

 Element head = html.head();
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.nodeName());

 Element body = html.body();
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.nodeName());
 }

 @Test
 public void testGetHeadAndBody() {
 Document document = new Document("http://example.com");
 Element head = document.head();
 Element body = document.body();

 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document document = new Document("http://example.com");
 document.title("Test Title");
 String title = document.title();

 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document document = new Document("http://example.com");
 document.title(null);
 String oldTitle = document.title();
 document.title("New Title");
 String newTitle = document.title();

 Assert.assertEquals("", oldTitle);
 Assert.assertEquals("New Title", newTitle);
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("http://example.com");
 Element element = document.createElement("div");

 Assert.assertNotNull(element);
 Assert.assertEquals("div", element.nodeName());
 }

 @Test
 public void testNormalise() {
 Document document = new Document("http://example.com");
 document.body().appendText("Test text 1");
 document.head().appendText("Test text 2");
 document.appendText("Test text 3");
 document.normalise();

 Element body = document.body();
 List<Node> bodyNodes = body.childNodes();
 TextNode bodyTextNode = (TextNode) bodyNodes.get(0);

 Assert.assertEquals("Test text 1Test text 3", bodyTextNode.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document document = new Document("http://example.com");
 Element html = document.head();
 html.appendElement("html");
 Element head = html.appendElement("head");
 Element body = head.appendElement("body");
 body.appendElement("div");
 body.appendText("Test text");

 Element foundElement = document.findFirstElementByTagName("body", this);
 Assert.assertNotNull(foundElement);
 Assert.assertEquals("body", foundElement.nodeName());
 }

 @Test
 public void testOuterHtml() {
 Document document = new Document("http://example.com");
 document.body().appendText("Test text 1");
 document.head().appendText("Test text 2");
 document.title("Test Title");
 String htmlString = document.outerHtml();

 Assert.assertTrue(htmlString.contains("Test Title"));
 Assert.assertTrue(htmlString.contains("Test text 1"));
 Assert.assertTrue(htmlString.contains("Test text 2"));
 }

 @Test
 public void testText() {
 Document document = new Document("http://example.com");
 document.body().appendText("Test text");
 String text = document.text();

 Assert.assertEquals("Test text", text);
 }
}