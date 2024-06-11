package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.ElementHelper;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 Element root = ElementHelper.parseHtml("<html><body><div id='test'>foo bar baz qux</div></body></html>");
 And andEvaluator1 = new And(Arrays.asList(new TagNameEvaluator("div"), new AttributeEvaluator("id", "test")));
 And andEvaluator2 = new And(Arrays.asList(new TagNameEvaluator("div"), new ClassNameEvaluator("highlighted")));

 Element found1 = null;
 Element found2 = null;

 for (Element e : root.select("div.test")) {
 if (andEvaluator1.matches(root, e)) {
 found1 = e;
 break;
 }
 }

 for (Element e : root.select("div.highlighted")) {
 if (andEvaluator2.matches(root, e)) {
 found2 = e;
 break;
 }
 }

 assertTrue(found1 != null);
 assertTrue(found2 != null);
 assertFalse(found1.equals(found2));
 }

 @Test
 public void testOrEvaluator() {
 Element root = ElementHelper.parseHtml("<html><body><div id='test1' class='foo'>foo</div><div id='test2' class='foo bar'>bar</div></body></html>");
 Or orEvaluator = new Or();
 orEvaluator.add(new TagNameEvaluator("div"));
 orEvaluator.add(new AttributeEvaluator("class", "foo"));

 Element found = null;

 for (Element e : root.select(":or(" + orEvaluator.toString() + ")")) {
 found = e;
 break;
 }

 assertTrue(found != null);
 }

 @Test
 public void testNestedOrAndEvaluators() {
 Element root = ElementHelper.parseHtml("<html><body><div id='test1' class='foo'>foo</div><div id='test2' class='bar'>bar</div></body></html>");
 And andCondition1 = new And(Arrays.asList(new AttributeEvaluator("id", "test1"), new ClassNameEvaluator("foo")));
 Or orCondition1 = new Or();
 orCondition1.add(andCondition1);
 And andCondition2 = new And(Arrays.asList(new AttributeEvaluator("id", "test2"), new ClassNameEvaluator("bar")));
 orCondition1.add(andCondition2);

 Element found = null;

 for (Element e : root.select(":or(" + orCondition1.toString() + ")")) {
 found = e;
 break;
 }

 assertTrue(found != null);
 }
}