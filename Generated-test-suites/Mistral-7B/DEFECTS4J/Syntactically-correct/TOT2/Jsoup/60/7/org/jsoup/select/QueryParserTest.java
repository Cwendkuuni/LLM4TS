import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertNull(evaluator);
    }

    @Test
    public void testParseTagName() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Tag.class, evaluator.getClass());
        assertEquals("p", evaluator.toString());
    }

    @Test
    public void testParseId() {
        String query = "#id";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Id.class, evaluator.getClass());
        assertEquals("id", evaluator.toString());
    }

    @Test
    public void testParseClass() {
        String query = ".class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        assertEquals(Evaluator.Class.class, evaluator.getClass());
        assertEquals("class", evaluator.toString());
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p.class";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();
        CombiningEvaluator andEvaluator = (CombiningEvaluator) evaluator;
        assertEquals(Evaluator.Tag.class, andEvaluator.getLeftEvaluator().getClass());
        assertEquals(Evaluator.Class.class, andEvaluator.getRightEvaluator().getClass());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        String query = ":notp";
        new QueryParser(query).parse();
    }
}