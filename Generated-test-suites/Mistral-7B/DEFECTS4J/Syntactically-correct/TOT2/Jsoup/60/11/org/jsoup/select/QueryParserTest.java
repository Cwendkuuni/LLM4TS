import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        assertTrue(result.evaluate(null) == null);
    }

    @Test
    public void testParseTagQuery() {
        String query = "p";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Tag);
        assertTrue(result.evaluate(null) != null);
    }

    @Test
    public void testParseIdQuery() {
        String query = "#id1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Id);
        assertTrue(result.evaluate(null) != null);
    }

    @Test
    public void testParseClassQuery() {
        String query = ".class1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Class);
        assertTrue(result.evaluate(null) != null);
    }

    @Test
    public void testParseCombinedQuery() {
        String query = "p.class1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        Evaluator tag = ((CombiningEvaluator.And) result).getLeft();
        assertTrue(tag instanceof Evaluator.Tag);
        Evaluator classQuery = ((CombiningEvaluator.And) result).getRight();
        assertTrue(classQuery instanceof Evaluator.Class);
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "p[class='class1']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Attribute);
        assertTrue(result.evaluate(null) != null);
    }

    @Test
    public void testParseAttributeQueryWithValue() {
        String query = "p[class='class1']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.Attribute);
        Evaluator attribute = ((Evaluator.Attribute) result);
        assertEquals("class", attribute.getAttributeName());
        assertEquals("class1", attribute.getAttributeValue());
    }

    @Test
    public void testParseAttributeQueryWithOperator() {
        String query = "p[class^='class1']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeStarting);
        Evaluator attribute = ((Evaluator.AttributeStarting) result);
        assertEquals("class", attribute.getAttributeName());
        assertEquals("class1", attribute.getValue());
    }

    @Test
    public void testParseAttributeQueryWithOperatorAndValue() {
        String query = "p[class^='class1']";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.AttributeStarting);
        Evaluator attribute = ((Evaluator.AttributeStarting) result);
        assertEquals("class", attribute.getAttributeName());
        assertEquals("class1", attribute.getValue());
    }

    @Test
    public void testParsePseudoClassQuery() {
        String query = "p:first-child";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.IsFirstChild);
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "p:nth-child(2)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.IsNthChild);
        Evaluator nthChild = ((Evaluator.IsNthChild) result);
        assertEquals(2, nthChild.getNthChildNumber());
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "p:nth-last-child(-n+3)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.IsNthLastChild);
        Evaluator nthLastChild = ((Evaluator.IsNthLastChild) result);
        assertEquals(3, nthLastChild.getNthLastChildNumber());
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "p:nth-of-type(2)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.IsNthOfType);
        Evaluator nthOfType = ((Evaluator.IsNthOfType) result);
        assertEquals(2, nthOfType.getNthOfTypeNumber());
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "p:nth-last-of-type(-n+3)";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof Evaluator.IsNthLastOfType);
        Evaluator nthLastOfType = ((Evaluator.IsNthLastOfType) result);
        assertEquals(3, nthLastOfType.getNthLastOfTypeNumber());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "p:not(p)p";
        QueryParser.parse(query);
        fail("Expected Selector.SelectorParseException");
    }

    @Test
    public void testParseCombinedQueryWithOr() {
        String query = "p, div.class1";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.Or);
        Evaluator tag = ((CombiningEvaluator.Or) result).getLeft();
        assertTrue(tag instanceof Evaluator.Tag);
        Evaluator classQuery = ((CombiningEvaluator.Or) result).getRight();
        assertTrue(classQuery instanceof Evaluator.Class);
    }

    @Test
    public void testParseCombinedQueryWithAnd() {
        String query = "p.class1.class2";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        Evaluator tag = ((CombiningEvaluator.And) result).getLeft();
        assertTrue(tag instanceof Evaluator.Tag);
        Evaluator class1 = ((CombiningEvaluator.And) result).getRight();
        assertTrue(class1 instanceof Evaluator.Class);
    }

    @Test
    public void testParseComplexQuery() {
        String query = "p:first-child > ul > li:first-child > a[href^='https']:not([href^='https://www.google.com'])";
        Evaluator result = QueryParser.parse(query);
        assertTrue(result instanceof CombiningEvaluator.And);
        Evaluator isFirstChild = ((CombiningEvaluator.And) result).getLeft();
        assertTrue(isFirstChild instanceof Evaluator.IsFirstChild);
        Evaluator isUL = ((CombiningEvaluator.And) result).getRight();
        assertTrue(isUL instanceof CombiningEvaluator.And);
        Evaluator isLI = ((CombiningEvaluator.And) isUL).getLeft();
        assertTrue(isLI instanceof Evaluator.IsNthChild);
        Evaluator isFirstLI = ((Evaluator.IsNthChild) isLI);
        assertEquals(1, isFirstLI.getNthChildNumber());
        Evaluator isAPair = ((CombiningEvaluator.And) isUL).getRight();
        assertTrue(isAPair instanceof CombiningEvaluator.And);
        Evaluator isAHref = ((CombiningEvaluator.And) isAPair).getLeft();
        assertTrue(isAHref instanceof Evaluator.Matching);
        Matcher hrefMatcher = ((Evaluator.Matching) isAHref).getPattern();
        assertTrue(hrefMatcher.matcher("https://example.com").matches());
        assertTrue(hrefMatcher.matcher("https://www.google.com").doesNotMatch());
        Evaluator notGoogle = ((CombiningEvaluator.And) isAPair).getRight();
        assertTrue(notGoogle instanceof StructuralEvaluator.Not);
    }
}