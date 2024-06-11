import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.QueryParser;
import org.jsoup.parser.Selector;
import org.jsoup.parser.parseQuery;

import java.util.List;

public class QueryParserTest {

	@Test
	public void testParseValidQuery() {
		String query = "div.class1 p#id1";
		QueryParser parser = new QueryParser();
		Evaluator evaluator = parser.parse(query);
		List<Selector> selectors = evaluator.getSelectors();
		Assert.assertEquals(2, selectors.size());
		Assert.assertEquals("div.class1", selectors.get(0).toString());
		Assert.assertEquals("p#id1", selectors.get(1).toString());
	}

	@Test
	public void testParseInvalidQuery1() {
		String query = "div.class1 #id1"; // missing space between class and id
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected IllegalArgumentException for invalid query");
		} catch (Selector.SelectorParseException e) {
			Assert.assertEquals("Expected ' ' between class and ID selector: div.class1#id1", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidQuery2() {
		String query = "div.class1#id1.class2"; // class selector with id
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected IllegalArgumentException for invalid query");
		} catch (Selector.SelectorParseException e) {
			Assert.assertEquals("ID selectors can't be applied to class selectors: div.class1#id1.class2", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidQuery3() {
		String query = "div.class1 p[class='class2']"; // invalid attribute selector
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected IllegalArgumentException for invalid query");
		} catch (Selector.SelectorParseException e) {
			Assert.assertEquals("Attribute selectors must have an operator: div.class1 p[class='class2']", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidQuery4() {
		String query = "div.class1 p:contains(text)"; // missing space after contains
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected IllegalArgumentException for invalid query");
		} catch (Selector.SelectorParseException e) {
			Assert.assertEquals("Expected ' ' after ':contains': div.class1 p:contains(text)", e.getMessage());
		}
	}

	@Test
	public void testParseInvalidQuery5() {
		String query = "div.class1:not(p)"; // missing space after not
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected IllegalArgumentException for invalid query");
		} catch (Selector.SelectorParseException e) {
			Assert.assertEquals("Expected ' ' after ':not': div.class1:not(p)", e.getMessage());
		}
	}

	@Test
	public void testParseComplexQuery() {
		String query = "div.class1 > p:first-child:contains(text)";
		QueryParser parser = new QueryParser();
		Evaluator evaluator = parser.parse(query);
		List<Selector> selectors = evaluator.getSelectors();
		Assert.assertEquals(1, selectors.size());
		Assert.assertEquals("div.class1 > p:first-child:contains(text)", selectors.get(0).toString());
	}

	@Test
	public void testParseComplexQueryWithExceptions() {
		String query = "div.class1 > p:nth-child(2) > :nth-child(2) > span:not(:containsOwn(text))";
		try {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			Assert.fail("Expected Selector.SelectorParseException for invalid query");
		} catch (Selector.SelectorParseException e) {
			// Expected exception
		}
	}
}