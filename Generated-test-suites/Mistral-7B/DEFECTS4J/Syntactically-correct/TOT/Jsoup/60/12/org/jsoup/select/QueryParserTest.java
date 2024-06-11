import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryParserTest {

 private QueryParser queryParser;
 private TokenQueue tokenQueue;

 @BeforeEach
 public void setup() {
 queryParser = new QueryParser("");
 tokenQueue = new TokenQueue("");
 }

 @Test
 public void testParseEmptyQuery() {
 Evaluator evaluator = queryParser.parse("");
 assertTrue(evaluator instanceof CombiningEvaluator.And);
 assertTrue(evaluator.toString().isEmpty());
 }

 @Test
 public void testParseSimpleTagSelector() {
 queryParser.parse("div");
 assertEquals(1, evals.size());
 assertTrue(evals.get(0) instanceof Evaluator.Tag);
 assertEquals("div", evals.get(0).toString());
 }

 @Test
 public void testParseClassSelector() {
 queryParser.parse("p.class1");
 assertEquals(1, evals.size());
 assertTrue(evals.get(0) instanceof Evaluator.Class);
 assertEquals("class1", evals.get(0).toString());
 }

 @Test
 public void testParseIdSelector() {
 queryParser.parse("#id1");
 assertEquals(1, evals.size());
 assertTrue(evals.get(0) instanceof Evaluator.Id);
 assertEquals("id1", evals.get(0).toString());
 }

 @Test
 public void testParseAttributeSelector() {
 queryParser.parse("[class='class1']");
 assertEquals(1, evals.size());
 assertTrue(evals.get(0) instanceof Evaluator.Attribute);
 assertEquals("class", evals.get(0).toString());
 }

 @Test
 public void testParseComplexQuery() {
 queryParser.parse("div.class1#id1 > ul > li:nth-child(2) > a[href='http://example.com']");
 assertEquals(7, evals.size());
 // TODO: check each evaluator in depth
 }

 @Test
 public void testParseCombinedQueries() {
 queryParser.parse("div.class1#id1 > div > p:not(.class2)");
 assertEquals(4, evals.size());
 // TODO: check each evaluator in depth
 }

 @Test
 public void testParseInvalidQuery() {
 queryParser.parse("div>div:not(div"); // missing ')'
 assertThrows(Selector.SelectorParseException.class, () -> queryParser.parse("div>div:not(div"));
 }

 @AfterEach
 public void tearDown() {
 evals.clear();
 }
}