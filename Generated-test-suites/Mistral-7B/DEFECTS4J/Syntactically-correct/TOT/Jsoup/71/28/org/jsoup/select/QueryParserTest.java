import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.QueryParser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.StructuralEvaluator;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeWithValue;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.AttributeWithValueEnding;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.IsFirstChild;
import org.jsoup.select.Evaluator.IsLastChild;
import org.jsoup.select.Evaluator.IsFirstOfType;
import org.jsoup.select.Evaluator.IsLastOfType;
import org.jsoup.select.Evaluator.IsOnlyChild;
import org.jsoup.select.Evaluator.IsOnlyOfType;
import org.jsoup.select.Evaluator.IsEmpty;
import org.jsoup.select.Evaluator.IsRoot;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexEquals;
import org.jsoup.select.Evaluator.Has;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsData;
import org.jsoup.select.Evaluator.Matches;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Not;
import org.jsoup.select.Evaluator.IsNthChild;
import org.jsoup.select.Evaluator.IsNthLastChild;
import org.jsoup.select.Evaluator.IsNthOfType;
import org.jsoup.select.Evaluator.IsNthLastOfType;
import org.jsoup.select.StructuralEvaluator.Root;
import org.jsoup.select.StructuralEvaluator.Parent;
import org.jsoup.select.StructuralEvaluator.ImmediateParent;
import org.jsoup.select.StructuralEvaluator.ImmediatePreviousSibling;
import org.jsoup.select.StructuralEvaluator.PreviousSibling;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;

public class QueryParserTest {

    @Test
    public void testSimpleSelectors() {
        String query = "div p";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new Tag("div"));
        expectedResult.add(new Tag("p"));
        assertEquals(expectedResult, parserResult.getChildren());
    }

    @Test
    public void testAttributeSelectors() {
        String query = "div[class='class1']";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new Class("class1"));
        expectedResult.add(new Tag("div"));
        assertEquals(expectedResult, parserResult.getChildren());
    }

    @Test
    public void testIDSelectors() {
        String query = "#id1";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new Id("id1"));
        assertEquals(expectedResult, parserResult.getChildren());
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidQuery() {
        String query = "div *p"; // invalid query: should not match '*' character
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidCombinator() {
        String query = "div >~ p"; // invalid combinator: '~' is not a valid sibling combinator
        QueryParser.parse(query);
    }

    @Test(expected = Selector.SelectorParseException.class)
    public void testInvalidAttributeSelector() {
        String query = "div[class=class1=class2"; // invalid attribute selector: multiple attribute values are not allowed
        QueryParser.parse(query);
    }

    @Test
    public void testNthChildSelector() {
        String query = "li:nth-child(3)";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new IsNthChild(2, 3));
        expectedResult.add(new Tag("li"));
        assertEquals(expectedResult, parserResult.getChildren());
    }

    @Test
    public void testNotSelector() {
        String query = "p:not(.error)";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new CombiningEvaluator.Not(new Class("error")));
        expectedResult.add(new Tag("p"));
        assertEquals(expectedResult, parserResult.getChildren());
    }

    @Test
    public void testComplexQuery() {
        String query = "div > ul > li:nth-child(2) > a[href^='http']";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new ImmediateParent(new Tag("div")));
        expectedResult.add(new Tag("ul"));
        expectedResult.add(new IsNthChild(2, 1));
        expectedResult.add(new Tag("li"));
        expectedResult.add(new AttributeStarting("href", "http"));
        expectedResult.add(new Tag("a"));
        Evaluator expected = new And(expectedResult.get(0), expectedResult.get(1), expectedResult.get(2), expectedResult.get(3), expectedResult.get(4), expectedResult.get(5));
        assertEquals(expected, parserResult);
    }

    @Test
    public void testComplexQueryWithOr() {
        String query = "div > ul > li:nth-child(2) > a[href^='http'] or div > ul > li:nth-child(3) > a[href^='https']";
        Evaluator parserResult = QueryParser.parse(query);
        List<Evaluator> expectedResult1 = new ArrayList<>();
        expectedResult1.add(new ImmediateParent(new Tag("div")));
        expectedResult1.add(new Tag("ul"));
        expectedResult1.add(new IsNthChild(2, 1));
        expectedResult1.add(new Tag("li"));
        expectedResult1.add(new AttributeStarting("href", "http"));
        expectedResult1.add(new Tag("a"));
        List<Evaluator> expectedResult2 = new ArrayList<>();
        expectedResult2.add(new ImmediateParent(new Tag("div")));
        expectedResult2.add(new Tag("ul"));
        expectedResult2.add(new IsNthChild(3, 1));
        expectedResult2.add(new Tag("li"));
        expectedResult2.add(new AttributeStarting("href", "https"));
        expectedResult2.add(new Tag("a"));
        List<Evaluator> expectedResult = new ArrayList<>();
        expectedResult.add(new CombiningEvaluator.Or(expectedResult1.get(0), expectedResult1.get(1), expectedResult1.get(2), expectedResult1.get(3), expectedResult1.get(4), expectedResult1.get(5), expectedResult2.get(0), expectedResult2.get(1), expectedResult2.get(2), expectedResult2.get(3), expectedResult2.get(4), expectedResult2.get(5)));
        Evaluator expected = expectedResult.get(0);
        assertEquals(expected, parserResult);
    }
}