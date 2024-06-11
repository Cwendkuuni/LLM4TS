import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassQuery() {
 QueryParser parser = new QueryParser(".button");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".button", evaluator.toString());
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser("#header");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#header", evaluator.toString());
 }

 @Test
 public void testParseSimpleQuery() {
 QueryParser parser = new QueryParser("div.button");
 Evaluator evaluator = parser.parse();
 Evaluator tagEvaluator = evaluator.get(0);
 assertEquals(Evaluator.Tag.class, tagEvaluator.getClass());
 assertEquals("div", tagEvaluator.toString());
 Evaluator classEvaluator = evaluator.get(1);
 assertEquals(Evaluator.Class.class, classEvaluator.getClass());
 assertEquals(".button", classEvaluator.toString());
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = new QueryParser("[href]");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 assertEquals("[href]", evaluator.toString());
 }

 @Test
 public void testParseAttributeValueQuery() {
 QueryParser parser = new QueryParser("[href='/']");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("[href='/']", evaluator.toString());
 }

 @Test
 public void testParseMultipleAttributeQuery() {
 QueryParser parser = new QueryParser("[id='header' class='button']");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.get(0).getClass());
 assertEquals("button", evaluator.get(0).toString());
 assertEquals(Evaluator.Id.class, evaluator.get(1).getClass());
 assertEquals("header", evaluator.get(1).toString());
 }

 @Test
 public void testParseCombinatorQuery() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator evaluator = parser.parse();
 Evaluator tagParentEvaluator = evaluator.get(0);
 assertEquals(Evaluator.Tag.class, tagParentEvaluator.getClass());
 assertEquals("div", tagParentEvaluator.toString());
 Evaluator immediateChildEvaluator = evaluator.get(1);
 assertEquals(StructuralEvaluator.ImmediateChild.class, immediateChildEvaluator.getClass());
 }

 @Test
 public void testParseCombinatorQueryWithMultipleChildren() {
 QueryParser parser = new QueryParser("div > p > ul > li");
 Evaluator evaluator = parser.parse();
 Evaluator tagParentEvaluator = evaluator.get(0);
 assertEquals(Evaluator.Tag.class, tagParentEvaluator.getClass());
 assertEquals("div", tagParentEvaluator.toString());
 Evaluator immediateChildEvaluator = evaluator.get(1);
 assertEquals(Evaluator.Tag.class, immediateChildEvaluator.getClass());
 assertEquals("p", immediateChildEvaluator.toString());
 Evaluator nextSiblingEvaluator = evaluator.get(2);
 assertEquals(StructuralEvaluator.ImmediatePreviousSibling.class, nextSiblingEvaluator.getClass());
 Evaluator parentOfNextSiblingEvaluator = evaluator.get(3);
 assertEquals(Evaluator.Tag.class, parentOfNextSiblingEvaluator.getClass());
 assertEquals("ul", parentOfNextSiblingEvaluator.toString());
 Evaluator listItemEvaluator = evaluator.get(4);
 assertEquals(Evaluator.Tag.class, listItemEvaluator.getClass());
 assertEquals("li", listItemEvaluator.toString());
 }

 @Test
 public void testParsePseudoClassQueryLessThan() {
 QueryParser parser = new QueryParser("li:lt(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexEvaluator = evaluator.get(0);
 assertEquals(Evaluator.IndexLessThan.class, indexEvaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryGreaterThan() {
 QueryParser parser = new QueryParser("li:gt(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexEvaluator = evaluator.get(0);
 assertEquals(Evaluator.IndexGreaterThan.class, indexEvaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryEquals() {
 QueryParser parser = new QueryParser("li:eq(2)");
 Evaluator evaluator = parser.parse();
 Evaluator indexEvaluator = evaluator.get(0);
 assertEquals(Evaluator.IndexEquals.class, indexEvaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryHas() {
 QueryParser parser = new QueryParser("li:has(p)");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
 Evaluator childEvaluator = evaluator.get(0);
 assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
 assertEquals("p", childEvaluator.toString());
 }

 @Test
 public void testParsePseudoClassQueryContains() {
 QueryParser parser = new QueryParser("li:contains(apple)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryContainsOwn() {
 QueryParser parser = new QueryParser("li:containsOwn(apple)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryMatches() {
 QueryParser parser = new QueryParser("li:matches(.button)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Matches.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryMatchesOwn() {
 QueryParser parser = new QueryParser("li:matchesOwn(.button)");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoClassQueryNot() {
 QueryParser parser = new QueryParser("li:not(.button)");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 Evaluator negatedEvaluator = evaluator.get(0);
 assertEquals(Evaluator.Class.class, negatedEvaluator.getClass());
 assertEquals(".button", negatedEvaluator.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("li:foo");
 parser.parse();
 fail("Expected SelectorParseException");
 }

}