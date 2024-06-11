import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.IsNthOfType;
import org.jsoup.select.Evaluator.IsNthChild;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsNthLastOfType;
import org.jsoup.select.Evaluator.IsNthLastChild;
import org.jsoup.select.Evaluator.Has;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsData;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Not;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.StructuralEvaluator.And;
import org.jsoup.select.StructuralEvaluator.Or;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class QueryParserTest {

 @Test
 public void testParse_EmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Root);
 }

 @Test
 public void testParse_SingleTag() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Tag);
 assertEquals("div", evaluator.asString());
 }

 @Test
 public void testParse_IdSelector() {
 QueryParser parser = new QueryParser("#myId");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Id);
 assertEquals("#myId", evaluator.asString());
 }

 @Test
 public void testParse_ClassSelector() {
 QueryParser parser = new QueryParser(".myClass");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Class);
 assertEquals(".myClass", evaluator.asString());
 }

 @Test
 public void testParse_AttributeSelector() {
 QueryParser parser = new QueryParser("[title]");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Attribute);
 assertEquals("[title]", evaluator.asString());
 }

 @Test
 public void testParse_AttributeValueSelector() {
 QueryParser parser = new QueryParser("[title='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValue);
 assertEquals("[title='Test']", evaluator.asString());
 }

 @Test
 public void testParse_AttributeValueContainingSelector() {
 QueryParser parser = new QueryParser("[title*='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueContaining);
 assertEquals("[title*='Test']", evaluator.asString());
 }

 @Test
 public void testParse_AttributeValueStartingSelector() {
 QueryParser parser = new QueryParser("[title^='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueStarting);
 assertEquals("[title^='Test']", evaluator.asString());
 }

 @Test
 public void testParse_AttributeValueEndingSelector() {
 QueryParser parser = new QueryParser("[title$='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeWithValueEnding);
 assertEquals("[title$='Test']", evaluator.asString());
 }

 @Test
 public void testParse_AttributeStartingSelector() {
 QueryParser parser = new QueryParser("[title^='Test']");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof AttributeStarting);
 assertEquals("[title^='Test']", evaluator.asString());
 }

 @Test
 public void testParse_IndexSelector() {
 QueryParser parser = new QueryParser("div:first-child");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IsFirstChild);
 assertEquals("div:first-child", evaluator.asString());
 }

 @Test
 public void testParse_IndexGreaterThanSelector() {
 QueryParser parser = new QueryParser("div:gt(1)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexGreaterThan);
 assertEquals("div:gt(1)", evaluator.asString());
 }

 @Test
 public void testParse_IndexLessThanSelector() {
 QueryParser parser = new QueryParser("div:lt(1)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexLessThan);
 assertEquals("div:lt(1)", evaluator.asString());
 }

 @Test
 public void testParse_IndexEqualsSelector() {
 QueryParser parser = new QueryParser("div:eq(1)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof IndexEquals);
 assertEquals("div:eq(1)", evaluator.asString());
 }

 @Test
 public void testParse_HasSelector() {
 QueryParser parser = new QueryParser("div:has(p)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Has);
 assertEquals("div:has(p)", evaluator.asString());
 }

 @Test
 public void testParse_ContainsTextSelector() {
 QueryParser parser = new QueryParser("div:contains(Test)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof ContainsText);
 assertEquals("div:contains(Test)", evaluator.asString());
 }

 @Test
 public void testParse_ContainsOwnTextSelector() {
 QueryParser parser = new QueryParser("div:containsOwn(Test)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof ContainsOwnText);
 assertEquals("div:containsOwn(Test)", evaluator.asString());
 }

 @Test
 public void testParse_ContainsDataSelector() {
 QueryParser parser = new QueryParser("div:containsData(data)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof ContainsData);
 assertEquals("div:containsData(data)", evaluator.asString());
 }

 @Test
 public void testParse_MatchesSelector() {
 QueryParser parser = new QueryParser("div:matches(p*)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Matches);
 assertEquals("div:matches(p*)", evaluator.asString());
 }

 @Test
 public void testParse_MatchesOwnSelector() {
 QueryParser parser = new QueryParser("div:matchesOwn(p*)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof MatchesOwn);
 assertEquals("div:matchesOwn(p*)", evaluator.asString());
 }

 @Test
 public void testParse_NotSelector() {
 QueryParser parser = new QueryParser("div:not(.error)");
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof Not);
 assertEquals("div:not(.error)", evaluator.asString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParse_InvalidQuery() {
 QueryParser parser = new QueryParser("div:not(p>div>div)");
 }

 @Test
 public void testParseCombo_Descendant() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator currentEval = rootEval;
 Evaluator newEval = parser.parse("p");
 Evaluator expectedEval = new StructuralEvaluator.Descendant(currentEval, new Evaluator.Tag("p"));
 assertEquals(expectedEval, parser.parse().asStructure());
 }

 @Test
 public void testParseCombo_Child() {
 QueryParser parser = new QueryParser("div > p:first-child");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator currentEval = rootEval;
 Evaluator newEval = parser.parse("p:first-child");
 Evaluator expectedEval = new StructuralEvaluator.Child(currentEval, new Evaluator.Tag("p"));
 assertEquals(expectedEval, parser.parse().asStructure());
 }

 @Test
 public void testParseCombo_PreviousSibling() {
 QueryParser parser = new QueryParser("p ~ div");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator currentEval = rootEval;
 Evaluator newEval = parser.parse("div");
 Evaluator expectedEval = new StructuralEvaluator.PreviousSibling(currentEval, new Evaluator.Tag("div"));
 assertEquals(expectedEval, parser.parse().asStructure());
 }

 @Test
 public void testParseCombo_Sibling() {
 QueryParser parser = new QueryParser("p + div");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator currentEval = rootEval;
 Evaluator newEval = parser.parse("div");
 Evaluator expectedEval = new StructuralEvaluator.PreviousSibling(currentEval, new Evaluator.Tag("div"), true);
 assertEquals(expectedEval, parser.parse().asStructure());
 }

 @Test
 public void testParseCombo_And() {
 QueryParser parser = new QueryParser("p:first-child > div.test-class");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator currentEval = rootEval;
 Evaluator tagEval = parser.parse("p:first-child");
 Evaluator tagAndClassEval = new CombiningEvaluator.And(tagEval, new Evaluator.Class("test-class"));
 Evaluator expectedEval = tagAndClassEval;
 assertEquals(expectedEval, parser.parse().asStructure());
 }

 @Test
 public void testParseCombo_Or() {
 QueryParser parser = new QueryParser("div.class1, div.class2");
 Evaluator rootEval = new StructuralEvaluator.Root();
 Evaluator orEval = new CombiningEvaluator.Or();
 Evaluator class1Eval = new Evaluator.Class("class1");
 Evaluator class2Eval = new Evaluator.Class("class2");
 orEval.add(class1Eval);
 orEval.add(class2Eval);
 Evaluator expectedEval = orEval;
 assertEquals(expectedEval, parser.parse().asStructure());
 }
}