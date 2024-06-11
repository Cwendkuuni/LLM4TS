import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'></div></body></html>");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 Assert.assertNotNull(w3cDocument);
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'><p>Test</p></div></body></html>");
 Document w3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, w3cDocument);
 Element rootElement = (Element) w3cDocument.getDocumentElement();
 Assert.assertEquals("html", rootElement.getTagName());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><head></head><body><div id='test'></div></body></html>"));
 String result = new W3CDom().asString(w3cDocument);
 Assert.assertNotNull(result);
 }

 @Test
 public void testNamespaces() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html xmlns:foo='http://example.com' xmlns:bar='http://example.org'><head></head><body><div id='test'><p xmlns:baz='http://example.net'>Test</p></div></body></html>");
 Document w3cDocument = new Document();
 new W3CDom().convert(jsoupDocument, w3cDocument);
 Element rootElement = (Element) w3cDocument.getDocumentElement();
 HashMap<String, String> expectedNamespaces = new HashMap<>();
 expectedNamespaces.put("foo", "http://example.com");
 expectedNamespaces.put("bar", "http://example.org");
 expectedNamespaces.put("baz", "http://example.net");
 Stack<HashMap<String, String>> namespacesStack = ((W3CDom) new W3CDom()).namespacesStack;
 assertEquals(expectedNamespaces, namespacesStack.peek());
 }

}