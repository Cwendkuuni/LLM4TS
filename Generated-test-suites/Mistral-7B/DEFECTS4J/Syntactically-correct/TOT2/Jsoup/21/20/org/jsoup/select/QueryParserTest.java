import org.junit.Test;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

public class QueryParserTest {
    @Test
    public void testParseValidQuery() {
        QueryParser queryParser = new QueryParser("div.class1 > p#id1 > ul > li:nth-child(2)");
        Evaluator evaluator = queryParser.parse();
        // Check if the evaluator is as expected
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        QueryParser queryParser = new QueryParser("invalid query");
        queryParser.parse();
        // This test should throw an exception
    }
}