import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testXmlTreeBuilder_ParsesValidXmlDocument() throws IOException {
 String xml = "<root><element attr1=\"value1\">Content</element></root>";
 Document doc = XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList());

 Element root = doc.getRootElement();
 assertEquals("root", root.nodeName());
 assertEquals(1, root.childNodes().size());
 Element element = root.childNodes().get(0);
 assertEquals("element", element.nodeName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testXmlTreeBuilder_HandlesEmptyXmlDocument() throws IOException {
 String xml = "<root/>";
 Document doc = XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList());

 Element root = doc.getRootElement();
 assertEquals("root", root.nodeName());
 assertTrue(root.childNodes().isEmpty());
 }

 @Test
 public void testXmlTreeBuilder_HandlesXmlDeclaration() throws IOException {
 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><element attr1=\"value1\">Content</element></root>";
 Document doc = XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList());

 Element xmlDeclaration = doc.childNodes().get(0);
 assertTrue(xmlDeclaration instanceof XmlDeclaration);
 assertEquals("xml", xmlDeclaration.nodeName());
 assertEquals("1.0", xmlDeclaration.attr("version"));
 assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
 }

 @Test
 public void testXmlTreeBuilder_HandlesSelfClosingTags() throws IOException {
 String xml = "<root><img src=\"image.jpg\" /> </root>";
 Document doc = XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList());

 Element img = doc.getElementsByTag("img").get(0);
 assertNotNull(img);
 assertTrue(img.hasAttr("src"));
 assertEquals("image.jpg", img.attr("src"));
 }

 @Test
 public void testXmlTreeBuilder_HandlesUnknownTags() throws IOException {
 String xml = "<root><unknown tag=\"attribute\" /> </root>";
 Document doc = XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList());

 Element unknownTag = doc.getElementsByTag("unknown").get(0);
 assertNotNull(unknownTag);
 assertTrue(unknownTag.nodeName().equalsIgnoreCase("unknown"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testXmlTreeBuilder_ThrowsExceptionForInvalidInput() throws IOException {
 String xml = "<root><element attr1=\"value1\">Content<script>alert('Hello')</script></element></root>";
 XmlTreeBuilder.parseFragment(xml, null, new ParseErrorList()); // Exception expected
 }
}