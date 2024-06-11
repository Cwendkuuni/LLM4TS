import org.junit.Test;
import org.junit.Assert.*;
import org.w3c.dom.*;
import org.jsoup.nodes.Document;
import org.jsoup.helper.W3CDom;

public class W3CDomTest {

 @Test
 public void testFromJsoup() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

 Assert.assertNotNull(w3cDocument);
 Assert.assertEquals("<html><head></head><body><h1 xmlns:jsoup='http://jsoup.org/' id='title'>Test</h1></body></html>", w3cDocument.getDocumentElement().getTagName());
 }

 @Test
 public void testConvert() {
 Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Test</h1><p class='test'>Test paragraph</p></body></html>");
 Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

 W3CDom w3CDom = new W3CDom();
 w3CDom.convert(jsoupDocument, w3cDocument);

 Assert.assertEquals("<html><head></head><body><h1 xmlns:jsoup='http://jsoup.org/' id='title'>Test</h1><p class='test'>Test paragraph</p></body></html>", w3cDocument.getDocumentElement().getTagName());
 }

 @Test
 public void testAsString() {
 Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
 w3cDocument.appendChild(w3cDocument.createElement("html"));
 w3cDocument.getElementsByTagName("html").item(0).appendChild(w3cDocument.createElement("body"));
 w3cDocument.getElementsByTagName("body").item(0).appendChild(w3cDocument.createElement("h1"));
 w3cDocument.getElementsByTagName("h1").item(0).setAttribute("id", "title");
 w3cDocument.getElementsByTagName("h1").item(0).setTextContent("Test");

 Document w3cDocumentWithJsoup = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1 id='title'>Test</h1></body></html>"));
 W3CDom w3CDom = new W3CDom();
 w3CDom.convert(w3cDocumentWithJsoup, w3cDocument);

 String jsoupString = new W3CDom().asString(w3cDocumentWithJsoup);
 String w3cString = new W3CDom().asString(w3cDocument);

 Assert.assertEquals(jsoupString, w3cString);
 }
}