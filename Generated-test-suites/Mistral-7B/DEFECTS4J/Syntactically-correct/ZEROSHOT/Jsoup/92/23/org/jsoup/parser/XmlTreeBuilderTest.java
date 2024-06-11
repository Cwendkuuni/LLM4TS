import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private Parser parser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 parser = new Parser(xmlTreeBuilder);
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child><subchild><data>Hello World</data></subchild></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "");
 assertEquals("root", doc.tagName());

 Element root = doc.select("root").first();
 assertNotNull(root);
 assertEquals(1, root.childrenSize());

 Element child = root.select("child").first();
 assertNotNull(child);
 assertEquals(1, child.childrenSize());

 Element subchild = child.select("subchild").first();
 assertNotNull(subchild);
 assertEquals(1, subchild.childrenSize());

 TextNode data = (TextNode) subchild.child(0);
 assertNotNull(data);
 assertEquals("Hello World", data.text());
 }

 @Test
 public void testParseWithBaseUri() {
 String xmlString = "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><child xsi:type=\"xsd:string\"><subchild><data>Hello World</data></subchild></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://example.com");
 assertEquals("root", doc.tagName());

 Element root = doc.select("root").first();
 assertNotNull(root);
 assertEquals(1, root.childrenSize());

 Element child = root.select("child").first();
 assertNotNull(child);
 assertEquals("http://example.com/child", child.attribute("xsi:type"));
 assertEquals(1, child.childrenSize());

 Element subchild = child.select("subchild").first();
 assertNotNull(subchild);
 assertEquals(1, subchild.childrenSize());

 TextNode data = (TextNode) subchild.child(0);
 assertNotNull(data);
 assertEquals("Hello World", data.text());
 }

 @Test
 public void testProcessStartTag() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><child><subchild><data>Hello World</data></subchild></child></root>"));
 Token token;
 while ((token = tokenizer.next()) != null) {
 xmlTreeBuilder.process(token);
 }

 Element root = xmlTreeBuilder.doc.select("root").first();
 Element child = root.select("child").first();
 Element subchild = child.select("subchild").first();
 TextNode data = (TextNode) subchild.child(0);

 assertEquals(4, xmlTreeBuilder.stack.size());
 assertEquals("root", root.tagName());
 assertEquals("child", child.tagName());
 assertEquals("subchild", subchild.tagName());
 assertEquals("data", data.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><child><subchild><data>Hello World</data></subchild></child></root>"));
 Token token;
 while ((token = tokenizer.next()) != null) {
 xmlTreeBuilder.process(token);
 }

 assertEquals(3, xmlTreeBuilder.stack.size());
 xmlTreeBuilder.popStackToClose(tokenizer.next().asEndTag());
 assertEquals(2, xmlTreeBuilder.stack.size());
 xmlTreeBuilder.popStackToClose(tokenizer.next().asEndTag());
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root><!--comment--></root>"));
 Token token;
 while ((token = tokenizer.next()) != null) {
 xmlTreeBuilder.process(token);
 }

 assertEquals(1, xmlTreeBuilder.stack.size());
 assertNull(xmlTreeBuilder.doc.childNodes().get(0));
 }

 @Test
 public void testProcessCharacter() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<root>abc</root>"));
 Token token;
 while ((token = tokenizer.next()) != null) {
 xmlTreeBuilder.process(token);
 }

 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(3, xmlTreeBuilder.doc.childNodes().size());
 TextNode firstChild = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("a", firstChild.text());
 TextNode secondChild = (TextNode) xmlTreeBuilder.doc.childNodes().get(1);
 assertEquals("b", secondChild.text());
 TextNode thirdChild = (TextNode) xmlTreeBuilder.doc.childNodes().get(2);
 assertEquals("", thirdChild.text());
 }

 @Test
 public void testProcessDoctype() {
 Tokenizer tokenizer = new Tokenizer(new StringReader("<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED> ]>"));
 Token token;
 while ((token = tokenizer.next()) != null) {
 xmlTreeBuilder.process(token);
 }

 DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("root", doctype.nodeName());
 assertEquals("root", doctype.tagName());
 assertEquals("CDATA", doctype.type());
 assertEquals(1, doctype.attributes().size());
 assertEquals("attr", doctype.attribute(0).name());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<subchild><data>Hello World</data></subchild>";
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, "", "");
 assertEquals(2, fragmentNodes.size());
 TextNode data = (TextNode) fragmentNodes.get(1);
 assertEquals("Hello World", data.text());
 }

 @Test
 public void testParseFragmentWithContext() {
 String xmlFragment = "<subchild><data>Hello World</data></subchild>";
 Element context = Jsoup.parse("<root/>").select("root").first();
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(xmlFragment, context, "", new Parser(xmlTreeBuilder));
 assertEquals(1, context.childrenSize());
 TextNode data = (TextNode) context.child(0).child(0);
 assertEquals("Hello World", data.text());
 }
}