import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.StringUtil;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 private static final String HTML = "<html><body><div id='div1' class='class1 class2'><p>Test and</p></div>" +
         "<div id='div2' class='class3'><p>Test or</p></div></body></html>";

 private Element root;

 @Before
 public void setUp() {
 root = Jsoup.parse(HTML);
 }

 @Test
 public void testAndEvaluator() {
 List<Evaluator> evaluators = Arrays.asList(
         new ElementSelector("div.class1"),
         new ElementSelector("div.class2")
 );
 And andEvaluator = new And(evaluators);
 assertTrue(andEvaluator.matches(root, root.selectFirst("div#div1")));
 assertFalse(andEvaluator.matches(root, root.selectFirst("div#div2")));
 }

 @Test
 public void testOrEvaluator() {
 List<Evaluator> evaluators = Arrays.asList(
         new ElementSelector("div.class1"),
         new ElementSelector("div.class3")
 );
 Or orEvaluator = new Or(evaluators);
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div1")));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div2")));

 // test adding new evaluator
 orEvaluator.add(new ElementSelector("div.class4"));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div1")));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div2")));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div1").selectFirst("p")));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div2").selectFirst("p")));
 }

 @Test
 public void testEmptyOrEvaluator() {
 Or orEvaluator = new Or(Collections.emptyList());
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div1")));
 assertTrue(orEvaluator.matches(root, root.selectFirst("div#div2")));
 }

 @Test
 public void testNullOrEvaluator() {
 Or orEvaluator = new Or(null);
 assertFalse(orEvaluator.matches(root, root.selectFirst("div#div1")));
 assertFalse(orEvaluator.matches(root, root.selectFirst("div#div2")));
 }
}