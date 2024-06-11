import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static final String VALID_SELECTORS = "div#id1 > p:nth-child(2):first-of-type, div#id2:not(:has(p:contains(Error)))";
 private static final String INVALID_SELECTOR = "div#id:contains(abc:def)"; // unsupported namespace selectors
 private static final String MISSING_QUERY = "";

 @Test
 public void testValidSelectors() {
 QueryParser parser = new QueryParser(VALID_SELECTORS);
 Evaluator evaluator = parser.parse();
 Selector result = evaluator.evaluate(new TokenQueue(""));

 // Test valid selectors
 List<String> expectedSelectors = new ArrayList<String>();
 expectedSelectors.add("div#id1 > p:nth-child(2)");
 expectedSelectors.add("div#id1 > p.isFirstOfType");
 expectedSelectors.add("div#id2:not(p:contains(Error))");
 expectedSelectors.add("div#id2:not.hasError");

 for (int i = 0; i < result.size(); i++) {
 Selector selector = result.get(i);
 assertEquals("Selector at index " + i + " does not match expected selector", expectedSelectors.get(i), selector.toString());
 }
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidSelector() {
 QueryParser parser = new QueryParser(INVALID_SELECTOR);
 parser.parse(); // This should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingQuery() {
 QueryParser parser = new QueryParser(MISSING_QUERY);
 parser.parse(); // This should throw an exception
 }
}