import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private QueryParser queryParser;

 @Before
 public void setup() {
 queryParser = new QueryParser("#test id > .class1 > .class2");
 }

 @Test
 public void testParse() {
 Evaluator evaluator = queryParser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseWithSingleElement() {
 QueryParser parser = new QueryParser(".class1");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseWithUnknownCombinator() {
 QueryParser parser = new QueryParser("div~x");
 parser.parse();
 }

 @Test
 public void testParseWithMultipleElements() {
 QueryParser parser = new QueryParser("div > p, div > ul > li > a");
 CombiningEvaluator.Or or = (CombiningEvaluator.Or) parser.parse();
 assertNotNull(or);
 assertEquals(2, or.size());
 }

 @Test
 public void testParseWithId() {
 QueryParser parser = new QueryParser("#testId");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 }

 @Test
 public void testParseWithClass() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 }

 @Test
 public void testParseWithTag() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 }

 @Test
 public void testParseWithAttribute() {
 QueryParser parser = new QueryParser("[title='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 }

 @Test
 public void testParseWithAttributeValue() {
 QueryParser parser = new QueryParser("[title='Test']");
 Evaluator evaluator = parser.parse();
 assertEquals("Test", evaluator.getAttributeValue());
 }

 @Test
 public void testParseWithAttributeValueNot() {
 QueryParser parser = new QueryParser("[title!='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals("Test", evaluator.getAttributeValueNot());
 }

 @Test
 public void testParseWithAttributeValueStarting() {
 QueryParser parser = new QueryParser("[title^='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals("Test", evaluator.getAttributeValueStarting());
 }

 @Test
 public void testParseWithAttributeValueEnding() {
 QueryParser parser = new QueryParser("[title$='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals("Test", evaluator.getAttributeValueEnding());
 }

 @Test
 public void testParseWithAttributeValueContaining() {
 QueryParser parser = new QueryParser("[title*='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals("Test", evaluator.getAttributeValueContaining());
 }

 @Test
 public void testParseWithAttributeValueMatching() {
 QueryParser parser = new QueryParser("[title~='Test']");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseWithAllElements() {
 QueryParser parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AllElements.class, evaluator.getClass());
 }

 @Test
 public void testParseWithIndexLessThan() {
 QueryParser parser = new QueryParser("li:lt(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.IndexLessThan.class, evaluator.getClass());
 }

 @Test
 public void testParseWithIndexGreaterThan() {
 QueryParser parser = new QueryParser("li:gt(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.IndexGreaterThan.class, evaluator.getClass());
 }

 @Test
 public void testParseWithIndexEquals() {
 QueryParser parser = new QueryParser("li:eq(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.IndexEquals.class, evaluator.getClass());
 }

 @Test
 public void testParseWithHas() {
 QueryParser parser = new QueryParser("div:has(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
 }

 @Test
 public void testParseWithContains() {
 QueryParser parser = new QueryParser("div:contains(Test)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 }

 @Test
 public void testParseWithContainsOwn() {
 QueryParser parser = new QueryParser("div:containsOwn(Test)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
 }

 @Test
 public void testParseWithMatches() {
 QueryParser parser = new QueryParser("div:matches(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Matches.class, evaluator.getClass());
 }

 @Test
 public void testParseWithMatchesOwn() {
 QueryParser parser = new QueryParser("div:matchesOwn(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
 }

 @Test
 public void testParseWithNot() {
 QueryParser parser = new QueryParser("div:not(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 }
}