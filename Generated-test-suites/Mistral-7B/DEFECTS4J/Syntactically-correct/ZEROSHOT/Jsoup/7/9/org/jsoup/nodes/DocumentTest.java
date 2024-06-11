import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document document = new Document("baseUri");
 Assert.assertNotNull(document);
 Assert.assertEquals("#root", document.tagName());
 }

 @Test
 public void testCreateShell() {
 Document document = Document.createShell("baseUri");
 Element html = document.head();
 Assert.assertNotNull(html);
 Assert.assertEquals("html", html.tagName());

 Element head = html.child(0);
 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.tagName());

 Element body = html.child(1);
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testAccessorMethodsForHeadAndBody() {
 Document document = new Document("baseUri");
 Element head = document.head();
 Element body = document.body();

 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testTitleGetterAndSetter() {
 Document document = new Document("baseUri");
 document.title("Test Title");
 String title = document.title();

 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testCreateElement() {
 Document document = new Document("baseUri");
 Element element = document.createElement("a");

 Assert.assertNotNull(element);
 Assert.assertEquals("a", element.tagName());
 }

 @Test
 public void testNormalise() {
 Document document = new Document("baseUri");
 document.body().appendText("Test Text");
 document.head().appendText("Test Head Text");
 document.appendText("Test Document Text");

 Document normalisedDocument = document.normalise();

 Element body = normalisedDocument.body();
 TextNode bodyTextNode = (TextNode) body.childNodes().get(0);
 Assert.assertEquals("Test Document Text Test Text", bodyTextNode.text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document document = new Document("baseUri");
 document.appendElement("html").appendElement("head").appendElement("body");

 Element head = document.findFirstElementByTagName("head", document);
 Element body = document.findFirstElementByTagName("body", document);

 Assert.assertNotNull(head);
 Assert.assertEquals("head", head.tagName());
 Assert.assertNotNull(body);
 Assert.assertEquals("body", body.tagName());
 }

 @Test
 public void testOuterHtmlAndText() {
 Document document = new Document("baseUri");
 document.body().appendText("Test Text");
 document.title("Test Title");

 String outerHtml = document.outerHtml();
 String text = document.text();

 Assert.assertEquals("<html><head><title>Test Title</title></head><body>Test Text</body></html>", outerHtml);
 Assert.assertEquals("Test Text", text);
 }

 @Test
 public void testNodeName() {
 Document document = new Document("baseUri");

 Assert.assertEquals("#document", document.nodeName());
 }
}