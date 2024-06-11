import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import static org.junit.Assert.*;

public class QueryParserTest {
    private QueryParser parser;

    @Before
    public void setUp() {
        this.parser = new QueryParser("#test");
    }

    @Test
    public void testParsingIdSelector() {
        assertEquals(new Evaluator.Id("test"), parser.parse().getEvaluators().get(0));
    }

    @Test
    public void testParsingClassSelector() {
        parser = new QueryParser(".test");
        assertEquals(new Evaluator.Class("test"), parser.parse().getEvaluators().get(0));
    }

    @Test
    public void testParsingTagSelector() {
        parser = new QueryParser("p");
        assertEquals(new Evaluator.Tag("p"), parser.parse().getEvaluators().get(0));
    }

    @Test
    public void testParsingAttributeSelector() {
        parser = new QueryParser("[href='test.html']");
        List<Evaluator> evaluators = parser.parse().getEvaluators();
        assertEquals(new Evaluator.AttributeWithValue("href", "test.html"), evaluators.get(0));
    }

    @Test
    public void testParsingAttributeSelectorWithOperator() {
        parser = new QueryParser("[data-test!='test']");
        List<Evaluator> evaluators = parser.parse().getEvaluators();
        assertEquals(new Evaluator.AttributeWithValueNot("data-test", "test"), evaluators.get(0));
    }

    @Test
    public void testParsingComplexSelector() {
        parser = new QueryParser("div.test>p:first-child");
        List<Evaluator> evaluators = parser.parse().getEvaluators();
        Evaluator rootEvaluator = evaluators.get(0);
        assertEquals(new CombiningEvaluator.And(
                new Evaluator.Class("test"),
                new StructuralEvaluator.ImmediateChildOf(
                        new Evaluator.Tag("div"),
                        new Evaluator.IsFirstChild())),
            rootEvaluator);
    }

    @Test
    public void testParsingCombinedSelectorsWithDifferentPrecedence() {
        parser = new QueryParser("div#test.test>p, p.test");
        List<Evaluator> evaluators = parser.parse().getEvaluators();
        Evaluator rootEvaluator = evaluators.get(0);
        Evaluator andEvaluator = ((CombiningEvaluator.Or) rootEvaluator).getLeftEvaluator();
        Evaluator idEvaluator = andEvaluator.getLeftEvaluator();
        Evaluator classEvaluator = andEvaluator.getRightEvaluator();

        assertEquals(new CombiningEvaluator.And(
                new Evaluator.Id("test"),
                new Evaluator.Class("test")),
            idEvaluator);

        assertEquals(new Evaluator.Class("test"), classEvaluator);

        Evaluator orEvaluator = ((CombiningEvaluator.Or) rootEvaluator).getRightEvaluator();
        Evaluator tagEvaluator = orEvaluator.getLeftEvaluator();

        assertEquals(new Evaluator.Tag("p"), tagEvaluator);
    }
}