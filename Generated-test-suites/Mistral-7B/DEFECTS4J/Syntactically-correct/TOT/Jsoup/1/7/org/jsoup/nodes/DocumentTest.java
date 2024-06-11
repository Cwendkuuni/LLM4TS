import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = Document.createShell("http://example.com");
 assertNotNull(doc);
 assertEquals("#root", doc.tagName());
 }

 @Test
 public void testHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 assertEquals("head", head.tagName());
 }

 @Test
 public void testBodyElement() {
 Element body = doc.body();
 assertNull(body);
 }

 @Test
 public void testSettingTitle() {
 doc.title("Test Title");
 Element title = doc.head().getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testGetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreatingElement() {
 Element link = doc.createElement("a");
 assertEquals("a", link.tagName());
 }

 @Test
 public void testNormaliseDocument() {
 doc.title("Test Title");
 doc.body().appendElement("Test Text");
 doc.normalise();
 Element body = doc.body();
 assertEquals("Test Title\nTest Text", doc.outerHtml());
 }

 @Test
 public void testSettingText() {
 doc.title("Test Title");
 doc.text("Test Text");
 assertEquals("Test Title\nTest Text", doc.outerHtml());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTitle() {
 doc.title(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyTitle() {
 doc.title("");
 }

 @Test
 public void testCreatingDocumentWithBaseUri() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("http://example.com", doc.baseUri());
 }

 @Test
 public void testGetEmptyTitle() {
 assertEquals("", doc.title());
 }

 @Test
 public void testGetHeadAndBodyElements() {
 List<Element> headElements = doc.getElementsByTag("head");
 assertEquals(1, headElements.size());
 List<Element> bodyElements = doc.getElementsByTag("body");
 assertEquals(0, bodyElements.size());
 }

 @Test
 public void testGetTagByName() {
 Element title = doc.createElement("title");
 doc.body().appendChild(title);
 assertEquals("title", doc.getElementsByTag("title").first().tagName());
 }

 @Test
 public void testNormaliseEmptyDocument() {
 Document doc = new Document("http://example.com");
 doc.normalise();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInRoot() {
 doc.text("Test Text");
 doc.normalise();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInHead() {
 doc.head().text("Test Text");
 doc.normalise();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInHtml() {
 Element html = doc.appendElement("html");
 html.text("Test Text");
 doc.normalise();
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInBody() {
 doc.body().text("Test Text");
 doc.normalise();
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleChildren() {
 Element body = doc.body();
 body.appendText("Test Text 1");
 body.appendElement("p").text("Test Text 2");
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleParents() {
 Element p = doc.createElement("p");
 Element div = doc.createElement("div");
 doc.body().appendChild(div);
 div.appendChild(p);
 p.text("Test Text");
 doc.normalise();
 assertEquals("Test Text", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleSiblings() {
 Element p1 = doc.createElement("p");
 Element p2 = doc.createElement("p");
 doc.body().appendChild(p1);
 doc.body().appendChild(p2);
 p1.text("Test Text 1");
 p2.text("Test Text 2");
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodes() {
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode("Test Text 1");
 TextNode t2 = doc.createTextNode("Test Text 2");
 p.appendChild(t1);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesWithBlankNode() {
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode("Test Text 1");
 TextNode t2 = doc.createTextNode(" ");
 TextNode t3 = doc.createTextNode("Test Text 2");
 p.appendChild(t1);
 p.appendChild(t2);
 p.appendChild(t3);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesWithSameText() {
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode("Test Text 1");
 TextNode t2 = doc.createTextNode("Test Text 1");
 p.appendChild(t1);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesWithSameTextInDifferentOrder() {
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode("Test Text 1");
 TextNode t2 = doc.createTextNode("Test Text 1");
 p.appendChild(t2);
 p.appendChild(t1);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithEmptyTextNodes() {
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode(" ");
 TextNode t2 = doc.createTextNode(" ");
 p.appendChild(t1);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInRootAndBody() {
 doc.text("Test Text 1");
 Element p = doc.createElement("p");
 p.text("Test Text 2");
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithEmptyTextNodesInRootAndBody() {
 doc.text(" ");
 Element p = doc.createElement("p");
 TextNode t1 = doc.createTextNode(" ");
 TextNode t2 = doc.createTextNode(" ");
 p.appendChild(t1);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInHeadAndBody() {
 doc.head().text("Test Text 1");
 doc.body().text("Test Text 2");
 doc.normalise();
 assertEquals("Test Text 1Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBody() {
 doc.head().text("Test Text 1 ");
 TextNode t2 = doc.createTextNode("Test Text 2");
 doc.body().appendChild(t2);
 doc.normalise();
 assertEquals("Test Text 1 Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInHeadAndMultipleTextNodesInBody() {
 TextNode t1 = doc.createTextNode("Test Text 1 ");
 doc.head().appendChild(t1);
 Element p = doc.createElement("p");
 TextNode t2 = doc.createTextNode("Test Text 2");
 TextNode t3 = doc.createTextNode("Test Text 3");
 p.appendChild(t2);
 p.appendChild(t3);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1 Test Text 2Test Text 3", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithSameText() {
 TextNode t1 = doc.createTextNode("Test Text 1 ");
 doc.head().appendChild(t1);
 TextNode t2 = doc.createTextNode("Test Text 1");
 TextNode t3 = doc.createTextNode("Test Text 1");
 p.appendChild(t2);
 p.appendChild(t3);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithSameTextInDifferentOrder() {
 TextNode t1 = doc.createTextNode("Test Text 1 ");
 doc.head().appendChild(t1);
 TextNode t2 = doc.createTextNode("Test Text 1");
 TextNode t3 = doc.createTextNode("Test Text 1");
 p.appendChild(t3);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithBlankNodes() {
 TextNode t1 = doc.createTextNode(" Test Text 1 ");
 doc.head().appendChild(t1);
 TextNode t2 = doc.createTextNode(" ");
 TextNode t3 = doc.createTextNode(" Test Text 2 ");
 p.appendChild(t2);
 p.appendChild(t3);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1 Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithBlankNodesInDifferentOrder() {
 TextNode t1 = doc.createTextNode(" Test Text 1 ");
 doc.head().appendChild(t1);
 TextNode t2 = doc.createTextNode(" ");
 TextNode t3 = doc.createTextNode(" Test Text 2 ");
 p.appendChild(t2);
 p.appendChild(t1);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1 Test Text 2", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithTextInBodyBeforeHead() {
 TextNode t1 = doc.createTextNode(" Test Text 1 ");
 TextNode t2 = doc.createTextNode(" Test Text 2 ");
 p.appendChild(t1);
 p.appendChild(t2);
 doc.body().appendChild(p);
 doc.head().text(" Test Text 3 ");
 doc.normalise();
 assertEquals("Test Text 2 Test Text 1 Test Text 3", doc.outerHtml());
 }

 @Test
 public void testNormaliseWithTextInMultipleTextNodesInHeadAndBodyWithTextInHeadBeforeBody() {
 TextNode t1 = doc.createTextNode(" Test Text 1 ");
 doc.head().appendChild(t1);
 TextNode t2 = doc.createTextNode(" Test Text 2 ");
 p.appendChild(t2);
 p.appendChild(t1);
 doc.body().appendChild(p);
 doc.normalise();
 assertEquals("Test Text 1 Test Text 2", doc.outerHtml());
 }
}