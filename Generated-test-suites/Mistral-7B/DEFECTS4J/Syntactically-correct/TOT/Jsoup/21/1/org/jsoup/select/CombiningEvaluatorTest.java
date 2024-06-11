import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

public class CombiningEvaluatorTest {

 @Test
 public void testAndEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1'><p id='p1'>Hello World</p></div><div id='div2'><p id='p2'>Hello Test</p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new AttributeEqualsValueEvaluator("id", "div1")
 ));
 Elements matchingElements = root.select(andEvaluator.toString());
 assertTrue(matchingElements.size() == 1);
 Element matchingElement = matchingElements.get(0);
 assertEquals("div1", matchingElement.id());
 }

 @Test
 public void testOrEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1'><p id='p1'>Hello World</p></div><div id='div2'><p id='p2'>Hello Test</p></div></body></html>");
 Or orEvaluator = new Or();
 orEvaluator.add(new ElementStartsWithTagNameEvaluator("div"));
 orEvaluator.add(new ElementHasTextEvaluator("p", "Hello World"));
 orEvaluator.add(new AttributeEqualsValueEvaluator("id", "div1"));
 Elements matchingElements = root.select(orEvaluator.toString());
 assertTrue(matchingElements.size() == 2);
 assertTrue(matchingElements.get(0).tagName().equals("div"));
 assertTrue(matchingElements.get(1).id().equals("div1"));
 }

 @Test
 public void testAndAndEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1'><p id='p1'>Hello World</p></div><div id='div2'><p id='p2'>Hello Test</p></div></body></html>");
 And andEvaluator = new And(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new And(Arrays.asList(
 new AttributeEqualsValueEvaluator("id", "div1"),
 new ElementHasTextEvaluator("p", "Hello World")
 ))
 ));
 Elements matchingElements = root.select(andEvaluator.toString());
 assertTrue(matchingElements.size() == 1);
 Element matchingElement = matchingElements.get(0);
 assertEquals("p", matchingElement.tagName());
 assertEquals("Hello World", matchingElement.text());
 }

 @Test
 public void testOrOrEvaluator() {
 Element root = Jsoup.parse("<html><body><div id='div1'><p id='p1'>Hello World</p></div><div id='div2'><p id='p2'>Hello Test</p></div></body></html>");
 Or orEvaluator = new Or(Arrays.asList(
 new Or(Arrays.asList(
 new ElementStartsWithTagNameEvaluator("div"),
 new AttributeEqualsValueEvaluator("id", "div1")
 )),
 new Or(Arrays.asList(
 new ElementHasTextEvaluator("p", "Hello World"),
 new AttributeEqualsValueEvaluator("id", "div2")
 ))
 ));
 Elements matchingElements = root.select(orEvaluator.toString());
 assertTrue(matchingElements.size() == 2);
 assertTrue(matchingElements.get(0).tagName().equals("div") && matchingElements.get(0).id().equals("div1"));
 assertTrue(matchingElements.get(1).tagName().equals("p") && matchingElements.get(1).text().equals("Hello World"));
 }

 @Test
 public void testEmptyEvaluator() {
 Element root = Jsoup.parse("<html><body></body></html>");
 Or orEvaluator = new Or();
 Elements matchingElements = root.select(orEvaluator.toString());
 assertTrue(matchingElements.isEmpty());
 }
}