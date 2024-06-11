import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

    @Test
    public void testParseSimpleQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseQueryWithId() {
        String query = "#myId";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("myId"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseQueryWithClass() {
        String query = ".myClass";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Class("myClass"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseQueryWithAttribute() {
        String query = "[title='Test']";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.AttributeWithValue("title", "Test"));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseQueryWithCombinator() {
        String query = "div > p";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new StructuralEvaluator.ChildOf(expectedEvaluators.get(0))));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }

    @Test
    public void testParseQueryWithMultipleSelectors() {
        String query = "div.myClass#myId > p.anotherClass:not(.excludedClass)";
        Evaluator evaluator = QueryParser.parse(query);
        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("myClass"));
        expectedEvaluators.add(new Evaluator.Id("myId"));
        expectedEvaluators.add(new Evaluator.Tag("p"));
        expectedEvaluators.add(new Evaluator.Class("anotherClass"));
        expectedEvaluators.add(new StructuralEvaluator.Not(new Evaluator.Class("excludedClass")));
        Assert.assertEquals(expectedEvaluators, evaluator.getChildren());
    }
}