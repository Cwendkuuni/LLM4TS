import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNull(result);
    }

    @Test
    public void testParseSimpleQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Tag);
        Assert.assertEquals("div", ((Evaluator.Tag) result).getTagName());
    }

    @Test
    public void testParseQueryWithAttribute() {
        String query = "div[class='red']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Attribute);
        Assert.assertEquals("class", ((Evaluator.Attribute) result).getAttributeName());
        Assert.assertEquals("red", ((Evaluator.Attribute) result).getAttributesValue());
    }

    @Test
    public void testParseQueryWithMultipleCombinators() {
        String query = "div > p:first-child + ul > li:last-child";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.CombiningEvaluator.And);

        Evaluator firstPart = ((Evaluator.CombiningEvaluator.And) result).getLeft();
        Assert.assertTrue(firstPart instanceof Evaluator.StructuralEvaluator.ImmediateChild);
        Assert.assertEquals("div", ((Evaluator.StructuralEvaluator.ImmediateChild) firstPart).getParentTagName());

        Evaluator secondPart = ((Evaluator.CombiningEvaluator.And) result).getRight();
        Assert.assertTrue(secondPart instanceof Evaluator.CombiningEvaluator.And);

        Evaluator firstGrandChild = ((Evaluator.CombiningEvaluator.And) secondPart).getLeft();
        Assert.assertTrue(firstGrandChild instanceof Evaluator.EvaluatorType.IsFirstChild);

        Evaluator sibling = ((Evaluator.CombiningEvaluator.And) secondPart).getRight();
        Assert.assertTrue(sibling instanceof Evaluator.StructuralEvaluator.PreviousSibling);
        Assert.assertEquals("ul", ((Evaluator.StructuralEvaluator.PreviousSibling) sibling).getPreviousTagName());

        Evaluator lastChild = ((Evaluator.CombiningEvaluator.And) sibling).getRight();
        Assert.assertTrue(lastChild instanceof Evaluator.EvaluatorType.IsLastOfType);
        Assert.assertEquals("li", ((Evaluator.EvaluatorType.IsLastOfType) lastChild).getTagName());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div#foo:not[class='red']:not[class='green']:not(p)";
        QueryParser parser = new QueryParser(query);
        parser.parse();
    }

    @Test
    public void testParseQueryWithPseudoClass() {
        String query = "input:text";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.PseudoClass);
        Assert.assertEquals("text", ((Evaluator.PseudoClass) result).getPseudoClass());
    }

    @Test
    public void testParseQueryWithPseudoElement() {
        String query = "a:link::after";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.PseudoElement);
        Assert.assertEquals("after", ((Evaluator.PseudoElement) result).getPseudoElementType());
    }

    @Test
    public void testParseQueryWithId() {
        String query = "#header";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Id);
        Assert.assertEquals("header", ((Evaluator.Id) result).getId());
    }

    @Test
    public void testParseQueryWithClass() {
        String query = ".red";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Class);
        Assert.assertEquals("red", ((Evaluator.Class) result).getClassValue());
    }

    @Test
    public void testParseQueryWithTag() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Tag);
        Assert.assertEquals("p", ((Evaluator.Tag) result).getTagName());
    }

    @Test
    public void testParseQueryWithAttributeValue() {
        String query = "input[value='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.AttributeWithValue);
        Assert.assertEquals("value", ((Evaluator.AttributeWithValue) result).getAttributeName());
        Assert.assertEquals("search", ((Evaluator.AttributeWithValue) result).getValue());
    }

    @Test
    public void testParseQueryWithAttributeWithValueStarting() {
        String query = "input[value^='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.AttributeWithValueStarting);
        Assert.assertEquals("value", ((Evaluator.AttributeWithValueStarting) result).getAttributeName());
        Assert.assertEquals("search", ((Evaluator.AttributeWithValueStarting) result).getValuePrefix());
    }

    @Test
    public void testParseQueryWithAttributeWithValueEnding() {
        String query = "input[value$='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.AttributeWithValueEnding);
        Assert.assertEquals("value", ((Evaluator.AttributeWithValueEnding) result).getAttributeName());
        Assert.assertEquals("search", ((Evaluator.AttributeWithValueEnding) result).getValueSuffix());
    }

    @Test
    public void testParseQueryWithAttributeWithValueContaining() {
        String query = "input[value*='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.AttributeWithValueContaining);
        Assert.assertEquals("value", ((Evaluator.AttributeWithValueContaining) result).getAttributeName());
        Assert.assertEquals("search", ((Evaluator.AttributeWithValueContaining) result).getValueContaining());
    }

    @Test
    public void testParseQueryWithAttributeWithValueMatching() {
        String query = "input[value~='search']";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.AttributeWithValueMatching);
        Assert.assertEquals("value", ((Evaluator.AttributeWithValueMatching) result).getAttributeName());
        Assert.assertEquals("search", ((Evaluator.AttributeWithValueMatching) result).getMatchingValue());
    }

    @Test
    public void testParseQueryWithPseudoClassContainsText() {
        String query = "div:contains(Hello)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.ContainsText);
        Assert.assertEquals("Hello", ((Evaluator.ContainsText) result).getTextToSearch());
    }

    @Test
    public void testParseQueryWithPseudoClassContainsOwnText() {
        String query = "div:containsOwn(Hello)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.ContainsOwnText);
        Assert.assertEquals("Hello", ((Evaluator.ContainsOwnText) result).getTextToSearch());
    }

    @Test
    public void testParseQueryWithPseudoClassMatches() {
        String query = "div:matches(.test-class)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Matches);
        Assert.assertEquals(Pattern.compile(".test-class"), ((Evaluator.Matches) result).getRegex());
    }

    @Test
    public void testParseQueryWithPseudoClassMatchesOwn() {
        String query = "div:matchesOwn(.test-class)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.MatchesOwn);
        Assert.assertEquals(Pattern.compile(".test-class"), ((Evaluator.MatchesOwn) result).getRegex());
    }

    @Test
    public void testParseQueryWithPseudoClassNot() {
        String query = "div:not(.test-class)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.Not);
        Assert.assertTrue(result.evaluate(new ElementContext("div")));
        Assert.assertFalse(result.evaluate(new ElementContext("div", new ClassAttribute("test-class"))));
    }

    @Test
    public void testParseQueryWithNthChild() {
        String query = "li:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.IsNthChild);
        Assert.assertEquals(2, ((Evaluator.IsNthChild) result).getIndex());
    }

    @Test
    public void testParseQueryWithNthLastChild() {
        String query = "li:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.IsNthLastChild);
        Assert.assertEquals(2, ((Evaluator.IsNthLastChild) result).getIndex());
    }

    @Test
    public void testParseQueryWithNthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.IsNthOfType);
        Assert.assertEquals(2, ((Evaluator.IsNthOfType) result).getIndex());
    }

    @Test
    public void testParseQueryWithNthLastOfType() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator result = parser.parse();
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Evaluator.IsNthLastOfType);
        Assert.assertEquals(2, ((Evaluator.IsNthLastOfType) result).getIndex());
    }
}