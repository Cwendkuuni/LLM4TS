import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Evaluator;
import org.jsoup.helper.StringUtil;

public class QueryParserTest {

    @Test
    public void testParseSimpleSelector() {
        String query = "p";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Tag);
        assertEquals("p", ((Evaluator.Tag) evaluator).tagName());
    }

    @Test
    public void testParseIdSelector() {
        String query = "#id1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Id);
        assertEquals("id1", ((Evaluator.Id) evaluator).id());
    }

    @Test
    public void testParseClassSelector() {
        String query = ".class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        assertTrue(evaluator instanceof Evaluator.Class);
        assertEquals("class1", ((Evaluator.Class) evaluator).className());
    }

    @Test
    public void testParseTagAndClassSelector() {
        String query = "p.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.Class("class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseCombinedSelectors() {
        String query = "p > div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Class("div")));
        evaluators.add(new Evaluator.Class("class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeSelector() {
        String query = "p[class='class1']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.Attribute(StringUtil.unQuote("class"), "class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseCombinatorSelectors() {
        String query = "p, div.class1";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.Class("class1"));
        evaluators.add(new Evaluator.Tag("div"));
        Evaluator expectedEvaluator = new CombiningEvaluator.Or(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeWithValueSelector() {
        String query = "p[class='class1']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.AttributeWithValue(StringUtil.unQuote("class"), "class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeWithValueNotSelector() {
        String query = "p:not([class='class1'])";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new StructuralEvaluator.Not(new Evaluator.AttributeWithValue(StringUtil.unQuote("class"), "class1")));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeStartingSelector() {
        String query = "p[class^='class1']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.AttributeStarting(StringUtil.unQuote("class"), "class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeEndingSelector() {
        String query = "p[class$='class1']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.AttributeWithValueEnding(StringUtil.unQuote("class"), "class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeContainingSelector() {
        String query = "p[class*='class1']";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.AttributeWithValueContaining(StringUtil.unQuote("class"), "class1"));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeMatchingSelector() {
        String query = "p:contains(Class)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.Matches(Pattern.compile("Class", Pattern.CASE_INSENSITIVE)));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseAttributeMatchingOwnSelector() {
        String query = "p:containsOwn(Class)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.Tag("p"));
        evaluators.add(new Evaluator.MatchesOwn(Pattern.compile("Class", Pattern.CASE_INSENSITIVE)));
        Evaluator expectedEvaluator = new CombiningEvaluator.And(evaluators);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParsePseudoElementSelector() {
        String query = "p:first-child";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsFirstChild());
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParsePseudoClassSelector() {
        String query = "p:empty";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsEmpty());
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthChildSelector() {
        String query = "p:nth-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsNthChild(2, 0));
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthChildOfTypeSelector() {
        String query = "p:nth-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsNthOfType(2, 0));
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthLastChildSelector() {
        String query = "p:nth-last-child(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsNthLastChild(2, 0));
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }

    @Test
    public void testParseNthLastOfTypeSelector() {
        String query = "p:nth-last-of-type(2)";
        QueryParser parser = new QueryParser(query);
        Evaluator evaluator = parser.parse();

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator.IsNthLastOfType(2, 0));
        Evaluator expectedEvaluator = evaluators.get(0);

        assertEquals(expectedEvaluator, evaluator);
    }
}