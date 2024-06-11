import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;

public class QueryParserTest {
    @Test
    public void testParseEmptyQuery() {
        QueryParser parser = new QueryParser("");
        assertNull(parser.parse());
    }

    @Test
    public void testParseTagSelector() {
        QueryParser parser = new QueryParser("div");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("div", evaluator.toString());
    }

    @Test
    public void testParseClassSelector() {
        QueryParser parser = new QueryParser(".class");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals(".class", evaluator.toString());
    }

    @Test
    public void testParseIdSelector() {
        QueryParser parser = new QueryParser("#id");
        Evaluator evaluator = parser.parse();
        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("#id", evaluator.toString());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidSelector() {
        QueryParser parser = new QueryParser(":invalidSelector");
        parser.parse(); // this should throw an exception
    }
}