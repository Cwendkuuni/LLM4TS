import static org.junit.Assert.*;
import org.jsoup.select.QueryParser;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QueryParserTest {

    @org.junit.Test
    public void testParse() {
        String query1 = "#testId > .testClass";
        QueryParser parser = new QueryParser(query1);
        Evaluator evaluator = parser.parse();

        List<Evaluator> expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Id("testId"));
        expectedEvaluators.add(new StructuralEvaluator.ImmediateParent(new Evaluator.Class("testClass")));

        assertEquals(expectedEvaluators, evaluator.getSubSelectors());

        String query2 = "body > .testClass:first";
        parser = new QueryParser(query2);
        evaluator = parser.parse();

        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("body"));
        expectedEvaluators.add(new CombiningEvaluator.And(
                new StructuralEvaluator.ImmediateParent(new Evaluator.Class("testClass")),
                new Evaluator.PseudoClass("first")
        ));

        assertEquals(expectedEvaluators, evaluator.getSubSelectors());

        String query3 = "div.testClass:has(p:contains(test))";
        parser = new QueryParser(query3);
        evaluator = parser.parse();

        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Tag("div"));
        expectedEvaluators.add(new Evaluator.Class("testClass"));
        expectedEvaluators.add(new StructuralEvaluator.Has(new Evaluator.ContainsText("test")));

        assertEquals(expectedEvaluators, evaluator.getSubSelectors());

        String query4 = "input[type='text'][placeholder='Search']";
        parser = new QueryParser(query4);
        evaluator = parser.parse();

        expectedEvaluators = new ArrayList<>();
        expectedEvaluators.add(new Evaluator.Attribute(new AttributeName("type"), "text"));
        expectedEvaluators.add(new Evaluator.Attribute(new AttributeName("placeholder"), "Search"));

        assertEquals(expectedEvaluators, evaluator.getSubSelectors());
    }

    @org.junit.Test(expected = Selector.SelectorParseException.class)
    public void testParseInvalidQuery() {
        String query = "input[type=text][placeholder=Search] > body";
        new QueryParser(query).parse();
    }
}