import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

 private Document doc;
 private XmlTreeBuilder parser;

 @Before
 public void setUp() {
 parser = new XmlTreeBuilder();
 }

 @After
 public void tearDown() {
 if (doc != null) {
 doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 System.out.println(doc.html());
 doc = null;
 }
 parser = null;
 }

 @Test
 public void testParseSimpleXml() {
 String xmlInput = "<root><element>Hello World</element></root>";
 doc = parser.parse(new StringReader(xmlInput), "");
 assertElementExists("root", doc);
 assertElementText("element", doc, "Hello World");
 }

 @Test
 public void testParseComplexXmlWithComments() {
 String xmlInput = "<root><!--comment-->\n<element>\n<subElement>\n<text>Hello World</text>\n</subElement>\n</element>\n</root>";
 doc = parser.parse(new StringReader(xmlInput), "");
 assertElementExists("root", doc);
 assertElementText("subElement/text", doc, "Hello World");
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXml() {
 String xmlInput = "<root><!--comment-->\n<element>\n<text>Hello World</text>\n</root>"; // missing > in root tag
 parser.parse(new StringReader(xmlInput), "");
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<element><subElement>Fragment Test</subElement></element>";
 List<Node> nodes = parser.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNodesCount(3, nodes);
 assertElementText("element/subElement", doc, "Fragment Test");
 }

 private void assertElementExists(String tagName, Document doc) {
 Element element = doc.select(tagName).first();
 assertNotNull(element);
 }

 private void assertElementText(String elementPath, Document doc, String text) {
 String actualText = doc.select(elementPath).text();
 assertEquals(text, actualText);
 }

 private void assertNodesCount(int expectedCount, List<Node> nodes) {
 int actualCount = nodes.size();
 assertEquals(expectedCount, actualCount);
 }

 private void assertNotNull(Object obj) {
 if (obj == null) {
 throw new AssertionError("Expected object to not be null");
 }
 }
}