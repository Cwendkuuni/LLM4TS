import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import java.util.List;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseSingleElementSelector() {
 QueryParser parser = new QueryParser("#elementId");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Id("elementId"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseClassSelector() {
 QueryParser parser = new QueryParser(".className");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Class("className"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseTagSelector() {
 QueryParser parser = new QueryParser("tagName");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Tag("tagName"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseAttributeSelectorWithEqualSign() {
 QueryParser parser = new QueryParser("[attributeName=attributeValue]");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.AttributeWithValue("attributeName", "attributeValue"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseAttributeSelectorWithDash() {
 QueryParser parser = new QueryParser("[data-test=\"test\"]");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.AttributeWithValue("data-test", "test"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseCombinedSelectorsWithSpaceSeparator() {
 QueryParser parser = new QueryParser("div p");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new CombiningEvaluator.And(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseCombinedSelectorsWithCommaSeparator() {
 QueryParser parser = new QueryParser("div, p");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new CombiningEvaluator.Or(new Evaluator.Tag("div"), new Evaluator.Tag("p")));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("div[x=y"); // missing ]
 Evaluator evaluator = parser.parse();
 fail("Parser should throw exception for invalid query");
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMissingQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 fail("Parser should throw exception for missing query");
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser("#elementId > div > p:containsOwn(test)");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Id("elementId"));
 expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("div")));
 expected.add(new Evaluator.ContainsOwnText("test"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseNegatedSelector() {
 QueryParser parser = new QueryParser(":not(.className)");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new StructuralEvaluator.Not(new Evaluator.Class("className")));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseComplexNegatedSelector() {
 QueryParser parser = new QueryParser(":not(.className:has(#elementId))");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new StructuralEvaluator.Not(new StructuralEvaluator.Has(new Evaluator.Id("elementId"))));
 expected.add(new Evaluator.Class("className"));
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParsePseudoClassSelectors() {
 QueryParser parser = new QueryParser(":first-child");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.IsFirstChild());
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParsePseudoElementSelectors() {
 QueryParser parser = new QueryParser(":last-child::after");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.IsLastChild());
 expected.add(new Evaluator.PseudoElementAfter());
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }

 @Test
 public void testParseComplexQueryWithPseudoElements() {
 QueryParser parser = new QueryParser("#elementId > div > p:containsOwn(test):last-child::after");
 Evaluator evaluator = parser.parse();
 List<Evaluator> expected = new ArrayList<>();
 expected.add(new Evaluator.Id("elementId"));
 expected.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("div")));
 expected.add(new Evaluator.ContainsOwnText("test"));
 expected.add(new Evaluator.IsLastChild());
 expected.add(new Evaluator.PseudoElementAfter());
 assertTrue(evaluator.equals(expected.toArray(new Evaluator[0])));
 }
}