import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {

 private static final String BASIC_QUERY = "div.class1.class2";
 private static final String BASIC_QUERY_PARSED = "class:class1 and class:class2 and tag:div";

 @Test
 public void testParseBasicQuery() {
 QueryParser queryParser = new QueryParser(BASIC_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(BASIC_QUERY_PARSED));
 }

 private static final String COMBINATOR_QUERY = "div > p";
 private static final String COMBINATOR_QUERY_PARSED = "tag:div and is_ancestor_of(tag:p)";

 @Test
 public void testParseCombinatorQuery() {
 QueryParser queryParser = new QueryParser(COMBINATOR_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(COMBINATOR_QUERY_PARSED));
 }

 private static final String ID_QUERY = "#id1";
 private static final String ID_QUERY_PARSED = "element_id:id1";

 @Test
 public void testParseIdQuery() {
 QueryParser queryParser = new QueryParser(ID_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(ID_QUERY_PARSED));
 }

 private static final String CLASS_QUERY = "div.class1";
 private static final String CLASS_QUERY_PARSED = "tag:div and class:class1";

 @Test
 public void testParseClassQuery() {
 QueryParser queryParser = new QueryParser(CLASS_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(CLASS_QUERY_PARSED));
 }

 private static final String TAG_QUERY = "input";
 private static final String TAG_QUERY_PARSED = "tag:input";

 @Test
 public void testParseTagQuery() {
 QueryParser queryParser = new QueryParser(TAG_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(TAG_QUERY_PARSED));
 }

 private static final String ATTRIBUTE_QUERY = "input[type='text']";
 private static final String ATTRIBUTE_QUERY_PARSED = "tag:input and attr:type and attr_value:text";

 @Test
 public void testParseAttributeQuery() {
 QueryParser queryParser = new QueryParser(ATTRIBUTE_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(ATTRIBUTE_QUERY_PARSED));
 }

 private static final String ATTRIBUTE_QUERY_WITH_VALUE = "input[type='text'][value='text']";
 private static final String ATTRIBUTE_QUERY_WITH_VALUE_PARSED = "tag:input and (attr:type and attr_value:text) or (attr:type and attr:value and text:text)";

 @Test
 public void testParseAttributeQueryWithValue() {
 QueryParser queryParser = new QueryParser(ATTRIBUTE_QUERY_WITH_VALUE);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(ATTRIBUTE_QUERY_WITH_VALUE_PARSED));
 }

 private static final String SUBSELECT_QUERY = "div > p:has(a[href='https://example.com'])";
 private static final String SUBSELECT_QUERY_PARSED = "tag:div and is_ancestor_of(tag:p) and (tag:a and attr:href and text:https://example.com)";

 @Test
 public void testParseSubselectQuery() {
 QueryParser queryParser = new QueryParser(SUBSELECT_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(SUBSELECT_QUERY_PARSED));
 }

 private static final String CONTAINS_QUERY = "div:contains(text)";
 private static final String CONTAINS_QUERY_PARSED = "tag:div and (text:text)";

 @Test
 public void testParseContainsQuery() {
 QueryParser queryParser = new QueryParser(CONTAINS_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(CONTAINS_QUERY_PARSED));
 }

 private static final String CONTAINS_OWN_QUERY = "div:containsOwn(text)";
 private static final String CONTAINS_OWN_QUERY_PARSED = "tag:div and descendant::*[text()=text()]";

 @Test
 public void testParseContainsOwnQuery() {
 QueryParser queryParser = new QueryParser(CONTAINS_OWN_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.startsWith("tag:div and descendant::"));
 }

 private static final String CONTAINS_DATA_QUERY = "div:containsData(data)";
 private static final String CONTAINS_DATA_QUERY_PARSED = "tag:div and contains_data(data)";

 @Test
 public void testParseContainsDataQuery() {
 QueryParser queryParser = new QueryParser(CONTAINS_DATA_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(CONTAINS_DATA_QUERY_PARSED));
 }

 private static final String MATCHES_QUERY = "div:matches(regex)";
 private static final String MATCHES_QUERY_PARSED = "tag:div and regex_match(regex)";

 @Test
 public void testParseMatchesQuery() {
 QueryParser queryParser = new QueryParser(MATCHES_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(MATCHES_QUERY_PARSED));
 }

 private static final String MATCHES_OWN_QUERY = "div:matchesOwn(regex)";
 private static final String MATCHES_OWN_QUERY_PARSED = "tag:div and descendant::*[regex_match(regex)]";

 @Test
 public void testParseMatchesOwnQuery() {
 QueryParser queryParser = new QueryParser(MATCHES_OWN_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.startsWith("tag:div and descendant::"));
 }

 private static final String NOT_QUERY = "div:not(p)";
 private static final String NOT_QUERY_PARSED = "tag:div and not(tag:p)";

 @Test
 public void testParseNotQuery() {
 QueryParser queryParser = new QueryParser(NOT_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(NOT_QUERY_PARSED));
 }

 private static final String NTH_CHILD_QUERY = "li:nth-child(2)";
 private static final String NTH_CHILD_QUERY_PARSED = "tag:li and is_nth_child(2)";

 @Test
 public void testParseNthChildQuery() {
 QueryParser queryParser = new QueryParser(NTH_CHILD_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(NTH_CHILD_QUERY_PARSED));
 }

 private static final String NTH_LAST_CHILD_QUERY = "li:nth-last-child(2)";
 private static final String NTH_LAST_CHILD_QUERY_PARSED = "tag:li and is_nth_last_child(2)";

 @Test
 public void testParseNthLastChildQuery() {
 QueryParser queryParser = new QueryParser(NTH_LAST_CHILD_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(NTH_LAST_CHILD_QUERY_PARSED));
 }

 private static final String NTH_OF_TYPE_QUERY = "li:nth-of-type(2)";
 private static final String NTH_OF_TYPE_QUERY_PARSED = "tag:li and is_nth_of_type(2)";

 @Test
 public void testParseNthOfTypeQuery() {
 QueryParser queryParser = new QueryParser(NTH_OF_TYPE_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(NTH_OF_TYPE_QUERY_PARSED));
 }

 private static final String NTH_LAST_OF_TYPE_QUERY = "li:nth-last-of-type(2)";
 private static final String NTH_LAST_OF_TYPE_QUERY_PARSED = "tag:li and is_nth_last_of_type(2)";

 @Test
 public void testParseNthLastOfTypeQuery() {
 QueryParser queryParser = new QueryParser(NTH_LAST_OF_TYPE_QUERY);
 Evaluator parsedQuery = queryParser.parse();
 String parsedQueryString = parsedQuery.toString();
 assertTrue(parsedQueryString.equals(NTH_LAST_OF_TYPE_QUERY_PARSED));
 }

 @Test(expected = Selector.SelectorParseException.class)
 public void testParseInvalidQuery() {
 QueryParser queryParser = new QueryParser("invalid_query");
 }
}