import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.util.EmptyStackException;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokenizer;
 private Document document;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokenizer = new Tokenizer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <element attr1=\"value1\" attr2=\"value2\">\n <subElement attr3=\"value3\"/>\n </element>\n</root>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), null, null);
 document = xmlTreeBuilder.document();
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", document.tagName());
 assertNotNull(document.head());
 assertNull(document.body());
 }

 @Test
 public void testProcessStartTag() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 Element root = document.elementSibling(0);
 Element element = root.child(0);
 assertNotNull(element);
 assertEquals("element", element.tagName());
 assertEquals("value1", element.attr("attr1"));
 assertEquals("value2", element.attr("attr2"));
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 Element root = document.elementSibling(0);
 Element element = root.child(0);
 assertNotNull(element);
 assertEquals("element", element.tagName());
 assertTrue(element.tagName().isSelfClosing());
 }

 @Test
 public void testProcessEndTag() {
 Element root = document.elementSibling(0);
 Element element = root.child(0);
 Element subElement = element.child(0);
 tokenizer.nextToken(); // consume <subElement> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 assertNull(subElement.parent());
 }

 @Test
 public void testProcessComment() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 Comment comment = document.child(1);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", comment.data());
 }

 @Test
 public void testProcessCharacter() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 TextNode textNode = document.child(3);
 assertEquals(" ", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token token = tokenizer.nextToken();
 xmlTreeBuilder.process(token);
 DocumentType documentType = document.child(0);
 assertEquals("xml", documentType.name());
 assertNull(documentType.publicId());
 assertNull(documentType.systemId());
 }

 @Test
 public void testProcessUnknownTag() {
 xmlTreeBuilder.tokenizer.input("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <unknownTag attr1=\"value1\" attr2=\"value2\"/>\n</root>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), null, null);
 Document document = xmlTreeBuilder.document();
 Element root = document.elementSibling(0);
 Element unknownTag = root.child(0);
 assertNotNull(unknownTag);
 assertTrue(unknownTag.tagName().isUnknown());
 }

 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder.tokenizer.input("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <element>\n <subElement>\n <subElement>\n </subElement>\n </subElement>\n </element>\n</root>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), null, null);
 xmlTreebuilder.process(tokenizer.nextToken()); // consume <root> tag
 xmlTreebuilder.process(tokenizer.nextToken()); // consume <?xml version=\"1.0\" encoding=\"UTF-8\"?> tag
 Element root = document.elementSibling(0);
 Element element = root.child(0);
 Element subElement1 = element.child(0);
 Element subElement2 = subElement1.child(0);
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 assertEquals(1, stack.size());
 assertNotNull(subElement1.parent());
 assertNull(subElement2.parent());
 }

 @Test(expected = EmptyStackException.class)
 public void testPopStackToCloseNoMatchingTag() {
 xmlTreeBuilder.tokenizer.input("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <element>\n <subElement>\n </root>\n </subElement>\n </element>\n");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), null, null);
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <root> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <?xml version=\"1.0\" encoding=\"UTF-8\"?> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <element> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <subElement> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </element> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </root> tag
 xmlTreeBuilder.popStackToClose(new Token.EndTag("subElement2"));
 }

 @Test
 public void testPopStackToCloseMatchingTagMultipleOccurrences() {
 xmlTreeBuilder.tokenizer.input("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n <element>\n <subElement>\n <subElement>\n <subElement>\n </subElement>\n </subElement>\n </subElement>\n </element>\n</root>");
 xmlTreeBuilder.initialiseParse(tokenizer.input(), null, null);
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <root> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume <?xml version=\"1.0\" encoding=\"UTF-8\"?> tag
 Element root = document.elementSibling(0);
 Element element = root.child(0);
 Element subElement1 = element.child(0);
 Element subElement2 = subElement1.child(0);
 Element subElement3 = subElement2.child(0);
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 xmlTreeBuilder.process(tokenizer.nextToken()); // consume </subElement> tag
 assertEquals(1, stack.size());
 assertNotNull(subElement1.parent());
 assertNull(subElement2.parent());
 assertNull(subElement3.parent());
 }
}