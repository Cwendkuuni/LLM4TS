import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {

 private String testQuery = "div.class1.class2 > p#id1:first-child:has(a[href='http://example.com'])";
 private QueryParser queryParser = new QueryParser(testQuery);

 @Test
 public void testParse() {
 Evaluator evaluator = queryParser.parse();
 // Basic functionality test
 assertEquals(evaluator.getClass().getName(), "org.jsoup.select.CombiningEvaluator$And");
 // Test for edge cases and exceptions
 Evaluator rootEval = evaluator;
 while (rootEval.getNextElement() != null) {
 if (rootEval instanceof StructuralEvaluator.Root) {
 assertEquals("div", rootEval.tagName());
 } else if (rootEval instanceof Evaluator.Class) {
 assertEquals("class1 class2", rootEval.attributeValue());
 } else if (rootEval instanceof Evaluator.Id) {
 assertEquals("id1", rootEval.attributeValue());
 } else if (rootEval instanceof Evaluator.Tag) {
 assertEquals("p", rootEval.tagName());
 } else if (rootEval instanceof StructuralEvaluator.ImmediateParent) {
 assertEquals("div", ((StructuralEvaluator.ImmediateParent) rootEval).getParentElement().tagName());
 } else if (rootEval instanceof StructuralEvaluator.ImmediatePreviousSibling) {
 assertEquals("div", ((StructuralEvaluator.ImmediatePreviousSibling) rootEval).getPreviousSibling().tagName());
 } else if (rootEval instanceof StructuralEvaluator.Parent) {
 assertEquals("div", ((StructuralEvaluator.Parent) rootEval).getParentElement().tagName());
 } else if (rootEval instanceof StructuralEvaluator.PreviousSibling) {
 assertEquals("p", ((StructuralEvaluator.PreviousSibling) rootEval).getPreviousSibling().tagName());
 } else if (rootEval instanceof CombiningEvaluator.Or) {
 // OR combinator test
 Evaluator leftOr = ((CombiningEvaluator.Or) rootEval).getLeftOrRightEvaluator();
 assertEquals("a[href='http://example.com']", leftOr.toString());
 Evaluator rightOr = ((CombiningEvaluator.Or) rootEval).getRightOrLeftEvaluator();
 assertEquals(rootEval.getClass().getName(), "org.jsoup.select.CombiningEvaluator$Or");
 } else if (rootEval instanceof CombiningEvaluator.And) {
 // AND combinator test
 Evaluator leftAnd = ((CombiningEvaluator.And) rootEval).getLeftOrRightEvaluator();
 assertEquals("div.class1.class2", leftAnd.toString());
 Evaluator rightAnd = ((CombiningEvaluator.And) rootEval).getRightOrLeftEvaluator();
 assertEquals("p#id1:first-child:has(a[href='http://example.com'])", rightAnd.toString());
 } else {
 throw new AssertionError("Unexpected evaluator: " + rootEval.getClass().getName());
 }
 rootEval = rootEval.getNextElement();
 }
 }

 @Test
 public void testParseWithEmptyQuery() {
 QueryParser emptyQueryParser = new QueryParser("");
 assertEquals(emptyQueryParser.parse(), new StructuralEvaluator.Root());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseWithInvalidQuery() {
 QueryParser invalidQueryParser = new QueryParser(":not(div)"); // missing opening parenthesis
 }

 @Test
 public void testParseWithComplexQuery() {
 String complexQuery = "div:not(:has(ul)):not(ul) > ul > li:not(.active):has(a) > a[href='https://example.com']";
 QueryParser complexQueryParser = new QueryParser(complexQuery);
 Evaluator complexQueryEvaluator = complexQueryParser.parse();
 }

}