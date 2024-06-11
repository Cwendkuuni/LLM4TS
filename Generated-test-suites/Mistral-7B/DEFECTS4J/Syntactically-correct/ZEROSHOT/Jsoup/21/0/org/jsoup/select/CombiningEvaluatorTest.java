package org.jsoup.select.test;

import org.jsoup.helper.ElementFormatter;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {

 private static final String HTML = "<html><body><div id='a'><p>Test 1</p><p>Test 2</p></div>" +
         "<div id='b'><p>Test 3</p></div></body></html>";

 static Element parseElement(String selector) {
     return ElementFormatter.parseHtmlFragment(HTML, selector).select("html body").get(0);
 }

 @Test
 public void testAndEvaluator() {
     List<Evaluator> evaluators = Arrays.asList(
             new ElementSelector("#a"),
             new ClassSelector(Element.Class.PARAGRAPH)
     );

     And andEvaluator = new And(evaluators);
     Element expected = parseElement("#a > p");

     Element actual = andEvaluator.matches(null, null);
     assertTrue("Expected element to match: " + expected, ElementFormatter.isSameNode(expected, actual));

     List<Evaluator> wrongEvaluators = Arrays.asList(
             new ElementSelector("#b"),
             new ClassSelector(Element.Class.PARAGRAPH)
     );

     And wrongAndEvaluator = new And(wrongEvaluators);
     assertFalse("Expected element not to match: " + parseElement("#a > p"), wrongAndEvaluator.matches(null, parseElement("#a > p")));
  }

 @Test
 public void testOrEvaluator() {
     List<Evaluator> firstClause = Arrays.asList(
             new ElementSelector("#a"),
             new ClassSelector(Element.Class.PARAGRAPH)
     );
     And firstClauseAnd = new And(firstClause);

     List<Evaluator> secondClause = Arrays.asList(
             new ElementSelector("#b")
     );
     Or orEvaluator = new Or(secondClause);
     orEvaluator.add(firstClauseAnd);

     Element aP = parseElement("#a > p:first-of-type");
     Element b = parseElement("#b");

     Element aPExpected = aP;
     Element bExpected = b;

     Element aPExpectedMatched = orEvaluator.matches(null, aP);
     assertTrue("Expected element to match: " + aPExpectedMatched, ElementFormatter.isSameNode(aPExpected, aPExpectedMatched));

     Element bExpectedMatched = orEvaluator.matches(null, b);
     assertTrue("Expected element to match: " + bExpectedMatched, ElementFormatter.isSameNode(bExpected, bExpectedMatched));

     Element c = parseElement("#nonExistent");
     assertFalse("Expected element not to match: " + c, orEvaluator.matches(null, c));
 }
}