import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.class1 > p.class2";
        Evaluator evaluator = QueryParser.parse(query);
        assertTrue(evaluator instanceof CombiningEvaluator.And);
        assertTrue(((CombiningEvaluator.And) evaluator).getEvaluators().get(0) instanceof StructuralEvaluator.ImmediateParent);
        assertTrue(((StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getEvaluators().get(0)).getChild() instanceof Evaluator.Class);
        assertTrue(((Evaluator.Class) ((StructuralEvaluator.ImmediateParent) ((CombiningEvaluator.And) evaluator).getEvaluators().get(0)).getChild()) instanceof Evaluator.Class);
    }
}