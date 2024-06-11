import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Parser;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class QueryParserTest {

 private static String query = "div.class1 > div.class2:not(.class3)";
 private static String query1 = "div.class1 > div.class2 > div.class3";
 private static String query2 = "div.class1 ~ div.class2";
 private static String query3 = "div.class1 + div.class2";
 private static String query4 = "div.class1 #id1";
 private static String query5 = "div.class1.class2";
 private static String query6 = "div.class1[title='Test']";
 private static String query7 = "div:contains(Test)";
 private static String query8 = "div:has(p:contains(Test))";
 private static String query9 = "div:matchesOwn(^(abc:def)$)";
 private static String query10 = "div:lt(5)";
 private static String query11 = "div:gt(5)";
 private static String query12 = "div:eq(5)";

 @Test
 public void testParseSimpleQuery() {
 QueryParser parser = new QueryParser(query);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class1"));
 Evaluator expected = new CombiningEvaluator.And(elements);
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseComplexQuery() {
 QueryParser parser = new QueryParser(query1);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class1"));
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class2"));
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class3"));
 Evaluator expected = new CombiningEvaluator.And(new StructuralEvaluator.ImmediateParent(elements.get(0)), new StructuralEvaluator.ImmediateChild(elements.get(2)));
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseSiblingRelationQuery() {
 QueryParser parser = new QueryParser(query2);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class1"));
 Evaluator expected = new StructuralEvaluator.PreviousSibling(elements.get(0));
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseNextSiblingRelationQuery() {
 QueryParser parser = new QueryParser(query3);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class1"));
 Evaluator expected = new StructuralEvaluator.ImmediatePreviousSibling(elements.get(0));
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseIdQuery() {
 QueryParser parser = new QueryParser(query4);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Attribute("id", "id1"));
 Evaluator expected = new Evaluator.Id("id1");
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseTagAndClassQuery() {
 QueryParser parser = new QueryParser(query5);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Class("class1"));
 elements.add(new Evaluator.Class("class2"));
 Evaluator expected = new CombiningEvaluator.And(elements.get(0), new Evaluator.Class("class2"));
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseAttributeQuery() {
 QueryParser parser = new QueryParser(query6);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 elements.add(new Evaluator.Attribute("title", "Test"));
 Evaluator expected = new Evaluator.ContainsText("Test");
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseContainsQuery() {
 QueryParser parser = new QueryParser(query7);
 Evaluator evaluator = parser.parse();
 // typical use case
 Evaluator expected = new Evaluator.ContainsText("Test");
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseHasQuery() {
 QueryParser parser = new QueryParser(query8);
 Evaluator evaluator = parser.parse();
 // typical use case
 Evaluator child = new Evaluator.Tag("p");
 Evaluator childText = new Evaluator.ContainsText("Test");
 Evaluator parent = new Evaluator.Tag("div");
 Evaluator expected = new StructuralEvaluator.Has(new CombiningEvaluator.And(child, childText), parent);
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseMatchesQuery() { // for matchesOwn test, replace "evaluator" with "expected" and swap them in the next assert statement
 QueryParser parser = new QueryParser(query9);
 Evaluator evaluator = parser.parse();
 // typical use case
 String regex = "^(abc:def)$";
 Pattern pattern = Pattern.compile(regex);
 Evaluator expected = new Evaluator.MatchesOwn(pattern);
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseIndexQuery() {
 QueryParser parser = new QueryParser(query10);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 Evaluator expected = new Evaluator.IndexLessThan(5);
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseIndexGreaterQuery() {
 QueryParser parser = new QueryParser(query11);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 Evaluator expected = new Evaluator.IndexGreaterThan(5);
 assertTrue(evaluator.equals(expected));
 }

 @Test
 public void testParseIndexEqualsQuery() {
 QueryParser parser = new QueryParser(query12);
 Evaluator evaluator = parser.parse();
 // typical use case
 List<Evaluator> elements = new ArrayList<>();
 elements.add(new Evaluator.Tag("div"));
 Evaluator expected = new Evaluator.IndexEquals(5);
 assertTrue(evaluator.equals(expected));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser parser = new QueryParser("invalidquery");
 Evaluator evaluator = parser.parse();
 fail("Unexpected result: " + evaluator);
 }
}