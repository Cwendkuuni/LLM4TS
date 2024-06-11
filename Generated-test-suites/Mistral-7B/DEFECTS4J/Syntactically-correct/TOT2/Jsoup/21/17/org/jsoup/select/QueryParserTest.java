import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;

public class QueryParserTest {

    @Test
    public void testParse() {
        // Test parsing a simple CSS query
        String cssQuery = "div.class";
        Evaluator evaluator = QueryParser.parse(cssQuery);
        assertEquals(Evaluator.Type.ELEMENT, evaluator.getType());
        assertEquals(Evaluator.Class.class, evaluator.getClassType());
        assertEquals("class", evaluator.getClassValue());

        // Test parsing an empty query
        cssQuery = "";
        evaluator = QueryParser.parse(cssQuery);
        assertNull(evaluator);

        // Test parsing a query with invalid characters
        cssQuery = "div#id%^&*()_={}[];";
        evaluator = QueryParser.parse(cssQuery);
        assertNull(evaluator);
    }
}