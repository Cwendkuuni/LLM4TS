import org.jsoup.parser.QueryParser;
import org.junit.Test;
import org.junit.Assert.*;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1 p.class2";
 private static final String BASIC_QUERY_PARSED = "div.class1 > p.class2";

 @Test
 public void testBasicQueryParsing() {
 QueryParser parser = new QueryParser(BASIC_QUERY);
 Evaluator parsedQuery = parser.parse();
 String expectedQuery = BASIC_QUERY_PARSED;

 StructuralEvaluator.And expectedAnd = new StructuralEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new StructuralEvaluator.Class("class1"),
 new StructuralEvaluator.Tag("p"),
 new StructuralEvaluator.Class("class2")
 );

 Assert.assertEquals(expectedAnd.toString(), parsedQuery.toString());
 }

 private static final String COMBINATOR_QUERY = "div > p ~ ul";
 private static final String COMBINATOR_QUERY_PARSED = "div > p > ~ ul";

 @Test
 public void testCombinatorQueryParsing() {
 QueryParser parser = new QueryParser(COMBINATOR_QUERY);
 Evaluator parsedQuery = parser.parse();
 String expectedQuery = COMBINATOR_QUERY_PARSED;

 StructuralEvaluator.And expectedAnd = new StructuralEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new StructuralEvaluator.Tag("p"),
 new StructuralEvaluator.PreviousSibling(new StructuralEvaluator.Tag("ul"))
 );

 Assert.assertEquals(expectedAnd.toString(), parsedQuery.toString());
 }

 private static final String ATTRIBUTE_QUERY = "input[type='text'][name='username']";
 private static final String ATTRIBUTE_QUERY_PARSED = "input[type='text'][@name='username']";

 @Test
 public void testAttributeQueryParsing() {
 QueryParser parser = new QueryParser(ATTRIBUTE_QUERY);
 Evaluator parsedQuery = parser.parse();
 String expectedQuery = ATTRIBUTE_QUERY_PARSED;

 StructuralEvaluator.And expectedAnd = new StructuralEvaluator.And(
 new StructuralEvaluator.Tag("input"),
 new AttributeEvaluator("type", "text"),
 new AttributeEvaluator("name", "username")
 );

 Assert.assertEquals(expectedAnd.toString(), parsedQuery.toString());
 }

 private static final String ID_QUERY = "#search-box";

 @Test
 public void testIdQueryParsing() {
 QueryParser parser = new QueryParser(ID_QUERY);
 Evaluator parsedQuery = parser.parse();
 String expectedQuery = "tag[id='search-box']";

 StructuralEvaluator.Id expectedId = new StructuralEvaluator.Id("search-box");

 Assert.assertEquals(expectedId.toString(), parsedQuery.toString());
 }

 private static final String MULTIPLE_QUERIES_QUERY = "div > p.class1, div > ul > li.class2";
 private static final String MULTIPLE_QUERIES_QUERY_PARSED = "div > p.class1, div > ul > li.class2";

 @Test
 public void testMultipleQueriesParsing() {
 QueryParser parser = new QueryParser(MULTIPLE_QUERIES_QUERY);
 Evaluator parsedQuery = parser.parse();
 String expectedQuery = MULTIPLE_QUERIES_QUERY_PARSED;

 StructuralEvaluator.Or expectedOr = new StructuralEvaluator.Or(
 new StructuralEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new StructuralEvaluator.Tag("p"),
 new StructuralEvaluator.Class("class1")
 ),
 new StructuralEvaluator.And(
 new StructuralEvaluator.Tag("div"),
 new StructuralEvaluator.Tag("ul"),
 new StructuralEvaluator.Tag("li"),
 new StructuralEvaluator.Class("class2")
 )
 );

 Assert.assertEquals(expectedOr.toString(), parsedQuery.toString());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testInvalidQueryParsing() {
 QueryParser parser = new QueryParser("*");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testMissingCombinatorQueryParsing() { {QueryParser parser = new QueryParser("div input");
 parser.parse();}
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testMissingAttributeNameQueryParsing() { {QueryParser parser = new QueryParser("input[value]");
 parser.parse();}
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testMissingAttributeValueQueryParsing() { {QueryParser parser = new QueryParser("input[type]");
 parser.parse();}
 }

 @Test
 public void testParseQueryWithErrorHandling() {
 String invalidQuery = "div > p.class1 div";
 QueryParser parser = null;
 try {
 parser = new QueryParser(invalidQuery);
 parser.parse();
 } catch (Selector.SelectorParseException e) {
 // Expected exception
 String expectedMessage = "Unknown combinator: div";
 String actualMessage = e.getMessage();

 // Check message
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }
}