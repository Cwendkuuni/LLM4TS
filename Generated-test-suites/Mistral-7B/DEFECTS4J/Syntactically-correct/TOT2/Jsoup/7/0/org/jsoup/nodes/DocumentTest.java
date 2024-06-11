import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

 private Document createDocument() {
 return new Document("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = createDocument();
 Assert.assertNotNull(doc);
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 Element html = doc.head();
 Element head = html.getFirstElementChild();
 Element body = html.getLastElementChild();
 Assert.assertEquals(Tag.HTML, html.tagName());
 Assert.assertEquals(Tag.HEAD, head.tagName());
 Assert.assertEquals(Tag.BODY, body.tagName());
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = createDocument();
 Element head = doc.head();
 Element body = doc.body();
 Assert.assertNotNull(head);
 Assert.assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = createDocument();
 doc.title("Test Title");
 String title = doc.title();
 Assert.assertEquals("Test Title", title);
 }

 @Test
 public void testSetTitle() {
 Document doc = createDocument();
 doc.title("Test Title");
 doc.title("New Title");
 String title = doc.title();
 Assert.assertEquals("New Title", title);
 }

 @Test
 public void testCreateElement() {
 Document doc = createDocument();
 Element p = doc.createElement("p");
 Assert.assertEquals(Tag.P, p.tagName());
 }

 @Test
 public void testNormalise() {
 Document doc = createDocument();
 doc.appendText("Test ");
 doc.appendText("this ");
 doc.appendText("document");
 Element p = doc.createElement("p");
 p.appendText("Normalised document");
 doc.body().prependChild(p);
 doc.normalise();
 List<Node> nodes = new ArrayList<>();
 for (Node node : doc.body().childNodes()) {
 nodes.add(node);
 }
 Assert.assertEquals(7, nodes.size());
 Assert.assertEquals(Tag.P, nodes.get(0).tagName());
 Assert.assertEquals(Tag.TEXT_NODE, nodes.get(1).tagName());
 Assert.assertEquals("Test ", nodes.get(1).text().trim());
 Assert.assertEquals(Tag.TEXT_NODE, nodes.get(2).tagName());
 Assert.assertEquals("this ", nodes.get(2).text().trim());
 Assert.assertEquals(Tag.TEXT_NODE, nodes.get(3).tagName());
 Assert.assertEquals("document", nodes.get(4).text().trim());
 Assert.assertEquals(Tag.P, nodes.get(5).tagName());
 Assert.assertEquals(Tag.TEXT_NODE, nodes.get(6).tagName());
 Assert.assertEquals("Normalised document", nodes.get(6).text().trim());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 Document doc = new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBaseUri() {
 Document doc = new Document(" ");
 }

 @Test
 public void testOutputSettings() {
 Document doc = createDocument();
 OutputSettings settings = doc.outputSettings();
 Assert.assertEquals(Entities.EscapeMode.base, settings.escapeMode());
 Assert.assertEquals(StandardCharsets.UTF_8(), settings.charset());
 Assert.assertTrue(settings.prettyPrint());
 Assert.assertEquals(1, settings.indentAmount());
 }
}