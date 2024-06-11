import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Tokenizer;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import static org.junit.Assert.*;

public class QueryParserTest {

 private String query;
 private QueryParser parser;

 @Test
 public void testParseEmptyQuery() {
 query = "";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.AllElements);
 }

 @Test
 public void testParseSingleElementQuery() {
 query = "div";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.Tag);
 assertEquals("div", parser.toString());
 }

 @Test
 public void testParseClassSelector() {
 query = ".myClass";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.Class);
 assertEquals(".myClass", parser.toString());
 }

 @Test
 public void testParseIDSelector() {
 query = "#myID";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.Id);
 assertEquals("#myID", parser.toString());
 }

 @Test
 public void testParseCombinedSelectors() {
 query = "div.myClass > .anotherClass";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof CombiningEvaluator.And);
 assertEquals(3, parser.children().size());
 assertEquals(parser.children().get(0), new StructuralEvaluator.Tag("div"));
 assertEquals(parser.children().get(1), new StructuralEvaluator.Class("myClass"));
 assertEquals(parser.children().get(2), new StructuralEvaluator.Tag("anotherClass"));
 }

 @Test
 public void testParseAttributeSelector() {
 query = "input[type='text']";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.Attribute);
 assertEquals("input[type='text']", parser.toString());
 }

 @Test
 public void testParseAttributeWithValueSelector() {
 query = "input[type='text'][value='username']";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof Evaluator.AttributeWithValue);
 assertEquals("input[type='text'][value='username']", parser.toString());
 }

 @Test
 public void testParseComplexSelectorWithNestedParentSelectors() {
 query = "ul > li > ul > li > input[type='text']";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof CombiningEvaluator.And);
 assertEquals(5, parser.children().size());
 assertEquals(parser.children().get(0), new StructuralEvaluator.Tag("ul"));
 assertEquals(parser.children().get(1), new StructuralEvaluator.Tag("li"));
 assertEquals(parser.children().get(2), new StructuralEvaluator.Tag("ul"));
 assertEquals(parser.children().get(3), new StructuralEvaluator.Tag("li"));
 assertEquals(parser.children().get(4), new Evaluator.AttributeWithValue(new Attribute("type", "text")));
 }

 @Test
 public void testParsePseudoSelectors() {
 query = ":has(.myClass)";
 parser = QueryParser.parse(query);
 assertTrue(parser instanceof StructuralEvaluator.Has);
 }

 @Test
 public void testParseInvalidQueryWithMissingSelector() {
 query = ":not(";
 try {
 parser = QueryParser.parse(query);
 fail();
 } catch (Selector.SelectorParseException e) {
 //expected
 }
 }

 @Test
 public void testParseInvalidQueryWithMissingColonBeforePseudoClass() {
 query = "not:has(.myClass)";
 try {
 parser = QueryParser.parse(query);
 fail();
 } catch (Selector.SelectorParseException e) {
 //expected
 }
 }

 @Test
 public void testParseInvalidQueryWithMissingClosingParenthesis() {
 query = ":has(.myClass).";
 try {
 parser = QueryParser.parse(query);
 fail();
 } catch (Selector.SelectorParseException e) {
 //expected
 }
 }

 @Test
 public void testParseInvalidQueryWithEmptySelector() {
 query = ":not()";
 try {
 parser = QueryParser.parse(query);
 fail();
 } catch (Selector.SelectorParseException e) {
 //expected
 }
 }
}