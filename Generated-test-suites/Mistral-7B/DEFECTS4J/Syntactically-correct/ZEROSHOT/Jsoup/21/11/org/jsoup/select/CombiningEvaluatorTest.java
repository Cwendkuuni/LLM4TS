import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Before
 public void setUp() {
 // Set up some test data or initialize any static variables here if needed
 }

 @Test
 public void testAndEvaluator() {
 // Test case for And evaluator
 List<Evaluator> evaluators = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h1")
 );
 And andEvaluator = new And(evaluators);
 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 Element node = root.body();

 assertTrue(andEvaluator.matches(root, node));
 }

 @Test
 public void testAndEvaluator_false() {
 // Test case for And evaluator with a false condition
 List<Evaluator> evaluators = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h2")
 );
 And andEvaluator = new And(evaluators);
 Element root = Element.parse("<html><body><p>Test</p><h2>Test</h2></body></html>");
 Element node = root.body();

 assertFalse(andEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator() {
 // Test case for Or evaluator with two conditions
 List<Evaluator> evaluators1 = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h1")
 );
 Or orEvaluator1 = new Or(evaluators1);

 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 Element node = root.body();

 assertTrue(orEvaluator1.matches(root, node));

 List<Evaluator> evaluators2 = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h2")
 );
 Or orEvaluator2 = new Or(evaluators2);

 Element root2 = Element.parse("<html><body><p>Test</p><h2>Test</h2></body></html>");
 Element node2 = root2.body();

 assertTrue(orEvaluator2.matches(root2, node2));
 }

 @Test
 public void testOrEvaluator_false() {
 // Test case for Or evaluator with no matching conditions
 List<Evaluator> evaluators = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h3")
 );
 Or orEvaluator = new Or(evaluators);
 Element root = Element.parse("<html><body><p>Test</p><h1>Test</h1></body></html>");
 Element node = root.body();

 assertFalse(orEvaluator.matches(root, node));
 }

 @Test
 public void testOrEvaluator_addEvaluator() {
 // Test case for adding a new evaluator to Or evaluator
 List<Evaluator> evaluators = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h1")
 );
 Or orEvaluator = new Or(evaluators);

 orEvaluator.add(new ElementFilter("h2"));
 List<Evaluator> newEvaluators = Arrays.asList(
 new ElementFilter("p"),
 new ElementFilter("h1"),
 new ElementFilter("h2")
 );

 assertTrue(Arrays.equals(orEvaluator.evaluators, newEvaluators));
 }
}