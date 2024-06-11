import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new CombiningEvaluator.And(new Evaluator.AllElements()), parser.parse());
    }

    @Test
    public void testParseSimpleTagSelector() {
        String query = "div";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Tag("div"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseIdSelector() {
        String query = "#myId";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Id("myId"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseClassSelector() {
        String query = ".myClass";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Class("myClass"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "div.myClass";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Class("myClass"), parser.parse().evals.get(0));
        Assert.assertEquals(new Evaluator.Tag("div"), parser.parse().evals.get(1));
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "[title='Test Title']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValue("title", "Test Title"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseAttributeSelectorWithOperator() {
        String query = "[title!='Test Title']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValueNot("title", "Test Title"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseAttributeSelectorWithStartsWith() {
        String query = "[title^='Test']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValueStarting("title", "Test"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseAttributeSelectorWithEndsWith() {
        String query = "[title$='Test']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValueEnding("title", "Test"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseAttributeSelectorWithContains() {
        String query = "[title*='Test']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValueContaining("title", "Test"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseAttributeSelectorWithMatching() {
        String query = "[title~='Test']";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.AttributeWithValueMatching("title", Pattern.compile("Test")), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithNthChild() {
        String query = "div:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsNthChild(2, 0), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithNthLastChild() {
        String query = "div:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsNthLastChild(2, 0), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithNthOfType() {
        String query = "li:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsNthOfType(2, 0), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithNthLastOfType() {
        String query = "li:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsNthLastOfType(2, 0), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithFirstChild() {
        String query = "div > :first-child";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsFirstChild(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithLastChild() {
        String query = "div > :last-child";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsLastChild(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithFirstOfType() {
        String query = "div > p:first-of-type";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsFirstOfType(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithLastOfType() {
        String query = "div > p:last-of-type";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsLastOfType(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithOnlyChild() {
        String query = "div > :only-child";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsOnlyChild(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithOnlyOfType() {
        String query = "div > p:only-of-type";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsOnlyOfType(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithEmpty() {
        String query = "div:empty";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsEmpty(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithRoot() {
        String query = ":root";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.IsRoot(), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithNot() {
        String query = ":not(div)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new StructuralEvaluator.Not(new Evaluator.Tag("div")), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithHas() {
        String query = "div:has(p)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new StructuralEvaluator.Has(new Evaluator.Tag("p")), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithContains() {
        String query = "div:contains(Test)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.ContainsText("Test"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithContainsData() {
        String query = "div:containsData(Test)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.ContainsData("Test"), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithMatches() {
        String query = "div:matches(p)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.Matches(Pattern.compile("p")), parser.parse().evals.get(0));
    }

    @Test
    public void testParseElementWithMatchesOwn() {
        String query = "div:matchesOwn(p)";
        QueryParser parser = new QueryParser(query);
        Assert.assertEquals(new Evaluator.MatchesOwn(Pattern.compile("p")), parser.parse().evals.get(0));
    }
}