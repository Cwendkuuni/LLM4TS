package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 // Test case 1: All evaluators match
 Element root = Element.parse("<html><body><div id='test'><p>Test</p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementStartsEvaluator("body"),
 new ElementHasAttribute("div", "id", "test")
 ));
 assertTrue(andEvaluator.matches(root, root));

 // Test case 2: One evaluator does not match
 andEvaluator = new And(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementStartsEvaluator("body"),
 new ElementHasAttribute("div", "id", "test2")
 ));
 assertFalse(andEvaluator.matches(root, root));

 // Test case 3: Test empty list
 andEvaluator = new And(new ArrayList<>());
 assertFalse(andEvaluator.matches(root, root));
 }

 @Test
 public void testOrEvaluator() {
 // Test case 1: All evaluators match
 Element root = Element.parse("<html><body><div id='test'><p>Test</p></div></body></html>");
 Or orEvaluator = new Or(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementStartsEvaluator("body"),
 new ElementHasAttribute("div", "id", "test")
 ));
 assertTrue(orEvaluator.matches(root, root));

 // Test case 2: First evaluator matches
 orEvaluator = new Or(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementStartsEvaluator("body"),
 new ElementHasAttribute("div", "id", "test")
 ), new ElementHasAttribute("p", "class", "test"));
 assertTrue(orEvaluator.matches(root, root.selectFirst("p.test")));

 // Test case 3: None evaluators match
 Or orEvaluatorFailure = new Or(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementHasAttribute("div", "id", "test2")
 ));
 assertFalse(orEvaluatorFailure.matches(root, root));

 // Test case 4: Test add method
 Or orEvaluatorAdd = new Or(Arrays.asList(
 new ElementStartsEvaluator("html"),
 new ElementStartsEvaluator("body")
 ));
 orEvaluatorAdd.add(new ElementHasAttribute("div", "id", "test"));
 assertTrue(orEvaluatorAdd.matches(root, root));
 }
}