import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'></div></body></html>");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 assertEquals("<html><body><div id='test'></div></body></html>", w3cDocument.getDocumentURI());
 }

 @Test
 public void testFromJsoupWithEmptyDocument() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertNotNull(w3cDocument);
 assertNull(w3cDocument.getDocumentURI());
 }

 @Test
 public void testFromJsoupWithNullDocument() {
 Document w3cDocument = new W3CDom().fromJsoup(null);
 assertNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><div id='test'><span>Test</span></div></body></html>");
 Document w3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, w3cDocument);

 Element root = w3cDocument.getDocumentElement();
 assertNotNull(root);
 assertEquals("html", root.getTagName());

 Element body = (Element) root.getElementsByTagName("body").item(0);
 assertNotNull(body);

 Element div = (Element) body.getElementsByTagName("div").item(0);
 assertNotNull(div);
 assertEquals("div", div.getTagName());

 Element span = (Element) div.getElementsByTagName("span").item(0);
 assertNotNull(span);
 assertEquals("span", span.getTagName());
 assertEquals("Test", span.getTextContent());
 }

 @Test
 public void testConvertWithEmptyDocument() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("");
 Document w3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, w3cDocument);

 assertNull(w3cDocument.getDocumentElement());
 }

 @Test
 public void testConvertWithNullDocument() {
 Document w3cDocument = new Document();
 new W3CDom().convert(null, w3cDocument);

 assertNull(w3cDocument.getDocumentElement());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 w3cDocument.appendChild(w3cDocument.createElement("html"));
 w3cDocument.getElementsByTagName("html").item(0).appendChild(w3cDocument.createElement("body"));
 w3cDocument.getElementsByTagName("body").item(0).appendChild(w3cDocument.createElement("div"));
 w3cDocument.getElementsByTagName("div").item(0).appendChild(w3cDocument.createTextNode("Test"));

 String w3cString = new W3CDom().asString(w3cDocument);
 String expected = "<html><body><div>Test</div></body></html>";
 assertEquals(expected, w3cString);
 }

}