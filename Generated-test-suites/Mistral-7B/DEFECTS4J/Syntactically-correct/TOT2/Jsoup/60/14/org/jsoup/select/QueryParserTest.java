import static org.junit.Assert.*;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParseEmptyQuery() {
        String query = "";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(evaluator.size() == 0);
    }

    @Test
    public void testParseSingleElementQuery() {
        String query = "div";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals(evaluator.tagName(), "div");
    }

    @Test
    public void testParseMultipleCombinatorsQuery() {
        String query = "div > p + ul";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        Evaluator[] elements = evaluator.toArray();
        assertEquals(elements.length, 3);
        assertTrue(elements[0] instanceof StructuralEvaluator.ImmediateParent);
        assertTrue(elements[1] instanceof StructuralEvaluator.ImmediatePreviousSibling);
        assertTrue(elements[2] instanceof Evaluator.Tag);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidCombinatorQuery() {
        String query = "div > z + ul";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseUnmatchedParenthesesQuery() {
        String query = "div > (p + ul)";
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testParseUnrecognizedTokenQuery() {
        String query = "div > x + ul";
        QueryParser.parse(query);
    }
}