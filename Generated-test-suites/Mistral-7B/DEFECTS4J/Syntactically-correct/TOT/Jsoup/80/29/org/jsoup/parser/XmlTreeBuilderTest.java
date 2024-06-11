import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParseValidXml() {
 String xml = "<root><element attr=\"value\">Content</element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = document.getRootElement();
 Validate.isTrue(root.tagName().equals("root"));
 Validate.isTrue(root.childNodeSize() == 1);
 Element element = root.child(0);
 Validate.isTrue(element.tagName().equals("element"));
 Validate.isTrue(element.text().equals("Content"));
 Validate.isTrue(element.attribute("attr").equals("value"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 xmlTreeBuilder.parse(null, "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptyInput() {
 xmlTreeBuilder.parse(new StringReader(""), "http://test.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidXml() {
 String xml = "<root<element>";
 Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Validate.fail("Expected an exception, but no exception was thrown.");
 }

 @Test
 public void testParseWithComments() {
 String xml = "<root><!-- comment --><element attr=\"value\">Content</element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = document.getRootElement();
 Validate.isTrue(root.tagName().equals("root"));
 Validate.isTrue(root.childNodeSize() == 1);
 Element element = root.child(0);
 Validate.isTrue(element.tagName().equals("element"));
 Validate.isTrue(element.text().equals("Content"));
 Validate.isTrue(element.attribute("attr").equals("value"));
 Comment comment = element.child(0);
 Validate.isInstanceOf(comment.getClass(), Comment.class);
 Validate.isTrue(comment.getText().equals(" comment "));
 }

 @Test
 public void testParseWithCDataSection() {
 String xml = "<root><![CDATA[ <html><head><title>Test</title></head><body><h1>Hello</h1></body></html> ]]></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = document.getRootElement();
 Validate.isTrue(root.tagName().equals("root"));
 Validate.isTrue(root.childNodeSize() == 1);
 Element cdata = root.child(0);
 Validate.isInstanceOf(cdata.getClass(), CDataNode.class);
 Validate.isTrue(cdata.text().equals("<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>"));
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element attr=\"value\">Content</element>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element element = (Element) nodes.get(0);
 Validate.isTrue(element.tagName().equals("element"));
 Validate.isTrue(element.text().equals("Content"));
 Validate.isTrue(element.attribute("attr").equals("value"));
 }

 @Test
 public void testParseWithDoctype() {
 String xml = "<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED> ]><root><element attr=\"value\">Content</element></root>";
 Document document = xmlTreeBuilder.parse(new StringReader(xml), "http://test.com");
 Element root = document.getRootElement();
 DocumentType doctype = root.document().doctype();
 Validate.isTrue(doctype.name().equals("root"));
 Validate.isTrue(doctype.publicId() == null);
 Validate.isTrue(doctype.systemId() == null);
 Element element = root.child(0);
 Validate.isTrue(element.tagName().equals("element"));
 Validate.isTrue(element.text().equals("Content"));
 Validate.isTrue(element.attribute("attr").equals("value"));
 }
}