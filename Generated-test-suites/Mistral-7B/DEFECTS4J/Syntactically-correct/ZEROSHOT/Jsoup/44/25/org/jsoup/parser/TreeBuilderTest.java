import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private Document doc;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 // Initialize the TreeBuilder with a sample input, baseUri and error tracking
 String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
 String baseUri = "http://www.example.com";
 errors = new ParseErrorList();
 treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // Add your testing logic here, e.g., to verify that the correct elements are processed
 return true;
 }
 };
 treeBuilder.initialiseParse(input, baseUri, errors);
 }

 @After
 public void tearDown() {
 doc = null;
 treeBuilder = null;
 }

 @Test
 public void testParse() {
 // Test parsing a valid HTML input
 doc = treeBuilder.parse(input, baseUri);
 assertNotNull(doc);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullInput() {
 // Test parsing a null input
 treeBuilder.parse(null, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullBaseUri() {
 // Test parsing a null baseUri
 treeBuilder.parse(input, null);
 }

 @Test
 public void testRunParser() {
 // Test the runParser method
 treeBuilder.runParser();
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithoutStartTag() {
 // Test processing an end tag without a corresponding start tag
 treeBuilder.processEndTag("html");
 }

 @Test
 public void testProcessStartTag() {
 // Test processing a start tag
 treeBuilder.processStartTag("html");
 Element current = treeBuilder.currentElement();
 assertNotNull(current);
 assertNotNull(current.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 // Test processing a start tag with attributes
 Attributes attrs = new Attributes();
 attrs.put("id", "test");
 treeBuilder.processStartTag("div", attrs);
 Element current = treeBuilder.currentElement();
 assertNotNull(current);
 assertNotNull(current.tagName());
 assertNotNull(current.attributes());
 }

 @Test
 public void testProcessEndTag() {
 // Test processing an end tag
 treeBuilder.processStartTag("div");
 treeBuilder.processEndTag("div");
 current = treeBuilder.currentElement();
 assertNull(current);
 }

}