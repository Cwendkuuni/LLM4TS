import org.jsoup.parser.QueryParser;
import org.junit.Test;
import org.junit.Assert.*;

public class QueryParserTest {

 private static final String TEST_QUERY_1 = ".class1 > .class2";
 private static final String TEST_QUERY_2 = "#id1 ~ .class3";
 private static final String TEST_QUERY_3 = "div.class1:contains(text)";
 private static final String TEST_QUERY_4 = "div#id1:not(.class1)";

 @Test
 public void testParse_EmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertTrue(evaluator instanceof Evaluator.AllElements);
 }

 @Test
 public void testParse_SimpleTagQuery() {
 QueryParser queryParser = new QueryParser("p");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("p", evaluator.toString());
 }

 @Test
 public void testParse_ClassQuery() {
 QueryParser queryParser = new QueryParser(".class1");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".class1", evaluator.toString());
 }

 @Test
 public void testParse_IdQuery() {
 QueryParser queryParser = new QueryParser("#id1");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#id1", evaluator.toString());
 }

 @Test
 public void testParse_CombinationQuery() {
 QueryParser queryParser = new QueryParser(TEST_QUERY_1);
 Evaluator evaluator = queryParser.parse();
 CombiningEvaluator and = (CombiningEvaluator) evaluator;
 StructuralEvaluator.ImmediateParent immediateParent = and.get(1);
 assertEquals("class2", immediateParent.getChild().toString());
 assertEquals("class1", immediateParent.getParent().toString());
 }

 @Test
 public void testParse_SiblingQuery() {
 QueryParser queryParser = new QueryParser(TEST_QUERY_2);
 Evaluator evaluator = queryParser.parse();
 CombiningEvaluator and = (CombiningEvaluator) evaluator;
 StructuralEvaluator.PreviousSibling previousSibling = and.get(1);
 assertEquals(".class3", previousSibling.getPreviousSibling().toString());
 StructuralEvaluator.Id id = (StructuralEvaluator.Id) and.get(0);
 assertEquals("id1", id.getId());
 }

 @Test
 public void testParse_ContainsTextQuery() {
 QueryParser queryParser = new QueryParser(TEST_QUERY_3);
 Evaluator evaluator = queryParser.parse();
 Evaluator.ContainsText containsText = (Evaluator.ContainsText) evaluator;
 assertEquals("text", containsText.getText());
 }

 @Test
 public void testParse_NotQuery() {
 QueryParser queryParser = new QueryParser(TEST_QUERY_4);
 Evaluator evaluator = queryParser.parse();
 StructuralEvaluator.Not not = (StructuralEvaluator.Not) evaluator;
 Evaluator child = not.getChild();
 assertEquals(Evaluator.Class.class, child.getClass());
 assertEquals(".class1", child.toString());
 }

}