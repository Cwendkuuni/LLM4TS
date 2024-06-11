import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<xml><test><subtest/></test></xml>"), "http://test.com");
 }

 @Test
 public void testInitialiseParse() {
 assertNotNull(document);
 assertEquals("http://test.com", document.baseUri());
 }

 @Test
 public void testParse() {
 assertNotNull(document);
 assertNotNull(document.childNodes());
 assertEquals(2, document.childNodes().size());
 assertEquals("xml", document.nodeName());
 }

 @Test
 public void testProcessStartTag() {
 Element root = document.child(0);
 assertEquals("test", root.nodeName());
 Element subtest = root.child(0);
 assertEquals("subtest", subtest.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Element root = document.child(0);
 Element test = root.child(0);
 test.remove();
 assertNull(test);
 }

 @Test
 public void testProcessComment() {
 assertEquals(1, document.childNodes().size());
 Element comment = (Element) document.child(0).child(0);
 assertEquals("", comment.text());
 }

 @Test
 public void testProcessCharacter() {
 assertEquals(1, document.childNodes().size());
 Element root = (Element) document.child(0);
 assertEquals("", root.child(0).text());
 }

 @Test
 public void testProcessDoctype() {
 assertNotNull(document.childNodes());
 assertEquals(1, document.childNodes().size());
 DocumentType doctype = (DocumentType) document.child(0);
 assertNotNull(doctype);
 assertEquals("xml", doctype.nodeName());
 }

 @Test
 public void testPopStackToClose() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader("<test><subtest></subtest></test>"), "http://test.com");
 Element test = doc.child(0);
 Element subtest = test.child(0);
 test.removeChild(0);
 assertNull(subtest);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragment = xmlTreeBuilder.parseFragment("<test><subtest/></test>", "http://test.com", new Parser(xmlTreeBuilder));
 assertEquals(2, fragment.size());
 }

 @Test
 public void testParseFragmentWithContext() {
 Element context = new Element("test");
 List<Node> fragment = xmlTreeBuilder.parseFragment("<subtest/>", context, "http://test.com", new Parser(xmlTreeBuilder));
 assertEquals(1, fragment.size());
 }
}