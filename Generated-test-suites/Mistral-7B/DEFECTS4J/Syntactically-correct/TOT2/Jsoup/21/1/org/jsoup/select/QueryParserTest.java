import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.jsoup.parser.QueryParser;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div.class1 > p#id1:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        assertNotNull(evaluator);
        // Add more assertions to check the structure of evaluator tree
    }
}