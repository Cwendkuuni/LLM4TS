import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.*;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Before
 public void setUp() {
 query = "div.class1 > div.class2 > input[type='text']";
 parser = new QueryParser(query);
 }

 @Test
 public void testParse_simpleSelector() {
 Evaluator evaluator = parser.parse();
 // typical use case: validate the parsed evaluator for the given query
 assertEquals("div.class1 > div.class2 > input[type='text']", parser.query);
 // edge case: validate the parsed evaluator when query contains multiple selectors separated by commas
 String multipleSelectors = "div.class1, div.class2 > input[type='text']";
 Evaluator multipleEvaluators[] = parser.parseAll(multipleSelectors);
 assertEquals(2, multipleEvaluators.length);
 assertTrue(multipleEvaluators[0] instanceof Evaluator.Class);
 assertTrue(multipleEvaluators[1] instanceof StructuralEvaluator.Descendant);
 }

 @Test
 public void testParse_combinators() {
 // typical use case: validate the parsed evaluator for a query with combinators
 String queryWithCombinators = "div > ul > li > a";
 parser = new QueryParser(queryWithCombinators);
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.Descendant);
 StructuralEvaluator.Descendant descendantEvaluator = (StructuralEvaluator.Descendant) evaluator;
 assertEquals("div", descendantEvaluator.getParentSelector());
 assertEquals("ul", descendantEvaluator.getChildSelector());
 assertEquals("li", descendantEvaluator.getGrandChildSelector());
 assertEquals("a", descendantEvaluator.getGreatGrandChildSelector());

 // edge case: validate the parsed evaluator for a query with multiple combinators in a row
 String queryWithMultipleCombinators = "div > div > div > div";
 parser = new QueryParser(queryWithMultipleCombinators);
 evaluator = parser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.Descendant);
 StructuralEvaluator.Descendant descendantEvaluatorMultipleCombinators = (StructuralEvaluator.Descendant) evaluator;
 assertEquals("div", descendantEvaluatorMultipleCombinators.getParentSelector());
 assertEquals("div", descendantEvaluatorMultipleCombinators.getChildSelector());
 assertEquals("div", descendantEvaluatorMultipleCombinators.getGrandChildSelector());
 assertEquals("div", descendantEvaluatorMultipleCombinators.getGreatGrandChildSelector());
 }

 @Test
 public void testParse_pseudoClasses() {
 // typical use case: validate the parsed evaluator for a query with pseudo classes
 String queryWithPseudoClasses = "div:first-child > input[type='text']:enabled";
 parser = new QueryParser(queryWithPseudoClasses);
 Evaluator evaluator = parser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 CombiningEvaluator.And andEvaluator = (CombiningEvaluator.And) evaluator;
 assertTrue(andEvaluator.getLeftEvaluator() instanceof Evaluator.IsFirstChild);
 assertTrue(andEvaluator.getRightEvaluator() instanceof Evaluator.IsEnabled);
 }

 @Test
 public void testParse_errors() {
 // error scenario: validate that an empty query throws an exception
 String emptyQuery = "";
 try {
 parser = new QueryParser(emptyQuery);
 parser.parse();
 fail("Expected Selector.SelectorParseException for empty query");
 } catch (Selector.SelectorParseException e) {
 // expected
 }

 // error scenario: validate that an invalid query throws an exception
 String invalidQuery = "div[class=1] > div[class^=x] > input[type=text]";
 try {
 parser = new QueryParser(invalidQuery);
 parser.parse();
 fail("Expected Selector.SelectorParseException for invalid query");
 } catch (Selector.SelectorParseException e) {
 // expected
 String errorMessage = e.getMessage();
 assertTrue(errorMessage.contains("Unexpected token: '^'"));
 }
}

}