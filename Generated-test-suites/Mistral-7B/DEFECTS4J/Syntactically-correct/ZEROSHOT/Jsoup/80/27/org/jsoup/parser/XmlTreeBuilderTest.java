package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlString = "<root><child element=\"value\"><subChild/></child></root>";
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://localhost");
 assertEquals("root", doc.tagName());
 assertNotNull(doc.child(0));
 assertEquals("child", doc.child(0).tagName());
 assertNotNull(doc.child(0).child(0));
 assertEquals("subChild", doc.child(0).child(0).tagName());
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<subChild/>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(1, nodes.size());
 assertNotNull(nodes.get(0));
 assertEquals("subChild", nodes.get(0).tagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 xmlTreeBuilder.parse(new StringReader("invalid input"), "http://localhost");
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.parse("<root><child><subChild/></child></root>", "http://localhost", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("<subChild/>"));
 assertEquals(2, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessToken() {
 Token token = new Token(Token.Type.StartTag, "<root>", null, null, null, null, null);
 assertFalse(xmlTreeBuilder.process(token));
 }
}