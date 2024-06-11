import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

 private String queryWithCombinators = "div > p > a + input";
 private String queryWithId = "#header";
 private String queryWithClass = ".nav";
 private String queryWithTag = "ul > li > a";
 private String queryWithAttribute = "[href]";
 private String queryWithMultipleAttributes = "[title~='test' and class='someClass']";
 private String queryWithPseudoClass = "li:first-child";
 private String queryWithPseudoElement = "input:checked ~ label";
 private String queryWithIdAndAttribute = "#id [href]";
 private String queryWithParent = "li > ul > li";
 private String queryWithNotSelector = "p:not(.red)";
 private String queryWithMultipleSelectors = "div.container > p, div.container > ul > li > a";

 @Test
 public void testParseQueryCombinators() {
 QueryParser parser = new QueryParser(queryWithCombinators);
 Evaluator evaluator = parser.parse();

 // Check the number of elements in the evaluator tree
 assertEquals(3, evaluator.size());

 // Check the structure of the evaluator tree
 StructuralEvaluator.ImmediateParent immediateParent = null;
 StructuralEvaluator.Parent parent = null;
 StructuralEvaluator.ImmediatePreviousSibling immediatePreviousSibling = null;
 StructuralEvaluator.PreviousSibling previousSibling = null;
 And and = null;
 Or or = null;

 // Check the first element
 Evaluator firstElement = evaluator.get(0);
 if (firstElement instanceof StructuralEvaluator.And) {
 and = (StructuralEvaluator.And) firstElement;
 } else {
 fail("The first element is not an And node");
 }

 // Check the second element
 Evaluator secondElement = and.get(0);
 if (secondElement instanceof StructuralEvaluator.Tag) {
 assertEquals("div", ((StructuralEvaluator.Tag) secondElement).getName());
 } else {
 fail("The second element is not a Tag node");
 }

 // Check the third element
 Evaluator thirdElement = and.get(1);
 if (thirdElement instanceof StructuralEvaluator.Tag) {
 assertEquals("p", ((StructuralEvaluator.Tag) thirdElement).getName());
 } else {
 fail("The third element is not a Tag node");
 }

 // Check the fourth element
 Evaluator fourthElement = and.get(2);
 if (fourthElement instanceof StructuralEvaluator.Tag) {
 assertEquals("a", ((StructuralEvaluator.Tag) fourthElement).getName());
 } else {
 fail("The fourth element is not a Tag node");
 }

 // Check the fifth element
 Evaluator fifthElement = and.get(3);
 if (fifthElement instanceof StructuralEvaluator.Tag) {
 assertEquals("input", ((StructuralEvaluator.Tag) fifthElement).getName());
 } else {
 fail("The fifth element is not a Tag node");
 }

 // Check the sixth element
 Evaluator sixthElement = and.get(4);
 if (sixthElement instanceof StructuralEvaluator.Combinator) {
 assertEquals("+", ((StructuralEvaluator.Combinator) sixthElement).getCombinator());
 } else {
 fail("The sixth element is not a Combinator node");
 }

 // Check the seventh element
 Evaluator seventhElement = and.get(5);
 if (seventhElement instanceof StructuralEvaluator.Tag) {
 assertEquals("input", ((StructuralEvaluator.Tag) seventhElement).getName());
 } else {
 fail("The seventh element is not a Tag node");
 }

 // Check the evaluator tree for the remaining combinators
 int index = 0;
 for (Evaluator evaluatorCombinator : evaluator) {
 if (evaluatorCombinator instanceof StructuralEvaluator.Combinator) {
 StructuralEvaluator.Combinator combinator = (StructuralEvaluator.Combinator) evaluatorCombinator;
 switch (index++) {
 case 1:
 if (combinator.getCombinator() != ">") {
 fail("Expected > as the second combinator, but got " + combinator.getCombinator());
 }
 break;
 case 2:
 if (combinator.getCombinator() != "+") {
 fail("Expected + as the third combinator, but got " + combinator.getCombinator());
 }
 break;
 default:
 break;
 }
 }
 }
 }

 @Test
 public void testParseQueryId() {
 QueryParser parser = new QueryParser(queryWithId);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 Equals equals = (Equals) evaluator.get(0);
 assertEquals("header", equals.getAttributeName());
 }

 @Test
 public void testParseQueryClass() {
 QueryParser parser = new QueryParser(queryWithClass);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 ClassName className = (ClassName) evaluator.get(0);
 assertEquals("nav", className.getClassValue());
 }

 @Test
 public void testParseQueryTag() {
 QueryParser parser = new QueryParser(queryWithTag);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 Tag tag = (Tag) evaluator.get(0);
 assertEquals("ul", tag.getName());
 }

 @Test
 public void testParseQueryAttribute() {
 QueryParser parser = new QueryParser(queryWithAttribute);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 Attribute attribute = (Attribute) evaluator.get(0);
 assertEquals("href", attribute.getAttributeName());
 }

 @Test
 public void testParseQueryMultipleAttributes() {
 QueryParser parser = new QueryParser(queryWithMultipleAttributes);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 And and = (And) evaluator.get(0);
 List<Evaluator> attributes = and.getSubSelectors();
 assertEquals(2, attributes.size());
 Attribute attribute1 = (Attribute) attributes.get(0);
 assertEquals("title", attribute1.getAttributeName());
 Attribute attribute2 = (Attribute) attributes.get(1);
 assertEquals("someClass", attribute2.getAttributeValue());
 }

 @Test
 public void testParseQueryPseudoClass() {
 QueryParser parser = new QueryParser(queryWithPseudoClass);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 PseudoClass pseudoClass = (PseudoClass) evaluator.get(0);
 assertEquals("first-child", pseudoClass.getName());
 }

 @Test
 public void testParseQueryPseudoElement() {
 QueryParser parser = new QueryParser(queryWithPseudoElement);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 PseudoElement pseudoElement = (PseudoElement) evaluator.get(0);
 assertEquals("label", pseudoElement.getName());
 }

 @Test
 public void testParseQueryIdAndAttribute() {
 QueryParser parser = new QueryParser(queryWithIdAndAttribute);
 Evaluator evaluator = parser.parse();
 assertEquals(2, evaluator.size());
 And and = (And) evaluator.get(0);
 List<Evaluator> idAndAttribute = and.getSubSelectors();
 assertEquals(2, idAndAttribute.size());
 Equals id = (Equals) idAndAttribute.get(0);
 assertEquals("header", id.getAttributeName());
 Attribute attribute = (Attribute) idAndAttribute.get(1);
 assertEquals("href", attribute.getAttributeName());
 }

 @Test
 public void testParseQueryParent() {
 QueryParser parser = new QueryParser(queryWithParent);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 StructuralEvaluator.Parent parent = (StructuralEvaluator.Parent) evaluator.get(0);
 assertEquals("li", parent.getChildClass().getName());
 }

 @Test
 public void testParseQueryNotSelector() {
 QueryParser parser = new QueryParser(queryWithNotSelector);
 Evaluator evaluator = parser.parse();
 assertEquals(1, evaluator.size());
 StructuralEvaluator.Not not = (StructuralEvaluator.Not) evaluator.get(0);
 assertEquals(1, not.getSubSelectors().size());
 StructuralEvaluator.Tag tag = (StructuralEvaluator.Tag) not.getSubSelectors().get(0);
 assertEquals("p", tag.getName());
 }

 @Test
 public void testParseQueryMultipleSelectors() {
 QueryParser parser = new QueryParser(queryWithMultipleSelectors);
 Evaluator evaluator = parser.parse();
 assertEquals(2, evaluator.size());
 And and = (And) evaluator.get(0);
 List<Evaluator> selectors = and.getSubSelectors();
 assertEquals(2, selectors.size());

 StructuralEvaluator.Tag tag1 = (StructuralEvaluator.Tag) selectors.get(0);
 assertEquals("div", tag1.getName());
 ClassName className1 = (ClassName) tag1.getClass();
 assertEquals("container", className1.getClassValue());

 StructuralEvaluator.Tag tag2 = (StructuralEvaluator.Tag) selectors.get(1);
 StructuralEvaluator.Tag tag2Child = (StructuralEvaluator.Tag) tag2.getChildClass();
 assertEquals("p", tag2.getName());
 }

}