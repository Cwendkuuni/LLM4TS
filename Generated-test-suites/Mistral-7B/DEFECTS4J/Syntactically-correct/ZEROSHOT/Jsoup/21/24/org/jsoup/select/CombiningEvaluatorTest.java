package org.jsoup.select.test;

import org.junit.Test;
import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

 private static Document doc = ElementFinder.getDocumentFromString("<html><body><div id='a'><p class='b c'></p><p class='b d'></p></div></body></html>");

 @Test
 public void testAndEvaluator() {
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 ));
 Element element = andEvaluator.matches(doc.body(), doc.body().element(0));
 assertTrue(element != null);
 }

 @Test
 public void testAndEvaluator_false() {
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("e")
 ));
 Element element = andEvaluator.matches(doc.body(), doc.body().element(1));
 assertFalse(element != null);
 }

 @Test
 public void testOrEvaluator() {
 Or orEvaluator = new Or(
 Arrays.asList(
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 )),
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("d")
 ))
 )
 );
 Element element = orEvaluator.matches(doc.body(), doc.body().element(0));
 assertTrue(element != null);

 element = orEvaluator.matches(doc.body(), doc.body().element(1));
 assertTrue(element != null);
 }

 @Test
 public void testOrEvaluator_false() {
 Or orEvaluator = new Or(
 Arrays.asList(
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 )),
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("e")
 ))
 )
 );
 Element element = orEvaluator.matches(doc.body(), doc.body().element(0));
 assertTrue(element != null);

 Element element1 = orEvaluator.matches(doc.body(), doc.body().element(1));
 assertFalse(element1 != null);
 }

 @Test
 public void testOrEvaluator_addEvaluator() {
 Or orEvaluator = new Or();
 orEvaluator.add(new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 )));
 orEvaluator.add(new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("d")
 )));
 Element element = orEvaluator.matches(doc.body(), doc.body().element(0));
 assertTrue(element != null);

 Element element1 = orEvaluator.matches(doc.body(), doc.body().element(1));
 assertTrue(element1 != null);
 }

 @Test
 public void testToString() {
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 ));
 String toString = andEvaluator.toString();
 assertTrue(StringUtil.join(new String[]{"And", "[ElementStartsWithTagName(div)]", "[ClassName(b)]"}, " ").equals(toString));

 Or orEvaluator = new Or(
 Arrays.asList(
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("b")
 )),
 new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new ClassNameEvaluator("d")
 ))
 )
 );
 String toString1 = orEvaluator.toString();
 assertTrue(StringUtil.join(new String[]{"Or", ":or[And [ElementStartsWithTagName(div)] [ClassName(b)]]", "[And [ElementStartsWithTagName(div)] [ClassName(d)]]"}, " ").equals(toString1));
 }
}