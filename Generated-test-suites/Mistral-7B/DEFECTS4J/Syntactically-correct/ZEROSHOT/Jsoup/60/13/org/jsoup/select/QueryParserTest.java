import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.Test;
import org.junit.Assert.*;

public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;

 @Before
 public void setUp() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 }

 @Test
 public void testParseEmptyQuery() {
 Evaluator evaluator = queryParser.parse("");
 Assert.assertEquals(Evaluator.class, evaluator.getClass());
 Assert.assertTrue(evaluator instanceof Evaluator.IsRoot);
 }

 @Test
 public void testParseSimpleTagSelector() {
 tokenQueue.append("p");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 Assert.assertEquals(Evaluator.Tag.class, evaluator.getClass());
 Assert.assertEquals("p", ((Evaluator.Tag) evaluator).tagName);
 }

 @Test
 public void testParseClassSelector() {
 tokenQueue.append(".");
 tokenQueue.append("class");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 Assert.assertEquals(Evaluator.Class.class, evaluator.getClass());
 Assert.assertEquals("class", ((Evaluator.Class) evaluator).className);
 }

 @Test
 public void testParseIdSelector() {
 tokenQueue.append("#id");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 Assert.assertEquals(Evaluator.Id.class, evaluator.getClass());
 Assert.assertEquals("id", ((Evaluator.Id) evaluator).id);
 }

 @Test
 public void testParseCombinedSelectors() {
 tokenQueue.append("p > ul > li");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 CombiningEvaluator and = (CombiningEvaluator) evaluator;
 Assert.assertEquals(CombiningEvaluator.And.class, and.getClass());
 StructuralEvaluator.ImmediateParent immediateParent = (StructuralEvaluator.ImmediateParent) and.getLeft();
 Assert.assertEquals(StructuralEvaluator.ImmediateParent.class, immediateParent.getClass());
 Assert.assertEquals(Evaluator.Tag.class, immediateParent.getEvaluator().getClass());
 Assert.assertEquals("ul", ((Evaluator.Tag) immediateParent.getEvaluator()).tagName);
 Evaluator rightEvaluator = and.getRight();
 Assert.assertEquals(Evaluator.Tag.class, rightEvaluator.getClass());
 Assert.assertEquals("li", ((Evaluator.Tag) rightEvaluator).tagName);
 }

 @Test
 public void testParseAttributeSelectors() {
 tokenQueue.append("p[class='test']");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 AttributeEvaluator attributeEvaluator = (AttributeEvaluator) evaluator;
 Assert.assertEquals(AttributeEvaluator.class, attributeEvaluator.getClass());
 Assert.assertEquals("class", attributeEvaluator.attributeName);
 Assert.assertEquals("test", ((AttributeEvaluator) evaluator).value);
 }

 @Test
 public void testParseAttributeSelectorsWithModifier() {
 tokenQueue.append("p[class^='test']");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 AttributeEvaluator attributeEvaluator = (AttributeEvaluator) evaluator;
 Assert.assertEquals(AttributeEvaluator.AttributeStarting.class, attributeEvaluator.getClass());
 Assert.assertEquals("class", attributeEvaluator.attributeName);
 Assert.assertEquals("test", ((AttributeEvaluator) evaluator).value);
 }

 @Test
 public void testParsePseudoClasses() {
 tokenQueue.append("p:first-child");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 Assert.assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
 }

 @Test
 public void testParsePseudoElements() {
 tokenQueue.append("p:first-of-type");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 Assert.assertEquals(Evaluator.IsFirstOfType.class, evaluator.getClass());
 }

 @Test
 public void testParseComplexQuery() {
 tokenQueue.append("ul > li.test:has(> p > a[href^='mailto:'])");
 queryParser.query = tokenQueue.toString();
 Evaluator evaluator = queryParser.parse();
 CombiningEvaluator and = (CombiningEvaluator) evaluator;
 StructuralEvaluator.ImmediateParent immediateParent = (StructuralEvaluator.ImmediateParent) and.getLeft();
 StructuralEvaluator.Parent parent = (StructuralEvaluator.Parent) immediateParent.getEvaluator();
 Assert.assertEquals(StructuralEvaluator.Parent.class, parent.getClass());
 Assert.assertEquals(Evaluator.Tag.class, parent.getEvaluator().getClass());
 Assert.assertEquals("ul", ((Evaluator.Tag) parent.getEvaluator()).tagName);
 AttributeEvaluator classAttributeEvaluator = (AttributeEvaluator) and.getRight();
 Assert.assertEquals(AttributeEvaluator.Class.class, classAttributeEvaluator.getClass());
 Assert.assertEquals("test", ((AttributeEvaluator) classAttributeEvaluator).className);
 StructuralEvaluator.Has hasEvaluator = (StructuralEvaluator.Has) and.getRightMostEvaluator();
 Evaluator innerEvaluator = hasEvaluator.getInnerEvaluator();
 StructuralEvaluator.Descendant descendantEvaluator = (StructuralEvaluator.Descendant) innerEvaluator;
 Evaluator tagEvaluator = descendantEvaluator.getTagEvaluator();
 Assert.assertEquals(Evaluator.Tag.class, tagEvaluator.getClass());
 Assert.assertEquals("a", ((Evaluator.Tag) tagEvaluator).tagName);
 AttributeEvaluator hrefAttributeEvaluator = (AttributeEvaluator) innerEvaluator;
 Assert.assertEquals(AttributeEvaluator.AttributeWithValue.class, hrefAttributeEvaluator.getClass());
 Assert.assertEquals("href", hrefAttributeEvaluator.attributeName);
 String hrefValue = ((AttributeEvaluator) hrefAttributeEvaluator).value;
 Matcher hrefMatcher = Pattern.compile("^mailto:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matcher(hrefValue);
 assertTrue(hrefMatcher.matches());
 }
}