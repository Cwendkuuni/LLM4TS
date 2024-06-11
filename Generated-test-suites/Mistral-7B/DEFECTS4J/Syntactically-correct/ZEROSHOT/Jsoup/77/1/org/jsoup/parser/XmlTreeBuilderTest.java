package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.Helper.*;
import org.jsoup.nodes.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.StringReader;
import java.util.List;

/**
 * Test class for XmlTreeBuilder.
 *
 * @author YourName
 */
class XmlTreeBuilderTest {

 @BeforeEach
 void setUp() {
 // Initialize any required test data or prepare test environment
 }

 @AfterEach
 void tearDown() {
 // Clean up test environment or release test resources
 }

 @Test
 void testParse() {
 // Test method parse
 String xmlString = "<root><child1>Hello World</child1><child2><grandChild>Test</grandChild></child2></root>";
 Document doc = XmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 List<Node> children = root.children();
 assertEquals(2, children.size());
 for (int i = 0; i < children.size(); i++) {
 Node child = children.get(i);
 if (child instanceof Element) {
 Element element = (Element) child;
 String tagName = element.tagName();
 switch (i) {
 case 0:
 assertEquals("child1", tagName);
 break;
 case 1:
 assertEquals("child2", tagName);
 assertNotNull(element.getChild(0));
 assertEquals("grandChild", element.getChild(0).tagName());
 break;
 }
 }
 }
}

 @Test
 void testProcessStartTag() {
 // Test method process
 String xmlString = "<root><child1>Hello World</child1><child2></child2></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", "baseUri");
 Element child1 = new Element("child1", "baseUri");
 Element child2 = new Element("child2", "baseUri");
 xmlTreeBuilder.doc = root;
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.settings = new ParseSettings();
 xmlTreeBuilder.processStartTag(new Token.StartTag("child1"));
 assertEquals(child1, xmlTreeBuilder.stack.peek());
}

 @Test
 void testProcessEndTag() {
 // Test method process
 String xmlString = "<root><child1><child2></child2></child1></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", "baseUri");
 Element child1 = new Element("child1", "baseUri");
 Element child2 = new Element("child2", "baseUri");
 xmlTreeBuilder.doc = root;
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.stack.add(child1);
 xmlTreeBuilder.settings = new ParseSettings();
 xmlTreeBuilder.processEndTag(new Token.EndTag("child2"));
 assertEquals(child1, xmlTreeBuilder.stack.peek());
}

 @Test
 void testProcessComment() {
 // Test method process
 String xmlString = "<!-- This is a comment -->";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Element root = new Element("root", "baseUri");
 xmlTreeBuilder.doc = root;
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.settings = new ParseSettings();
 xmlTreeBuilder.processComment(new Token.Comment("This is a comment"));
 assertNotNull(xmlTreeBuilder.stack.peek().getElementsByTag("comment").get(0));
}

 @Test
 void testParseFragment() {
 // Test method parseFragment
 String xmlFragment = "<child1><child2><grandChild>Test</grandChild></child2></child1>";
 List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "baseUri", new ParseErrorList(), new ParseSettings());
 Element child1 = (Element) nodes.get(0);
 Element child2 = (Element) nodes.get(1);
 Element grandChild = (Element) child2.getChild(0);
 assertEquals("child1", child1.tagName());
 assertEquals("child2", child2.tagName());
 assertEquals("grandChild", grandChild.tagName());
 assertEquals("Test", grandChild.text());
}
}