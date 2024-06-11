import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

 private Document createJsoupDocument() {
 String html = "<html><head><title>Test Title</title></head>" +
 "<body><h1 id='test'>Test Heading</h1>" +
 "<p id='text'>Test Text</p></body></html>";
 Document jsoupDocument = Jsoup.parse(html);
 return jsoupDocument;
 }

 @Test
 public void testFromJsoup() throws Exception {
 Document jsoupDocument = createJsoupDocument();
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);

 // Assert w3cDocument is valid
 assertNotNull(w3cDocument);
 String w3cXml = w3cdom.asString(w3cDocument);
 System.out.println("w3cDocument as String: \n" + w3cXml);
 }

 @Test
 public void testConvert() throws Exception {
 Document jsoupDocument = createJsoupDocument();
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.newDocument(); // create empty w3c document
 w3cdom.convert(jsoupDocument, w3cDocument);

 // Assert w3cDocument is equal to jsoupDocument
 Document convertedJsoupDocument = w3cdom.fromJsoup(w3cDocument);
 assertEquals(jsoupDocument, convertedJsoupDocument);
 }

 @Test
 public void testNamespaces() throws Exception {
 Document jsoupDocument = Jsoup.parse("<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:my='http://www.example.com/my'><body></body></html>");
 W3CDom w3cdom = new W3CDom();
 Document w3cDocument = w3cdom.fromJsoup(jsoupDocument);
 String namespacePrefix = "my";
 String expectedNamespaceUri = "http://www.example.com/my";

 // Assert namespaces are correctly mapped
 assertEquals(expectedNamespaceUri, w3cdom.getNamespaceUri(w3cDocument, namespacePrefix));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullDocument() {
 W3CDom w3cdom = new W3CDom();
 try {
 w3cdom.fromJsoup(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}