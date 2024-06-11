import org.jsoup.parser.QueryParser;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        Assert.assertNull(parser.parse());
    }

    @Test
    public void testParseTagQuery() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.Tag.class));
        Assert.assertThat(evaluator.toString(), is("div"));
    }

    @Test
    public void testParseClassQuery() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.Class.class));
        Assert.assertThat(evaluator.toString(), is(".myClass"));
    }

    @Test
    public void testParseIdQuery() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.Id.class));
        Assert.assertThat(evaluator.toString(), is("#myId"));
    }

    @Test
    public void testParseMultipleCombinators() {
        String query = "div > p > ul";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(CombiningEvaluator.And.class));
        Assert.assertThat(evaluator.getLeft(), instanceOf(Evaluator.Tag.class));
        Assert.assertThat(evaluator.getLeft().toString(), is("div"));
        Assert.assertThat(evaluator.getRight(), instanceOf(CombiningEvaluator.And.class));
        Assert.assertThat(evaluator.getRight().getLeft(), instanceOf(Evaluator.Tag.class));
        Assert.assertThat(evaluator.getRight().getLeft().toString(), is("p"));
        Assert.assertThat(evaluator.getRight().getRight(), instanceOf(Evaluator.Tag.class));
        Assert.assertThat(evaluator.getRight().getRight().toString(), is("ul"));
    }

    @Test
    public void testParseAttributeQuery() {
        String query = "a[href]";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.Attribute.class));
        Assert.assertThat(evaluator.toString(), is("a[href]"));
    }

    @Test
    public void testParseAttributeValueQuery() {
        String query = "a[href='http://example.com']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.AttributeWithValue.class));
        Assert.assertThat(evaluator.toString(), is("a[href='http://example.com']"));
    }

    @Test
    public void testParseAttributeValueStartingQuery() {
        String query = "a[href^='http']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.AttributeWithValueStarting.class));
        Assert.assertThat(evaluator.toString(), is("a[href^='http']"));
    }

    @Test
    public void testParseAttributeValueEndingQuery() {
        String query = "a[href$='.pdf']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.AttributeWithValueEnding.class));
        Assert.assertThat(evaluator.toString(), is("a[href$='.pdf']"));
    }

    @Test
    public void testParseAttributeValueContainingQuery() {
        String query = "a[href*='example']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.AttributeWithValueContaining.class));
        Assert.assertThat(evaluator.toString(), is("a[href*='example']"));
    }

    @Test
    public void testParseAttributeValueMatchingQuery() {
        String query = "a[href~='example']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.AttributeWithValueMatching.class));
        Assert.assertThat(evaluator.toString(), is("a[href~='example']"));
    }

    @Test
    public void testParseNthChildQuery() {
        String query = "li:nth-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.IsNthChild.class));
        Assert.assertThat(evaluator.toString(), is("li:nth-child(3)"));
    }

    @Test
    public void testParseNthOfTypeQuery() {
        String query = "li:nth-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.IsNthOfType.class));
        Assert.assertThat(evaluator.toString(), is("li:nth-of-type(3)"));
    }

    @Test
    public void testParseNthLastChildQuery() {
        String query = "li:nth-last-child(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.IsNthLastChild.class));
        Assert.assertThat(evaluator.toString(), is("li:nth-last-child(3)"));
    }

    @Test
    public void testParseNthLastOfTypeQuery() {
        String query = "li:nth-last-of-type(3)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.IsNthLastOfType.class));
        Assert.assertThat(evaluator.toString(), is("li:nth-last-of-type(3)"));
    }

    @Test
    public void testParseContainsTextQuery() {
        String query = "div:contains(example)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.ContainsText.class));
        Assert.assertThat(evaluator.toString(), is("div:contains(example)"));
    }

    @Test
    public void testParseContainsOwnTextQuery() {
        String query = "div:containsOwn(example)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.ContainsOwnText.class));
        Assert.assertThat(evaluator.toString(), is("div:containsOwn(example)"));
    }

    @Test
    public void testParseContainsDataQuery() {
        String query = "div:containsData(example)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.ContainsData.class));
        Assert.assertThat(evaluator.toString(), is("div:containsData(example)"));
    }

    @Test
    public void testParseMatchesQuery() {
        String query = "div:matches(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.Matches.class));
        Assert.assertThat(evaluator.toString(), is("div:matches(p)"));
    }

    @Test
    public void testParseMatchesOwnQuery() {
        String query = "div:matchesOwn(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(Evaluator.MatchesOwn.class));
        Assert.assertThat(evaluator.toString(), is("div:matchesOwn(p)"));
    }

    @Test
    public void testParseNotQuery() {
        String query = "div:not(p)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        Assert.assertThat(evaluator, instanceOf(StructuralEvaluator.Not.class));
        Assert.assertThat(evaluator.getChild(), instanceOf(Evaluator.Tag.class));
        Assert.assertThat(evaluator.getChild().toString(), is("p"));
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "div:not(p):has(a:not(b))";
        new QueryParser(query).parse();
    }
}