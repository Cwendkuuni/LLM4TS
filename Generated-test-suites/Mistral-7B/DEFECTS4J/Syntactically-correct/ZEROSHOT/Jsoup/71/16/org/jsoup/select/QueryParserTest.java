import org.junit.Assert;
import org.jsoup.parser.QueryParser;
import org.jsoup.helper.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class QueryParserTest {

	@org.junit.Test
	public void testParseQuery() {
		String query = "div.red > p.italic:first-child";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("div"));
		evaluators.add(new Evaluator.Class("red"));
		evaluators.add(new CombiningEvaluator.Descendant(evaluators.get(0), new Evaluator.Tag("p")));
		evaluators.add(new Evaluator.Class("italic"));
		evaluators.add(new Evaluator.IsFirstChild());
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithAttributes() {
		String query = "input[type='text'][name='username']";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("input"));
		evaluators.add(new AttributeEvaluator("type", "text"));
		evaluators.add(new AttributeEvaluator("name", "username"));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithCombinators() {
		String query = "h1 + p, h1 > ul";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("h1"));
		evaluators.add(new CombiningEvaluator.NextSibling(new Evaluator.Tag("p")));
		evaluators.add(new CombiningEvaluator.And(new Evaluator.Tag("h1"), new TagEvaluator("ul")));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithPseudoClasses() {
		String query = "p:first-child, p:last-child";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("p"));
		evaluators.add(new Evaluator.IsFirstChild());
		evaluators.add(new Evaluator.IsLastChild());
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithIdSelector() {
		String query = "#header > ul";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Id("header"));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("ul")));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithClassSelector() {
		String query = ".button.active";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Class("button"));
		evaluators.add(new Evaluator.Class("active"));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithTagSelector() {
		String query = "li";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("li"));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithCombiningDescendants() {
		String query = "ul > li > a:contains(Home)";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("ul"));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("li")));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("a")));
		evaluators.add(new ContainsTextEvaluator("Home"));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	@org.junit.Test
	public void testParseQueryWithSubSelectors() {
		String query = "ul > li > div > a:contains(Home) > span.highlight";
		Evaluator evaluator = QueryParser.parse(query);
		Assert.assertNotNull(evaluator);
		List<Evaluator> evaluators = new ArrayList<>();
		evaluators.add(new Evaluator.Tag("ul"));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("li")));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("div")));
		evaluators.add(new StructuralEvaluator.ImmediateChild(new Evaluator.Tag("a")));
		evaluators.add(new ContainsTextEvaluator("Home"));
		evaluators.add(new Evaluator.Tag("span"));
		evaluators.add(new Evaluator.Class("highlight"));
		Assert.assertEquals(evaluators, extractEvaluators(evaluator));
	}

	private List<Evaluator> extractEvaluators(Evaluator evaluator) {
		return ((CombiningEvaluator) evaluator).getCombinedEvaluators();
	}

}