import org.junit.Assert;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.parseErrorException;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1 > p.class2:not(.class3)";
 private static final String BASIC_QUERY_SELECTOR = "div > p:not(.class3)";

 @org.junit.Test
 public void testParseBasicQuery() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY);
 Selector expectedResult = Selector.parse(BASIC_QUERY_SELECTOR);
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test(expected = parseErrorException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("invalid query");
 }

 @org.junit.Test
 public void testParseEmptyQuery() {
 QueryParser queryParser = new QueryParser("");
 Selector expectedResult = Selector.empty();
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseSimpleQueryByTag() {
 QueryParser queryParser = new QueryParser("p");
 Selector expectedResult = Selector.tag("p");
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseSimpleQueryById() {
 QueryParser queryParser = new QueryParser("#id");
 Selector expectedResult = Selector.id("id");
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseSimpleQueryByClass() {
 QueryParser queryParser = new QueryParser(".class");
 Selector expectedResult = Selector.classNames("class");
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithMultipleCombinators() {
 QueryParser queryParser = new QueryParser("div#id > p.class1 ~ p.class2:not(.class3)");
 Selector expectedResult = Selector.compound(
 Selector.id("id"),
 Selector.descendant(Selector.tag("div"), Selector.tag("p")),
 Selector.classNames("class1"),
 Selector.sibling(Selector.classNames("class2")),
 Selector.not(Selector.classNames("class3"))
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithMultipleSelectors() {
 QueryParser queryParser = new QueryParser("div#id1, div#id2 > p.class1:not(.class3)");
 Selector expectedResult = Selector.list(
 Selector.id("id1"),
 Selector.tag("div"),
 Selector.tag("p"),
 Selector.classNames("class1"),
 Selector.not(Selector.classNames("class3")),
 Selector.id("id2"),
 Selector.tag("div"),
 Selector.tag("p")
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithMultipleAttributeSelectors() {
 QueryParser queryParser = new QueryParser("[data-test='value1'][data-test2='value2']");
 Selector expectedResult = Selector.list(
 Selector.attribute("data-test", "value1"),
 Selector.attribute("data-test2", "value2")
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithNthChildSelector() {
 QueryParser queryParser = new QueryParser("li:nth-child(3) > ul > li:nth-child(2)");
 Selector expectedResult = Selector.list(
 Selector.nthChild(3, "li"),
 Selector.descendant(Selector.tag("ul"), Selector.tag("li")),
 Selector.nthChild(2, "li")
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithNthOfTypeSelector() {
 QueryParser queryParser = new QueryParser("table:nth-of-type(2) > tr:nth-of-type(1) > td:nth-child(2)");
 Selector expectedResult = Selector.list(
 Selector.nthOfType(2, "table"),
 Selector.descendant(Selector.tag("table"), Selector.tag("tr")),
 Selector.nthOfType(1, "tr"),
 Selector.descendant(Selector.tag("tr"), Selector.tag("td")),
 Selector.nthChild(2, "td")
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithHasSelector() {
 QueryParser queryParser = new QueryParser("ul > li:has(> a[href^='http'])");
 Selector expectedResult = Selector.list(
 Selector.descendant(Selector.tag("ul"), Selector.tag("li")),
 Selector.hasChild(Selector.tag("a"), Selector.attrStartsWith("href", "http"))
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithMatchesSelector() {
 QueryParser queryParser = new QueryParser("input[type='text']:matchesOwn('[a-z]+')");
 Selector expectedResult = Selector.list(
 Selector.attribute("type", "text"),
 Selector.regexMatchesOwnText("\\w+")
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }

 @org.junit.Test
 public void testParseComplexQueryWithNotSelector() {
 QueryParser queryParser = new QueryParser("p:not([class]) > a:not(:has(> ul > li))");
 Selector expectedResult = Selector.list(
 Selector.not(Selector.attribute("class", "")),
 Selector.tag("p"),
 Selector.not(Selector.hasChild(Selector.tag("ul"), Selector.descendant(Selector.tag("ul"), Selector.tag("li"))))
 );
 Selector actualResult = queryParser.parse().asSelector();

 Assert.assertEquals(expectedResult, actualResult);
 }
}