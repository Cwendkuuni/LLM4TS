package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.Validate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;

@RunWith(org.junit.runner.JUnit4.class)
public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration decl = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", decl.nodeName());
 assertEquals("xml", decl.name());
 assertNull(decl.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationCreationWithProcessingInstruction() {
 XmlDeclaration decl = new XmlDeclaration("<?xml", null, true);
 assertEquals("#declaration", decl.nodeName());
 assertEquals("<?xml", decl.name());
 assertNull(decl.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationCreationWithAttributes() {
 XmlDeclaration decl = new XmlDeclaration("xml", null, false);
 Attributes attr = mock(Attributes.class);
 doReturn("1.0").when(attr).get("version");
 doReturn("UTF-8").when(attr).get("encoding");
 decl.attributes = attr;
 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 assertEquals(expected, decl.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationNullName() {
 Validate.notNull(new XmlDeclaration(null, "http://www.example.com", false));
 }

 @Test
 public void testXmlDeclarationToString() throws IOException {
 XmlDeclaration decl = new XmlDeclaration("xml", null, false);
 Element parent = mock(Element.class);
 Document doc = mock(Document.class);
 doReturn(doc).when(parent).ownerDocument();
 decl.appendChild(new TextNode(""));
 decl.parent = parent;
 String expected = "<declaration>\n</declaration>";
 assertEquals(expected, decl.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 XmlDeclaration decl = new XmlDeclaration("xml", null, false);
 Appendable accum = mock(Appendable.class);
 Document.OutputSettings out = mock(Document.OutputSettings.class);
 decl.outerHtmlHead(accum, 0, out);
 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">\n";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 XmlDeclaration decl = new XmlDeclaration("xml", null, false);
 Appendable accum = mock(Appendable.class);
 Document.OutputSettings out = mock(Document.OutputSettings.class);
 decl.outerHtmlTail(accum, 0, out);
 assertEquals("", accum.toString());
 }
}