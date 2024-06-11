import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import org.junit.Assert;

public class TreeBuilderTest {

 @Test
 public void testParseWithValidInput() {
 String input = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new HtmlTreeBuilder(); // assume HtmlTreeBuilder is the concrete implementation of TreeBuilder
 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 Assert.assertNotNull(doc);
 Assert.assertEquals("Test", doc.title().text());
 Assert.assertEquals("Hello World", doc.body().select("h1").text());
 }

 @Test
 public void testParseWithEmptyInput() {
 String input = "";
 String baseUri = "http://example.com";
 ParseErrorList errors = ParseErrorList.noTracking();

 TreeBuilder builder = new HtmlTreeBuilder();
 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 Assert.assertNotNull(doc);
 Assert.assertTrue(doc.body().select("*").isEmpty());
 }

 @Test
 public void testParseWithInvalidInput() {
 String input = "<html<title>Test</title>";
 String baseUri = "http://example.com";
 ParseErrorList errors = new ParseErrorList();

 TreeBuilder builder = new HtmlTreeBuilder();
 builder.initialiseParse(input, baseUri, errors);
 Document doc = builder.parse(input, baseUri);

 Assert.assertNull(doc);
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Error parsing start tag : <html<title>", errors.get(0).message());
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder builder = new HtmlTreeBuilder();
 builder.initialiseParse("", "", ParseErrorList.noTracking());

 Token token = new Token.StartTag();
 token.name("html");

 Element element = new Element("html");
 element.attributes(new Attributes(){{
 put("id", "root");
 }});

 builder.stack.add(element);

 boolean result = builder.processStartTag(token);

 Assert.assertTrue(result);
 Assert.assertEquals("html", currentElement(builder).tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder builder = new HtmlTreeBuilder();
 builder.initialiseParse("", "", ParseErrorList.noTracking());

 Token token = new Token.StartTag();
 token.name("div");
 Attributes attributes = new Attributes(){{
 put("class", "myClass");
 put("id", "myId");
 put("style", "color: red;");
 put("title", "myTitle");
 }};
 token.attributes(attributes);

 Element element = new Element("div");
 element.attributes(attributes);

 builder.stack.add(element);

 boolean result = builder.processStartTag(token);

 Assert.assertTrue(result);
 Assert.assertEquals("div", currentElement(builder).tagName());
 Assert.assertEquals("myClass", currentElement(builder).classes());
 Assert.assertEquals("myId", currentElement(builder).id());
 Assert.assertEquals("color: red;", currentElement(builder).attr("style"));
 Assert.assertEquals("myTitle", currentElement(builder).attr("title"));
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder builder = new HtmlTreeBuilder();
 builder.initialiseParse("", "", ParseErrorList.noTracking());

 Token token = new Token.EndTag();
 token.name("html");

 builder.stack.add(new Element("html"));

 boolean result = builder.processEndTag(token);

 Assert.assertTrue(result);
 Assert.assertEquals(0, builder.stack.size());
 }

 private Element currentElement(TreeBuilder builder) {
 return builder.currentElement();
 }

}