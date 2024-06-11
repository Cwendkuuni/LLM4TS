import org.junit.Test;
import org.jsoup.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static final String BASIC_SELECTOR = "#testId .testClass";
 private static final String BASIC_HTML = "<html><body><div id=\"testId\"><p class=\"testClass\">Hello World</p></div></body></html>";

 @Test
 public void testBasicSelector() {
 Document doc = Parser.parseInput(BASIC_HTML, "");
 QueryParser queryParser = new QueryParser(BASIC_SELECTOR);
 Evaluator evaluator = queryParser.parse();

 Element element = doc.selectOne(evaluator.toString());
 assertNotNull(element);
 assertEquals("Hello World", element.text());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidSelector() {
 String invalidSelector = ":invalidSelector";
 QueryParser queryParser = new QueryParser(invalidSelector);
 try {
 queryParser.parse();
 fail("Expected SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // pass
 }
 }

 @Test
 public void testMultipleSelectorsWithCombinators() {
 String multipleSelectors = "#testId > .testClass";
 Document doc = Parser.parseInput(BASIC_HTML, "");
 QueryParser queryParser = new QueryParser(multipleSelectors);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testAttributeSelector() {
 String attributeSelector = "p[class='testClass']";
 Document doc = Parser.parseInput(BASIC_HTML, "");
 QueryParser queryParser = new QueryParser(attributeSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertTrue(element.hasAttr("class"));
 assertEquals("testClass", element.attr("class"));
 }

 @Test
 public void testAttributeSelectorWithValue() {
 String attributeSelectorWithValue = "p[class='testClass']:contains(Hello)";
 Document doc = Parser.parseInput(BASIC_HTML, "");
 QueryParser queryParser = new QueryParser(attributeSelectorWithValue);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertTrue(element.hasText());
 assertEquals("Hello World", element.text());
 }

 @Test
 public void testAttributeSelectorWithMultipleValues() {
 String attributeSelectorWithMultipleValues = "p[class~='testClass testClass2']";
 Document doc = Parser.parseInput("<html><body><p class='testClass testClass2'>Hello World</p></body></html>", "");
 QueryParser queryParser = new QueryParser(attributeSelectorWithMultipleValues);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertTrue(element.hasAttr("class"));
 assertTrue(element.hasClass("testClass"));
 assertTrue(element.hasClass("testClass2"));
 }

 @Test
 public void testIdSelectorWithMultipleElements() {
 String idSelectorWithMultipleElements = "#testId1, #testId2";
 Document doc = Parser.parseInput("<html><body><div id=\"testId1\">1</div><div id=\"testId2\">2</div></body></html>", "");
 QueryParser queryParser = new QueryParser(idSelectorWithMultipleElements);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(2, elements.size());
 }

 @Test
 public void testNthChildSelector() {
 String nthChildSelector = "div > p:nth-child(2)";
 Document doc = Parser.parseInput("<html><body><div><p>1</p><p>2</p></div></body></html>", "");
 QueryParser queryParser = new QueryParser(nthChildSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertEquals("2", element.text());
 }

 @Test
 public void testNthLastChildSelector() {
 String nthLastChildSelector = "div > p:nth-last-child(2)";
 Document doc = Parser.parseInput("<html><body><div><p>1</p><p>2</p><p>3</p></div></body></html>", "");
 QueryParser queryParser = new QueryParser(nthLastChildSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 Element element = elements.get(0);
 assertEquals("1", element.text());
 }

 @Test
 public void testNthOfTypeSelector() {
 String nthOfTypeSelector = "div > p:nth-of-type(2)";
 Document doc = Parser.parseInput("<html><body><div><p/><p/><p/></div></body></html>", "");
 QueryParser queryParser = new QueryParser(nthOfTypeSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(2, elements.get(0).tagName().intValue());
 }

 @Test
 public void testNthLastOfTypeSelector() {
 String nthLastOfTypeSelector = "div > p:nth-last-of-type(2)";
 Document doc = Parser.parseInput("<html><body><div><p/><p/><p/></div></body></html>", "");
 QueryParser queryParser = new QueryParser(nthLastOfTypeSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(3, elements.get(0).tagName().intValue());
 }

 @Test
 public void testFirstChildSelector() {
 String firstChildSelector = "div > p:first-child";
 Document doc = Parser.parseInput("<html><body><div><p>1</p><p>2</p></div></body></html>", "");
 QueryParser queryParser = new QueryParser(firstChildSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(1, elements.get(0).tagName().intValue());
 }

 @Test
 public void testLastChildSelector() {
 String lastChildSelector = "div > p:last-child";
 Document doc = Parser.parseInput("<html><body><div><p>1</p><p>2</p></div></body></html>", "");
 QueryParser queryParser = new QueryParser(lastChildSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(2, elements.get(0).tagName().intValue());
 }

 @Test
 public void testFirstOfTypeSelector() {
 String firstOfTypeSelector = "div > p:first-of-type";
 Document doc = Parser.parseInput("<html><body><div><p/><p/><p/></div></body></html>", "");
 QueryParser queryParser = new QueryParser(firstOfTypeSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(1, elements.get(0).tagName().intValue());
 }

 @Test
 public void testLastOfTypeSelector() {
 String lastOfTypeSelector = "div > p:last-of-type";
 Document doc = Parser.parseInput("<html><body><div><p/><p/><p/></div></body></html>", "");
 QueryParser queryParser = new QueryParser(lastOfTypeSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(3, elements.get(0).tagName().intValue());
 }

 @Test
 public void testEmptySelector() {
 String emptySelector = ":empty";
 Document doc = Parser.parseInput("<html><body></body></html>", "");
 QueryParser queryParser = new QueryParser(emptySelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).isEmpty());
 }

 @Test
 public void testRootSelector() {
 String rootSelector = ":root";
 Document doc = Parser.parseInput("<html><body></body></html>", "");
 QueryParser queryParser = new QueryParser(rootSelector);
 Evaluator evaluator = queryParser.parse();

 List<Element> elements = doc.select(evaluator.toString());
 assertEquals(1, elements.size());
 assertEquals(1, elements.get(0).tagName().intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseError() {
 String parseError = "div > p:nth-child(-1)";
 QueryParser queryParser = new QueryParser(parseError);
 try {
 queryParser.parse();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}