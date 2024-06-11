import org.jsoup.parser.QueryParser;
import org.jsoup.select.Evaluator;
import org.junit.Test;

public class QueryParserTest {
    @Test
    public void testParseValidQuery() {
        String query = ".class1 #id1 > div.class2";
        Evaluator expectedEvaluator = new CombiningEvaluator.And(
                new Evaluator.Class("class1"),
                new Evaluator.Id("id1"),
                new StructuralEvaluator.ImmediateChild(new Evaluator.Class("class2"))
        );
        Evaluator actualEvaluator = QueryParser.parse(query);
        assertSame(expectedEvaluator, actualEvaluator);
    }
}