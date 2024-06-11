import org.junit.Test;
import org.jsoup.parser.QueryParser;
import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParseSimpleSelector() {
        String query = "div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add your assertions here to check if the evaluator matches the expected elements
    }

    @Test
    public void testParseComplexSelector() {
        String query = "div.class1 > p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        // Add your assertions here to check if the evaluator matches the expected elements
    }

    @Test
    public void testParseErrorCase() {
        String query = "div.class1 > p:first-child:not(p:first-child)"; // This query should throw an exception
        QueryParser parser = new QueryParser(query);
        try {
            parser.parse();
            fail("Expected SelectorParseException");
        } catch (Selector.SelectorParseException e) {
            // Add your assertions here to check the error message
        }
    }
}