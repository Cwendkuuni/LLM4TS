import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseSimpleSelector() {
 String query = "div.class1";
 Evaluator evaluator = QueryParser.parse(query);
 assertInstanceOf(Evaluator.Class.class, evaluator);
 assertEquals("class1", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithCombinators() {
 String query = "div > p:first-child";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("div > p:first-child", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithAttribute() {
 String query = "input[type='text']";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("input[type='text']", evaluator.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 String query = "div:invalidQuery";
 try {
 QueryParser.parse(query);
 fail("Expected Selector.SelectorParseException");
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }

 @Test
 public void testParseComplexSelectorWithNthChild() {
 String query = "li:nth-child(2) of p:nth-child(3)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("li:nth-child(2):of(p:nth-child(3))", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithNot() {
 String query = "li:not(:first-child)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("li:not(:first-child)", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithHas() {
 String query = "li:has(a)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("li:has(a)", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithContainsData() {
 String query = "li:containsData(data)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("li:containsData(data)", evaluator.toString());
 }

 @Test
 public void testParseComplexSelectorWithMatches() {
 String query = "li:matches(a:b)";
 Evaluator evaluator = QueryParser.parse(query);
 assertEquals("li:matches(a:b)", evaluator.toString());
 }
}