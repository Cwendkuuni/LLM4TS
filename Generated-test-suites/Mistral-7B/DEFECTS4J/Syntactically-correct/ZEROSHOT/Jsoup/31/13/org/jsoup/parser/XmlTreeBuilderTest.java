import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\"/>\n<another>\n<subElement attr3=\"value3\"/>\n</another>\n</root>";
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse(input, null, null);
 assertNotNull(xmlTreeBuilder.doc);
 assertEquals(doc, xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\"/>\n<another>\n<subElement attr3=\"value3\"/>\n</another>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 Token token = xmlTreeBuilder.tokeniser.nextToken();
 Element root = xmlTreeBuilder.process(token);
 assertNotNull(root);
 assertEquals("root", root.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\">\n<another>\n<subElement attr3=\"value3\"/>\n</subElement>\n</another>\n</element>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 Token startTagToken = xmlTreeBuilder.tokeniser.nextToken();
 Token endTagToken = xmlTreeBuilder.tokeniser.nextToken();
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessComment() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<!-- This is a comment -->\n<root>\n<element attr1=\"value1\" attr2=\"value2\"/>\n<another>\n<subElement attr3=\"value3\"/>\n</another>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 Token commentToken = xmlTreeBuilder.tokeniser.nextToken();
 xmlTreeBuilder.process(commentToken);
 assertNotNull(xmlTreeBuilder.doc.body());
 assertEquals(1, xmlTreeBuilder.doc.body().childNodes().size());
 Node commentNode = xmlTreeBuilder.doc.body().child(0);
 assertInstanceOf(Comment.class, commentNode);
 }

 @Test
 public void testProcessCharacter() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\">This is a text node\n</element>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 while (xmlTreeBuilder.tokeniser.nextToken() != null) {
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.getCurrentToken());
 }
 Element root = xmlTreeBuilder.stack.peek();
 TextNode textNode = (TextNode) root.child(0);
 assertNotNull(textNode);
 assertEquals("This is a text node", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/REC-xml10/DTD/xml10.dtd\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\"/>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 Token doctypeToken = xmlTreeBuilder.tokeniser.nextToken();
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.peek();
 assertNotNull(doctypeNode);
 assertEquals("xml", doctypeNode.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<element attr1=\"value1\" attr2=\"value2\"><!-- This is a comment -->\n<another>\n<subElement attr3=\"value3\"/>\n</subElement>\n</another>\n</root>";
 xmlTreeBuilder.initialiseParse(input, null, null);
 Token token = xmlTreeBuilder.tokeniser.nextToken();
 xmlTreeBuilder.process(token);
 }
}