import org.jsoup.parser.QueryParser;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {
    // Test case for empty query
    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        Assert.assertEquals(Evaluator.class, evaluator.getClass());
        Assert.assertTrue(evaluator.isEmpty());
    }

    // Test case for simple query
    @Test
    public void testParseSimpleQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator tagEvaluator = new Evaluator.Tag("div");
        Assert.assertEquals(tagEvaluator, evaluator);
    }

    // Test case for query with id
    @Test
    public void testParseQueryWithId() {
        String query = "#header";
        Evaluator evaluator = QueryParser.parse(query);
        Evaluator idEvaluator = new Evaluator.Id("header");
        Assert.assertEquals(idEvaluator, evaluator);
    }
}