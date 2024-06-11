import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setUp() {
 queryParser = new QueryParser("");
 }

 /**
 * Test parsing a simple query with no combinators or attributes.
 */
 @Test
 public void testParseSimpleSelectors() {
 String query = "div p";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 }

 /**
 * Test parsing a query with a class attribute.
 */
 @Test
 public void testParseClassSelector() {
 String query = ".red";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an ID attribute.
 */
 @Test
 public void testParseIdSelector() {
 String query = "#myId";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertEquals("myId", elements.get(0).id());
 }

 /**
 * Test parsing a query with a tag name.
 */
 @Test
 public void testParseTagSelector() {
 String query = "h1";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertEquals("h1", elements.get(0).tagName());
 }

 /**
 * Test parsing a query with multiple simple selectors separated by commas.
 */
 @Test
 public void testParseMultipleSelectors() {
 String query = "div, p";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(2, elements.size());
 assertTrue(elements.get(0).tagName().equals("div") || elements.get(0).tagName().equals("p"));
 assertTrue(elements.get(1).tagName().equals("div") || elements.get(1).tagName().equals("p"));
 }

 /**
 * Test parsing a query with a descendant selector (space combinator).
 */
 @Test
 public void testParseDescendantSelector() {
 String query = "div p";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div"));
 }

 /**
 * Test parsing a query with an attribute selector.
 */
 @Test
 public void testParseAttributeSelector() {
 String query = "div[class='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value.
 */
 @Test
 public void testParseAttributeValueSelector() {
 String query = "div[class='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value using '=' combinator.
 */
 @Test
 public void testParseAttributeEqualsSelector() {
 String query = "div[class='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value using '!=
*/
 @Test
 public void testParseAttributeNotEqualsSelector() {
 String query = "div[class!='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertFalse(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value starting with the string.
 */
 @Test
 public void testParseAttributeStartsWithSelector() {
 String query = "div[class^='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().startsWith("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value ending with the string.
 */
 @Test
 public void testParseAttributeEndsWithSelector() {
 String query = "div[class$='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().endsWith("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a value containing the substring.
 */
 @Test
 public void testParseAttributeContainsSelector() {
 String query = "div[class*='red']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).className().contains("red"));
 }

 /**
 * Test parsing a query with an attribute selector and a regex value.
 */
 @Test
 public void testParseAttributeRegexSelector() {
 String query = "div[class~='regex']";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(Pattern.matches("regex", elements.get(0).className()));
 }

 /**
 * Test parsing a query with a pseudo-class selector :first-child.
 */
 @Test
 public void testParseFirstChildSelector() {
 String query = "div > p:first-child";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div"));
 assertTrue(elements.get(0).isFirstSibling());
 }

 /**
 * Test parsing a query with a pseudo-class selector :last-child.
 */
 @Test
 public void testParseLastChildSelector() {
 String query = "div > p:last-child";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div"));
 assertTrue(elements.get(0).isLastSibling());
 }

 /**
 * Test parsing a query with a pseudo-class selector :only-child.
 */
 @Test
 public void testParseOnlyChildSelector() {
 String query = "div > p:only-child";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div") && elements.get(0).isOnlyChild());
 }

 /**
 * Test parsing a query with a pseudo-class selector :only-of-type.
 */
 @Test
 public void testParseOnlyOfTypeSelector() {
 String query = "div > p:only-of-type";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p"));
 }

 /**
 * Test parsing a query with a pseudo-class selector :empty.
 */
 @Test
 public void testParseEmptySelector() {
 String query = "input:empty";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).text().isEmpty());
 }

 /**
 * Test parsing a query with a pseudo-class selector :root.
 */
 @Test
 public void testParseRootSelector() {
 String query = ":root";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).isRootElement());
 }

 /**
 * Test parsing a query with a pseudo-class selector :nth-child.
 */
 @Test
 public void testParseNthChildSelector() {
 String query = "div > p:nth-child(2)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div"));
 assertTrue(elements.get(0).isNthChild(2));
 }

 /**
 * Test parsing a query with a pseudo-class selector :nth-last-child.
 */
 @Test
 public void testParseNthLastChildSelector() {
 String query = "div > p:nth-last-child(2)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p") && elements.get(0).parentElement().tagName().equals("div"));
 assertTrue(elements.get(0).isNthLastChild(2));
 }

 /**
 * Test parsing a query with a pseudo-class selector :nth-of-type.
 */
 @Test
 public void testParseNthOfTypeSelector() {
 String query = "div > p:nth-of-type(2)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p"));
 assertTrue(elements.get(0).isNthOfType(2));
 }

 /**
 * Test parsing a query with a pseudo-class selector :nth-last-of-type.
 */
 @Test
 public void testParseNthLastOfTypeSelector() {
 String query = "div > p:nth-last-of-type(2)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("p"));
 assertTrue(elements.get(0).isNthLastOfType(2));
 }

 /**
 * Test parsing a query with a pseudo-class selector :has.
 */
 @Test
 public void testParseHasSelector() {
 String query = "div:has(p)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).tagName().equals("div"));
 assertTrue(elements.get(0).hasDescendant(new ElementsSelector(new TagNameSelector("p"))));
 }

 /**
 * Test parsing a query with a pseudo-class selector :contains.
 */
 @Test
 public void testParseContainsSelector() {
 String query = "div:contains(text)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).text().contains("text"));
 }

 /**
 * Test parsing a query with a pseudo-class selector :containsOwn.
 */
 @Test
 public void testParseContainsOwnSelector() {
 String query = "div:containsOwn(text)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).text().contains("text") && elements.get(0).isDescendantOrSelf(elements.get(0)));
 }

 /**
 * Test parsing a query with a pseudo-class selector :containsData.
 */
 @Test
 public void testParseContainsDataSelector() {
 String query = "div:containsData(data)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(elements.get(0).data().contains("data"));
 }

 /**
 * Test parsing a query with a pseudo-class selector :matches.
 */
 @Test
 public void testParseMatchesSelector() {
 String query = "div:matches(regex)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(Pattern.matches("regex", elements.get(0).outerHtml()));
 }

 /**
 * Test parsing a query with a pseudo-class selector :matchesOwn.
 */
 @Test
 public void testParseMatchesOwnSelector() {
 String query = "div:matchesOwn(regex)";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertTrue(Pattern.matches("regex", elements.get(0).outerHtml()) && elements.get(0).isDescendantOrSelf(elements.get(0)));
 }

 /**
 * Test parsing a query with a pseudo-class selector :not.
 */
 @Test
 public void testParseNotSelector() {
 String query = "div:not(:has(p))";
 Evaluator evaluator = QueryParser.parse(query);
 Selector selector = new Selector(evaluator);
 List<Element> elements = selector.select(new Document());
 assertEquals(1, elements.size());
 assertFalse(elements.get(0).hasDescendant(new ElementsSelector(new TagNameSelector("p"))));
 }
}