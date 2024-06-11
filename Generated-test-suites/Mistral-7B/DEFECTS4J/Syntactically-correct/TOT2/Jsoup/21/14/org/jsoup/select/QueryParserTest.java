import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryParserTest {
    @Test
    public void testParse() {
        String query = "div.class1 > p#id1.class2:not(.class3)";
        Evaluator evaluator = QueryParser.parse(query);
        // Add assertions to check the structure and properties of the returned evaluator
    }
}