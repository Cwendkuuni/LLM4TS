import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueryParserTest {

 @Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.class, evaluator.getClass());
 }

 @Test
 public void testParseSimpleElementSelector() {
 QueryParser queryParser = new QueryParser("div");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Tag.class, evaluator.getClass());
 assertEquals("div", evaluator.toString());
 }

 @Test
 public void testParseClassSelector() {
 QueryParser queryParser = new QueryParser(".className");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Class.class, evaluator.getClass());
 assertEquals(".className", evaluator.toString());
 }

 @Test
 public void testParseIDSelector() {
 QueryParser queryParser = new QueryParser("#id");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Id.class, evaluator.getClass());
 assertEquals("#id", evaluator.toString());
 }

 @Test
 public void testParseCombinator() {
 QueryParser queryParser = new QueryParser("div > p");
 Evaluator evaluator = queryParser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> elements = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(2, elements.size());
 assertEquals(Evaluator.Tag.class, elements.get(0).getClass());
 assertEquals("div", ((Evaluator.Tag) elements.get(0)).getTagName());
 assertEquals(StructuralEvaluator.ImmediateParent.class, elements.get(1).getClass());
 }

 @Test
 public void testParseCombinatorWithError() {
 QueryParser queryParser = new QueryParser("div > p > x");
 try {
 Evaluator evaluator = queryParser.parse();
 fail("Expected SelectorParseException but got Evaluator instance");
 } catch (Selector.SelectorParseException e) {
 // expected
 }
 }

 @Test
 public void testParseAttributeSelector() {
 QueryParser queryParser = new QueryParser("[title]");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.Attribute.class, evaluator.getClass());
 assertEquals("[title]", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValue() {
 QueryParser queryParser = new QueryParser("[title='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValue.class, evaluator.getClass());
 assertEquals("[title='Java']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValueNot() {
 QueryParser queryParser = new QueryParser("[title!='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueNot.class, evaluator.getClass());
 assertEquals("[title!='Java']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValueStarting() {
 QueryParser queryParser = new QueryParser("[title^='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
 assertEquals("[title^='Java']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValueEnding() {
 QueryParser queryParser = new QueryParser("[title$='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
 assertEquals("[title$='Java']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValueContaining() {
 QueryParser queryParser = new QueryParser("[title*='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
 assertEquals("[title*='Java']", evaluator.toString());
 }

 @Test
 public void testParseAttributeSelectorWithValueMatching() {
 QueryParser queryParser = new QueryParser("[title~='Java']");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
 assertEquals("[title~='Java']", evaluator.toString());
 }

 @Test
 public void testParsePseudoSelectorContains() {
 QueryParser queryParser = new QueryParser(":contains(Java)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
 assertEquals(":contains(Java)", evaluator.toString());
 }

 @Test
 public void testParsePseudoSelectorContainsOwn() {
 QueryParser queryParser = new QueryParser(":containsOwn(Java)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
 assertEquals(":containsOwn(Java)", evaluator.toString());
 }

 @Test
 public void testParsePseudoSelectorMatches() {
 QueryParser queryParser = new QueryParser(":matches(div.className:containsOwn(Java))");
 Evaluator evaluator = queryParser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> elements = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(2, elements.size());
 Evaluator firstElement = elements.get(0);
 assertEquals(Evaluator.Tag.class, firstElement.getClass());
 assertEquals("div", ((Evaluator.Tag) firstElement).getTagName());
 Evaluator secondElement = elements.get(1);
 assertEquals(Evaluator.ContainsOwnText.class, secondElement.getClass());
 assertEquals("Java", ((Evaluator.ContainsOwnText) secondElement).getText());
 }

 @Test
 public void testParsePseudoSelectorMatchesOwn() {
 QueryParser queryParser = new QueryParser(":matchesOwn(div:containsOwn(Java))");
 Evaluator evaluator = queryParser.parse();
 assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
 List<Evaluator> elements = ((CombiningEvaluator.And) evaluator).getChildren();
 assertEquals(1, elements.size());
 Evaluator element = elements.get(0);
 assertEquals(StructuralEvaluator.Has.class, element.getClass());
 Evaluator hasElement = ((StructuralEvaluator.Has) element).getSubSelector();
 assertEquals(Evaluator.ContainsOwnText.class, hasElement.getClass());
 assertEquals("Java", ((Evaluator.ContainsOwnText) hasElement).getText());
 }

 @Test
 public void testParsePseudoSelectorNot() {
 QueryParser queryParser = new QueryParser(":not(p)");
 Evaluator evaluator = queryParser.parse();
 assertEquals(StructuralEvaluator.Not.class, evaluator.getClass());
 }
}