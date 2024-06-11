import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QueryParserTest {

 private static Evaluator parse(String query) {
 return QueryParser.parse(query);
 }

 @Test
 public void testParseEmptyQuery() {
 QueryParser parser = new QueryParser("");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseRootQuery() {
 QueryParser parser = new QueryParser("*");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(StructuralEvaluator.Root.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleTagQuery() {
 QueryParser parser = new QueryParser("div");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", ((Evaluator.Tag) evaluator).getTagName());
 }

 @Test
 public void testParseTagWithClassQuery() {
 QueryParser parser = new QueryParser(".class");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator tagEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Tag.class, tagEvaluator.getClass());
 assertEquals("class", ((Evaluator.Tag) tagEvaluator).getTagName());
 Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Class.class, classEvaluator.getClass());
 assertEquals("class", ((Evaluator.Class) classEvaluator).getClassValue());
 }

 @Test
 public void testParseTagWithIdQuery() {
 QueryParser parser = new QueryParser("#id");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("id", ((Evaluator.Id) evaluator).getIdValue());
 }

 @Test
 public void testParseSimpleCombinatorQuery() {
 QueryParser parser = new QueryParser("div > p");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator parentEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(StructuralEvaluator.ImmediateParent.class, parentEvaluator.getClass());
 Evaluator childEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
 assertEquals("p", ((Evaluator.Tag) childEvaluator).getTagName());
 }

 @Test
 public void testParseComplexQueryWithIdAndClass() {
 QueryParser parser = new QueryParser("#id.class");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 Evaluator idEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Id.class, idEvaluator.getClass());
 assertEquals("id", ((Evaluator.Id) idEvaluator).getIdValue());
 Evaluator classEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Class.class, classEvaluator.getClass());
 assertEquals("class", ((Evaluator.Class) classEvaluator).getClassValue());
 }

 @Test
 public void testParseComplexQueryWithMultipleCombinators() {
 QueryParser parser = new QueryParser("div > p > span ~ ul > li:eq(2)");
 Evaluator evaluator = parser.parse();
 assertNotNull(evaluator);
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> evaluators = ((CombiningEvaluator.And) evaluator).getEvaluators();
 // checking first evaluator
 Evaluator parentEvaluator = evaluators.get(0);
 assertEquals(StructuralEvaluator.ImmediateParent.class, parentEvaluator.getClass());
 Evaluator childEvaluator = evaluators.get(1);
 assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
 assertEquals("p", ((Evaluator.Tag) childEvaluator).getTagName());
 // checking second evaluator
 parentEvaluator = evaluators.get(1);
 evaluators = ((CombiningEvaluator.And) parentEvaluator).getEvaluators();
 childEvaluator = evaluators.get(0);
 assertEquals(Evaluator.Tag.class, childEvaluator.getClass());
 assertEquals("span", ((Evaluator.Tag) childEvaluator).getTagName());
 // checking third evaluator
 parentEvaluator = evaluators.get(1);
 evaluators = ((CombiningEvaluator.And) parentEvaluator).getEvaluators();
 childEvaluator = evaluators.get(0);
 assertEquals(StructuralEvaluator.PreviousSibling.class, childEvaluator.getClass());
 Evaluator siblingEvaluator = evaluators.get(1);
 assertEquals(Evaluator.Tag.class, siblingEvaluator.getClass());
 assertEquals("ul", ((Evaluator.Tag) siblingEvaluator).getTagName());
 // checking fourth evaluator
 parentEvaluator = evaluators.get(1);
 evaluators = ((CombiningEvaluator.And) parentEvaluator).getEvaluators();
 childEvaluator = evaluators.get(0);
 assertEquals(CombiningEvaluator.And.class, childEvaluator.getClass());
 Evaluator indexEvaluator = ((CombiningEvaluator.And) childEvaluator).getEvaluators().get(0);
 assertEquals(Evaluator.Index.class, indexEvaluator.getClass());
 assertEquals(2, ((Evaluator.Index) indexEvaluator).getIndexValue());
 Evaluator liEvaluator = ((CombiningEvaluator.And) childEvaluator).getEvaluators().get(1);
 assertEquals(Evaluator.Tag.class, liEvaluator.getClass());
 assertEquals("li", ((Evaluator.Tag) liEvaluator).getTagName());
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMissingCombinator() {
 QueryParser parser = new QueryParser("div p");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithInvalidCombinator() {
 QueryParser parser = new QueryParser("div % p");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMissingElement() {
 QueryParser parser = new QueryParser("div >");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMissingAttribute() {
 QueryParser parser = new QueryParser("div[attribute]");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithUnknownAttributeOperator() {
 QueryParser parser = new QueryParser("div[attribute~=value]");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMultipleAttributeOperators() {
 QueryParser parser = new QueryParser("div[attribute=value][attribute~=value]");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithUnescapedCharacter() {
 QueryParser parser = new QueryParser("div[class=class1][class=class2]");
 parser.parse();
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQueryWithMissingClosingBracket() {
 QueryParser parser = new QueryParser("div[class=class1][class=class2](");
 parser.parse();
 }
}