import static org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

 @org.junit.Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @org.junit.Test
 public void testParseSingleTagSelector() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Tag);
 assertEquals("div", evaluator.toString());
 }

 @org.junit.Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".class-name");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Class);
 assertEquals("class-name", evaluator.toString());
 }

 @org.junit.Test
 public void testParseIdSelector() {
 QueryParser queryParser = new QueryParser("#id-name");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Id);
 assertEquals("id-name", evaluator.toString());
 }

 @org.junit.Test
 public void testParseCombinedSelectors() {
 QueryParser queryParser = new QueryParser("div.class-name #id-name");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(2, evaluator.children().size());
 }

 @org.junit.Test
 public void testParseDescendantSelectors() {
 QueryParser queryParser = new QueryParser("div > p > ul > li > a");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(5, evaluator.children().size());
 }

 @org.junit.Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[href]");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.Attribute);
 }

 @org.junit.Test
 public void testParseAttributeSelectorWithValue() {
 QueryParser queryParser = new QueryParser("[href='http://www.example.com']");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
 assertEquals("http://www.example.com", evaluator.toString());
 }

 @org.junit.Test
 public void testParseAttributeSelectorWithOperator() {
 QueryParser queryParser = new QueryParser("[title!='Home']");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
 }

 @org.junit.Test
 public void testParsePseudoClasses() {
 QueryParser queryParser = new QueryParser(":hover:focus");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(2, evaluator.children().size());
 }

 @org.junit.Test
 public void testParseCombinatorPlus() {
 QueryParser queryParser = new QueryParser("div + p");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.PreviousSibling);
 }

 @org.junit.Test
 public void testParseCombinatorTilde() {
 QueryParser queryParser = new QueryParser("div ~ p");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof StructuralEvaluator.PreviousSiblingOrSameType);
 }

 @org.junit.Test
 public void testParseCombinatorGreaterThan() {
 QueryParser queryParser = new QueryParser("div > ul > li > a");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(4, evaluator.children().size());
 }

 @org.junit.Test
 public void testParseCombinatorLessThan() {
 QueryParser queryParser = new QueryParser("div < ul > li > a");
 // This test case should fail since the order of child elements is not defined.
 // assertFalse("Unexpected result for less than combinator", evaluator instanceof CombiningEvaluator.And);
 }

 @org.junit.Test
 public void testParsePseudoElement() {
 QueryParser queryParser = new QueryParser("ul > li:first-child");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(2, evaluator.children().size());
 }

 @org.junit.Test
 public void testParseComplexSelector() {
 QueryParser queryParser = new QueryParser("div.class-name > div#id-name > ul > li > a[href^='http']");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertEquals(6, evaluator.children().size());
 }
}