import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class W3CDomTest {

 @BeforeAll
 static void setup() {
 factory = DocumentBuilderFactory.newInstance();
 }

 @TestFactory
 public DynamicNode testFromJsoup() {
 return DynamicContainer.of(
 TestCase1::new,
 TestCase2::new,
 TestCase3::new);
 }

 static class TestCase1 {
 org.jsoup.nodes.Document jsoupDoc;
 Document w3cDoc;

 @BeforeEach
 void setup() {
 jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello, World!</h1></body></html>");
 try {
 w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 void testBasicConversion() {
 assertNotNull(w3cDoc);
 assertEquals(jsoupDoc.tagName(), w3cDoc.getDocumentElement().getTagName());
 assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
 assertEquals("Hello, World!", w3cDoc.getDocumentElement().getFirstChild().getNodeValue());
 }
 }

 static class TestCase2 {
 org.jsoup.nodes.Document jsoupDoc;
 Document w3cDoc;

 @BeforeEach
 void setup() {
 jsoupDoc = org.jsoup.Jsoup.parse("<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">" +
 "  <html:head profile=\"http://www.w3.org/1999/xhtml/vocab\" xml:base=\"\">" +
 "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>" +
 "  </html:head>" +
 "  <html:body>" +
 "    <h1 class=\"title\">Test</h1>" +
 "    <p class=\"content\">This is a test.</p>" +
 "  </html:body>" +
 " </html>");
 try {
 w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 void testNamespaces() {
 assertNotNull(w3cDoc);
 assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 String htmlNamespace = w3cDoc.getDocumentElement().getNamespaceURI();
 assertTrue(htmlNamespace.startsWith("http://www.w3.org/1999/xhtml"));
 Element title = (Element) w3cDoc.getDocumentElement().getElementsByTagNameNS(htmlNamespace, "html:h1")
 .item(0);
 assertEquals("title", title.getTagName());
 assertEquals("Test", title.getTextContent());
 }
 }

 static class TestCase3 {
 org.jsoup.nodes.Document jsoupDoc;
 Document w3cDoc;

 @BeforeEach
 void setup() {
 jsoupDoc = org.jsoup.Jsoup.parse("<html><body><h1>Hello, <script>World!</script></h1></body></html>");
 try {
 w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 } catch (ParserConfigurationException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 void testScriptTag() {
 assertNotNull(w3cDoc);
 assertEquals(1, w3cDoc.getDocumentElement().getChildNodes().getLength());
 Element htmlEl = w3cDoc.getDocumentElement();
 assertEquals(1, htmlEl.getChildNodes().getLength());
 Element bodyEl = (Element) htmlEl.getChildNodes().item(0);
 assertEquals(1, bodyEl.getChildNodes().getLength());
 Element h1El = (Element) bodyEl.getChildNodes().item(0);
 assertEquals("Hello, ", h1El.getTextContent());
 // Assertions.assertThrows() not supported in JUnit 4
 Executable scriptRun = () -> {
 String xml = new W3CDom().asString(w3cDoc);
 };
 assertTrue(scriptRun.getClass().getName().contains("NoScriptException"));
 }
 }

 DocumentBuilderFactory factory;
}