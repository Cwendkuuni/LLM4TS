import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @org.junit.Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @org.junit.Test
 public void testParseOneElementQuery() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("div", evaluator.toString());
 }

 @org.junit.Test
 public void testParseOneClassQuery() {
 QueryParser queryParser = new QueryParser(".class");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Class);
 assertEquals("class", evaluator.toString());
 }

 @org.junit.Test
 public void testParseOneIdQuery() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("id", evaluator.toString());
 }

 @org.junit.Test
 public void testParseMultiElementQuery() {
 QueryParser queryParser = new QueryParser("div, p, ul");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.Or);
 assertEquals(3, ((CombiningEvaluator.Or) evaluator).size());
 }

 @org.junit.Test
 public void testParseMultiCombinatorQuery() {
 QueryParser queryParser = new QueryParser("div > p > ul > li");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertTrue(evaluator instanceof StructuralEvaluator.ImmediateParent);
 assertTrue(evaluator instanceof StructuralEvaluator.ImmediateChild);
 assertTrue(evaluator instanceof StructuralEvaluator.ImmediatePreviousSibling);
 }

 @org.junit.Test
 public void testParseAttributeQuery() {
 QueryParser queryParser = new QueryParser("input[type=text]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 assertEquals("input", evaluator.toString());
 assertEquals("type", ((Evaluator.Attribute) evaluator).getKey());
 assertEquals("text", ((Evaluator.Attribute) evaluator).getValue());
 }

 @org.junit.Test
 public void testParseAttributeQueryWithValueNot() {
 QueryParser queryParser = new QueryParser("input:not([type=text])");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.Not);
 assertTrue(evaluator.getChild() instanceof Evaluator.Attribute);
 assertEquals("input", evaluator.toString());
 }

 @org.junit.Test
 public void testParseAttributeQueryWithValueStarting() {
 QueryParser queryParser = new QueryParser("input[type^=text]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeStarting);
 assertEquals("input", evaluator.toString());
 assertEquals("type", ((Evaluator.AttributeStarting) evaluator).getKey());
 assertEquals("text", ((Evaluator.AttributeStarting) evaluator).getValue());
 }

 @org.junit.Test
 public void testParseAttributeQueryWithValueEnding() {
 QueryParser queryParser = new QueryParser("input[type$=text]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueEnding);
 assertEquals("input", evaluator.toString());
 assertEquals("type", ((Evaluator.AttributeWithValueEnding) evaluator).getKey());
 assertEquals("text", ((Evaluator.AttributeWithValueEnding) evaluator).getValue());
 }

 @org.junit.Test
 public void testParseAttributeQueryWithValueContaining() {
 QueryParser queryParser = new QueryParser("input[type*=text]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
 assertEquals("input", evaluator.toString());
 assertEquals("type", ((Evaluator.AttributeWithValueContaining) evaluator).getKey());
 assertEquals("text", ((Evaluator.AttributeWithValueContaining) evaluator).getValue());
 }

 @org.junit.Test
 public void testParseAttributeQueryWithValueMatching() {
 QueryParser queryParser = new QueryParser("input[type~=text]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
 assertEquals("input", evaluator.toString());
 assertEquals("type", ((Evaluator.AttributeWithValueMatching) evaluator).getKey());
 }

 @org.junit.Test
 public void testParsePseudoClassLessThanQuery() {
 QueryParser queryParser = new QueryParser("li:lt(2)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.IndexLessThan);
 assertEquals("li", evaluator.toString());
 assertEquals(2, ((Evaluator.IndexLessThan) evaluator).getIndex());
 }

 @org.junit.Test
 public void testParsePseudoClassGreaterThanQuery() {
 QueryParser queryParser = new QueryParser("li:gt(2)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.IndexGreaterThan);
 assertEquals("li", evaluator.toString());
 assertEquals(3, ((Evaluator.IndexGreaterThan) evaluator).getIndex());
 }

 @org.junit.Test
 public void testParsePseudoClassEqualsQuery() {
 QueryParser queryParser = new QueryParser("li:eq(2)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.IndexEquals);
 assertEquals("li", evaluator.toString());
 assertEquals(2, ((Evaluator.IndexEquals) evaluator).getIndex());
 }

 @org.junit.Test
 public void testParsePseudoClassHasQuery() {
 QueryParser queryParser = new QueryParser("li:has(a)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.Has);
 }

 @org.junit.Test
 public void testParsePseudoClassContainsTextQuery() {
 QueryParser queryParser = new QueryParser("li:contains(text)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.ContainsText);
 assertEquals("li", evaluator.toString());
 }

 @org.junit.Test
 public void testParsePseudoClassContainsOwnTextQuery() {
 QueryParser queryParser = new QueryParser("li:containsOwn(text)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.ContainsOwnText);
 assertEquals("li", evaluator.toString());
 }

 @org.junit.Test
 public void testParsePseudoClassMatchesTextQuery() {
 QueryParser queryParser = new QueryParser("li:matches(text)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Matches);
 }

 @org.junit.Test
 public void testParsePseudoClassMatchesOwnTextQuery() {
 QueryParser queryParser = new QueryParser("li:matchesOwn(text)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.MatchesOwn);
 }

 @org.junit.Test
 public void testParseNotQuery() {
 QueryParser queryParser = new QueryParser("li:not(.selected)");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.Not);
 }

 @org.junit.Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("li:not(.selected):has(a:matches[abc]);");
 }

 @org.junit.Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery2() {
 QueryParser queryParser = new QueryParser("li:not(.selected):has(a:matches[abc:]);");
 }

 @org.junit.Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery3() {
 QueryParser queryParser = new QueryParser("li:not(.selected):has(a:matches[abc]):has(b:matches[xyz]);");
 }
}