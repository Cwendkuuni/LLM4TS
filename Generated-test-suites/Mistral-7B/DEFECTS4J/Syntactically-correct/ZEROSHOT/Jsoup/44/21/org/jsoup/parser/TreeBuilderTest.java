import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TreeBuilderTest {

 @Test
 public void testParseWithValidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 Document doc = treeBuilder.parse(input, baseUri);

 assertNotNull(doc);
 Element html = doc.body();
 assertNotNull(html);
 Element title = html.head();
 assertNotNull(title);
 Element h1 = html.child(0);
 assertNotNull(h1);
 }

 @Test
 public void testParseWithEmptyInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "";
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 Document doc = treeBuilder.parse(input, baseUri);

 assertNotNull(doc);
 assertNull(doc.body());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = null;
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 treeBuilder.parse(input, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }
 };
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = null;
 ParseErrorList errors = ParseErrorList.noTracking();

 treeBuilder.parse(input, baseUri);
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(Token startTag) {
 Validate.notNull(startTag.name(), "Name of the start tag must not be null");
 return true;
 }
 };
 String input = "<html>";
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 treeBuilder.parse(input, baseUri);

 Element html = treeBuilder.currentElement();
 assertNotNull(html);
 assertNotNull(html.tagName());
 assertNull(html.attributes());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(Token startTag) {
 Validate.notNull(startTag.name(), "Name of the start tag must not be null");
 return true;
 }

 @Override
 protected void processStartTagEnd(Token endTag) {
 Validate.notNull(endTag.name(), "Name of the end tag must not be null");
 }
 };
 String input = "<img src=\"image.jpg\" alt=\"Description\" title=\"Title\" class=\"class-name\" id=\"id-name\">";
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 treeBuilder.parse(input, baseUri);

 Element img = treeBuilder.currentElement();
 assertNotNull(img);
 assertNotNull(img.tagName());
 assertNotNull(img.attributes());
 Attributes attributes = img.attributes();
 assertNotNull(attributes.get("src"));
 assertNotNull(attributes.get("alt"));
 assertNotNull(attributes.get("title"));
 assertNotNull(attributes.get("class"));
 assertNotNull(attributes.get("id"));
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 return true;
 }

 @Override
 protected boolean processStartTag(Token startTag) {
 return true;
 }

 @Override
 protected boolean processEndTag(Token endTag) {
 Validate.notNull(endTag.name(), "Name of the end tag must not be null");
 return true;
 }
 };
 String input = "<html><body><img src=\"image.jpg\"></body></html>";
 String baseUri = "https://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 treeBuilder.parse(input, baseUri);

 Element img = treeBuilder.currentElement();
 assertNotNull(img);
 assertNotNull(img.tagName());
 assertNull(img.attributes());
 treeBuilder.processEndTag("img");
 Element parent = treeBuilder.currentElement();
 assertNotNull(parent);
 assertNull(parent.child(0));
 }
}