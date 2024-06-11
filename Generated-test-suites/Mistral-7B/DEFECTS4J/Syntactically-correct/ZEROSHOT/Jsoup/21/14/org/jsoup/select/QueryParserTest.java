import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueMatching;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Not;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.Has;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 }

 @Test
 public void testParseTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassQuery() {
 QueryParser parser = new QueryParser(".classname");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".classname", evaluator.toString());
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser("#idname");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#idname", evaluator.toString());
 }

 @Test
 public void testParseMultipleCombinedElementQuery() {
 QueryParser parser = new QueryParser("div#idname.classname > ul > li:nth-child(2)");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());

 Evaluator firstEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Tag.class, firstEvaluator.getClass());
 assertEquals("div", firstEvaluator.toString());

 Evaluator secondEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Id.class, secondEvaluator.getClass());
 assertEquals("#idname", secondEvaluator.toString());

 Evaluator thirdEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(2);
 assertEquals(Evaluator.Tag.class, thirdEvaluator.getClass());
 assertEquals("ul", thirdEvaluator.toString());

 Evaluator fourthEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(3);
 assertEquals(Evaluator.Index.class, fourthEvaluator.getClass());
 assertEquals("2", fourthEvaluator.toString());
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = new QueryParser("[href='http://example.com']");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("[href='http://example.com']", evaluator.toString());
 }

 @Test
 public void testParseAttributeQueryWithNot() {
 QueryParser parser = new QueryParser("[!href='http://example.com']");
 Evaluator evaluator = parser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());

 Evaluator childEvaluator = ((StructuralEvaluator.Not) evaluator).getEvaluator();
 assertEquals(Evaluator.AttributeWithValue.class, childEvaluator.getClass());
 assertEquals("[href='http://example.com']", childEvaluator.toString());
 }

 @Test
 public void testParseAttributeQueryWithMultipleAttributes() {
 QueryParser parser = new QueryParser("[data-role='button' data-theme='a']");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());

 Evaluator firstAttributeEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Attribute.class, firstAttributeEvaluator.getClass());
 assertEquals("[data-role='button']", firstAttributeEvaluator.toString());

 Evaluator secondAttributeEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.AttributeWithValue.class, secondAttributeEvaluator.getClass());
 assertEquals("[data-theme='a']", secondAttributeEvaluator.toString());
 }

 @Test
 public void testParsePseudoClassQuery() {
 QueryParser parser = new QueryParser(":enabled");
 Evaluator evaluator = parser.parse();
 assertEquals(Evaluator.PseudoClass.class, evaluator.getClass());
 assertEquals(":enabled", evaluator.toString());
 }

 @Test
 public void testParsePseudoElementQuery() {
 QueryParser parser = new QueryParser("input:checked ~ label");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());

 Evaluator firstEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.PseudoElement.class, firstEvaluator.getClass());
 assertEquals("input:checked", firstEvaluator.toString());

 Evaluator secondEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(StructuralEvaluator.PreviousSibling.class, secondEvaluator.getClass());
 assertEquals("label", secondEvaluator.toString());
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("div.class1 > div.class2:has(ul:containsOwn(li:contains(text()='Hello World')))");
 Evaluator evaluator = parser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());

 Evaluator firstEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Tag.class, firstEvaluator.getClass());
 assertEquals("div.class1", firstEvaluator.toString());

 Evaluator secondEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Combining.class, secondEvaluator.getClass());
 assertEquals(":has", ((CombiningEvaluator) secondEvaluator).getOperator());

 Evaluator thirdEvaluator = ((CombiningEvaluator) secondEvaluator).getLeftOperand();
 assertEquals(Evaluator.Tag.class, thirdEvaluator.getClass());
 assertEquals("ul", thirdEvaluator.toString());

 Evaluator fourthEvaluator = ((CombiningEvaluator) secondEvaluator).getRightOperand();
 assertEquals(Evaluator.ContainsOwnText.class, fourthEvaluator.getClass());
 assertEquals("li:contains(text()='Hello World')", fourthEvaluator.toString());
 }
}