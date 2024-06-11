import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class XmlTreeBuilderTest {

 @BeforeEach
 public void setUp() {
 }

 @AfterEach
 public void tearDown() {
 }

 @Test
 public void testParseFragment() throws IOException {
 // Given: a valid XML fragment to parse
 String inputFragment = "<root><element attr=\"value\">Content</element></root>";
 // When: parsing the XML fragment using XmlTreeBuilder
 Document xmlDoc = Jsoup.parse(inputFragment, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = (XmlTreeBuilder) xmlDoc.parser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 // Then: the parsed XML fragment should contain the expected nodes
 Assertions.assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Assertions.assertEquals("root", root.nodeName());
 Element element = (Element) nodes.get(1);
 Assertions.assertEquals("element", element.nodeName());
 Assertions.assertEquals("value", element.attr("attr"));
 Assertions.assertEquals("Content", element.text());
 }

 @Test
 public void testParseFragmentWithUnknownTag() throws IOException {
 // Given: an XML fragment with an unknown tag to parse
 String inputFragment = "<root><unknownTag attr=\"value\">Content</unknownTag></root>";
 // When: parsing the XML fragment using XmlTreeBuilder
 Document xmlDoc = Jsoup.parse(inputFragment, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = (XmlTreeBuilder) xmlDoc.parser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 // Then: the parsed XML fragment should contain the expected nodes (with the tag treated as self-closing)
 Assertions.assertEquals(2, nodes.size());
 Element root = (Element) nodes.get(0);
 Assertions.assertEquals("root", root.nodeName());
 Element unknownTag = (Element) nodes.get(1);
 Assertions.assertEquals("unknownTag", unknownTag.nodeName());
 Assertions.assertTrue(unknownTag.isSelfClosing());
 Assertions.assertEquals("value", unknownTag.attr("attr"));
 Assertions.assertNull(unknownTag.text());
 }

 @Test
 public void testParseFragmentWithBogusComment() throws IOException {
 // Given: an XML fragment with a bogus comment to parse
 String inputFragment = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
 // When: parsing the XML fragment using XmlTreeBuilder
 Document xmlDoc = Jsoup.parse(inputFragment, Parser.xmlParser());
 XmlTreeBuilder xmlTreeBuilder = (XmlTreeBuilder) xmlDoc.parser();
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", new ParseErrorList());
 // Then: the parsed XML fragment should contain the expected nodes (with the bogus comment parsed as an XML declaration)
 Assertions.assertEquals(1, nodes.size());
 DocumentType doctypeNode = (DocumentType) nodes.get(0);
 Assertions.assertEquals("html", doctypeNode.nodeName());
 Assertions.assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", doctypeNode.publicId());
 Assertions.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", doctypeNode.systemId());
 }

 @Test
 public void testParseFragmentWithError() throws IOException {
 // Given: an XML fragment with an error to parse
 String inputFragment = "<root><element attr=\"value\">Content<illegalCharacter>";
 // When: parsing the XML fragment using XmlTreeBuilder
 Executable parseFragment = () -> Jsoup.parse(inputFragment, Parser.xmlParser());
 // Then: an exception should be thrown
 Assertions.assertThrows(IllegalArgumentException.class, parseFragment);
 }
}