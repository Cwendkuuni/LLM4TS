import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.helper.Validate;

import java.io.StringReader;
import java.util.List;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parse(new StringReader("<root><child><grandchild/></child></root>"), "baseUrl");
 }

 @Test
 public void testParse() {
 // Test parsing of a valid XML document
 assertNotNull(document);
 assertEquals("root", document.tagName());

 // Test root element
 Element root = document.child(0);
 assertEquals("root", root.tagName());

 // Test child element
 Element child = root.child(0);
 assertEquals("child", child.tagName());

 // Test grandchild element
 Element grandchild = child.child(0);
 assertNotNull(grandchild);
 assertEquals("grandchild", grandchild.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 // Test parsing of invalid input
 XmlTreeBuilder builder = new XmlTreeBuilder();
 String invalidInput = "<html>";
 try {
 builder.parse(new StringReader(invalidInput), "baseUrl");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseFragment() {
 // Test parsing of a fragment
 String fragment = "<child><grandchild/></child>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, document, "baseUrl", new Parser(xmlTreeBuilder));
 assertNotNull(nodes);
 assertEquals(2, nodes.size());

 // Test child element
 Element child = (Element) nodes.get(0);
 assertEquals("child", child.tagName());

 // Test grandchild element
 Element grandchild = (Element) nodes.get(1);
 assertEquals("grandchild", grandchild.tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentInvalidInput() {
 // Test parsing of invalid fragment input
 String invalidFragment = "<html>";
 try {
 xmlTreeBuilder.parseFragment(invalidFragment, document, "baseUrl", new Parser(xmlTreeBuilder));
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testInsertCharacter() {
 // Test character insertion
 String characterInput = "<root><child>&amp;<grandchild/></child></root>";
 document = xmlTreeBuilder.parse(new StringReader(characterInput), "baseUrl");
 TextNode textNode = (TextNode) document.child(0).child(0).child(0);
 assertEquals("&amp;", textNode.text());
 }

 @Test
 public void testInsertComment() {
 // Test comment insertion
 String commentInput = "<root><!-- This is a comment --><child><grandchild/></child></root>";
 document = xmlTreeBuilder.parse(new StringReader(commentInput), "baseUrl");
 Element commentElement = (Element) document.child(0).child(0);
 assertEquals("This is a comment", commentElement.text());
 }

 @Test
 public void testInsertDocumentType() {
 // Test document type insertion
 String documentTypeInput = "<!DOCTYPE root SYSTEM \"root.dtd\">";
 document = xmlTreeBuilder.parse(new StringReader(documentTypeInput), "baseUrl");
 DocumentType documentTypeNode = (DocumentType) document.child(0);
 assertEquals("root", documentTypeNode.nodeName());
 assertEquals("root.dtd", documentTypeNode.getSystemIdentifier());
 }

 @Test
 public void testInsertSelfClosingTag() {
 // Test self-closing tag insertion
 String selfClosingTagInput = "<root><selfClosing/></root>";
 document = xmlTreeBuilder.parse(new StringReader(selfClosingTagInput), "baseUrl");
 Element selfClosingTagElement = (Element) document.child(0);
 assertEquals("selfClosing", selfClosingTagElement.tagName());
 assertTrue(selfClosingTagElement.hasAttr("xmlns:xml"));
 assertEquals("http://www.w3.org/2000/xmlns/", selfClosingTagElement.attr("xmlns:xml"));
 assertEquals("http://www.w3.org/2000/xml", selfClosingTagElement.attr("xmlns"));
 }
}