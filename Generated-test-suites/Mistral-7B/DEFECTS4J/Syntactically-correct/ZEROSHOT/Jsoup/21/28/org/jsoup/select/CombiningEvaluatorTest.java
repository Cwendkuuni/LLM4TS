package org.jsoup.select.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementHelper;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = ElementHelper.parse("<html><body><div id='a'><p>1</p><p>2</p></div>" +
 "<div id='b'><p>3</p><p>4</p></div></body></html>");
 }

 @Test
 public void testAndEvaluator() {
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("#a > p:contains(1)"),
 new ElementSelector("#a > p:contains(2)")
 );
 And andEvaluator = new And(evaluators);
 Element pElement = doc.selectFirst(andEvaluator.toString());
 assertTrue(andEvaluator.matches(doc.body(), pElement));
 }

 @Test
 public void testAndEvaluatorMatchesFalse() {
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("#a > p:contains(1)"),
 new ElementSelector("#b > p:contains(1)")
 );
 And andEvaluator = new And(evaluators);
 Element pElement = doc.selectFirst(andEvaluator.toString());
 assertFalse(andEvaluator.matches(doc.body(), pElement));
 }

 @Test
 public void testOrEvaluator() {
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("#a > p:contains(1)"),
 new ElementSelector("#b > p:contains(3)")
 );
 Or orEvaluator = new Or(evaluators);
 Element pElement = doc.selectFirst(orEvaluator.toString());
 assertTrue(orEvaluator.matches(doc.body(), pElement));
 }

 @Test
 public void testOrEvaluatorMatchesFalse() {
 List<Evaluator> evaluators = Arrays.asList(
 new ElementSelector("#a > p:contains(1)"),
 new ElementSelector("#b > p:contains(4)")
 );
 Or orEvaluator = new Or(evaluators);
 Element pElement = doc.selectFirst(orEvaluator.toString());
 assertFalse(orEvaluator.matches(doc.body(), pElement));
 }

 @Test
 public void testOrWithMultipleEvaluators() {
 List<Evaluator> evaluators = new ArrayList<>();
 evaluators.add(new ElementSelector("#a > p:contains(1)"));
 evaluators.add(new ElementSelector("#b > p:contains(3)"));
 evaluators.add(new ElementSelector("#b > p:contains(4)"));
 Or orEvaluator = new Or(evaluators);
 Element pElement = doc.selectFirst(orEvaluator.toString());
 assertTrue(orEvaluator.matches(doc.body(), pElement));
 }
}