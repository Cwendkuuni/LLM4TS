package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.jsoup.parser.TokenQueue;

public class QueryParserTest {

	private static Evaluator parseQuery(String query) {
		QueryParser parser = new QueryParser(query);
		return parser.parse();
	}

	@Test
	public void testEmptyQuery() {
		Evaluator evaluator = parseQuery("");
		assertNull(evaluator);
	}

	@Test
	public void testTagSelector() {
		Evaluator evaluator = parseQuery("div");
		assertEquals(Evaluator.Tag.class, evaluator.getClass());
		assertEquals("div", evaluator.toString());
	}

	@Test
	public void testClassSelector() {
		Evaluator evaluator = parseQuery(".className");
		assertEquals(Evaluator.Class.class, evaluator.getClass());
		assertEquals(".className", evaluator.toString());
	}

	@Test
	public void testIdSelector() {
		Evaluator evaluator = parseQuery("#idName");
		assertEquals(Evaluator.Id.class, evaluator.getClass());
		assertEquals("#idName", evaluator.toString());
	}

	@Test
	public void testAttributeSelector() {
		Evaluator evaluator = parseQuery("[attr=value]");
		assertEquals(Evaluator.Attribute.class, evaluator.getClass());
		assertEquals("[attr=value]", evaluator.toString());
	}

	@Test
	public void testAttributeWithValueSelectors() {
		Evaluator evaluator = parseQuery("[attr*='value']");
		assertEquals(Evaluator.AttributeWithValueContaining.class, evaluator.getClass());
		assertEquals("[attr*='value']", evaluator.toString());

		evaluator = parseQuery("[attr^='value']");
		assertEquals(Evaluator.AttributeWithValueStarting.class, evaluator.getClass());
		assertEquals("[attr^='value']", evaluator.toString());

		evaluator = parseQuery("[attr$='value']");
		assertEquals(Evaluator.AttributeWithValueEnding.class, evaluator.getClass());
		assertEquals("[attr$='value']", evaluator.toString());

		evaluator = parseQuery("[attr=value]");
		assertEquals(Evaluator.Attribute.class, evaluator.getClass());
		assertEquals("[attr=value]", evaluator.toString());

		evaluator = parseQuery("[attr!=value]");
		assertEquals(Evaluator.AttributeWithValueNot.class, evaluator.getClass());
		assertEquals("[attr!=value]", evaluator.toString());
	}

	@Test
	public void testCombinedSelectors() {
		Evaluator evaluator = parseQuery("div.className");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div.className", evaluator.toString());

		evaluator = parseQuery("div#idName.className");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div#idName.className", evaluator.toString());

		evaluator = parseQuery("div#idName > p");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div#idName > p", evaluator.toString());

		evaluator = parseQuery("div#idName + p");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div#idName + p", evaluator.toString());

		evaluator = parseQuery("div#idName ~ p");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div#idName ~ p", evaluator.toString());

		evaluator = parseQuery("div#idName, p");
		assertEquals(CombiningEvaluator.Or.class, evaluator.getClass());
		assertEquals("div#idName, p", evaluator.toString());
	}

	@Test
	public void testDescendantSelectors() {
		Evaluator evaluator = parseQuery("div > p");
		assertEquals(StructuralEvaluator.ImmediateChild.class, evaluator.getClass());
		assertEquals("div > p", evaluator.toString());

		evaluator = parseQuery("div > p > span");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div > p > span", evaluator.toString());

		evaluator = parseQuery("div p");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div p", evaluator.toString());

		evaluator = parseQuery("div > p#idName");
		assertEquals(StructuralEvaluator.ImmediateChildWithId.class, evaluator.getClass());
		assertEquals("div > p#idName", evaluator.toString());

		evaluator = parseQuery("div > p#idName > span");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div > p#idName > span", evaluator.toString());
	}

	@Test
	public void testPseudoClassSelectors() {
		Evaluator evaluator = parseQuery(":empty");
		assertEquals(Evaluator.IsEmpty.class, evaluator.getClass());
		assertEquals(":empty", evaluator.toString());

		evaluator = parseQuery(":root");
		assertEquals(Evaluator.IsRoot.class, evaluator.getClass());
		assertEquals(":root", evaluator.toString());

		evaluator = parseQuery(":last-child");
		assertEquals(Evaluator.IsLastChild.class, evaluator.getClass());
		assertEquals(":last-child", evaluator.toString());

		evaluator = parseQuery(":only-child");
		assertEquals(Evaluator.IsOnlyChild.class, evaluator.getClass());
		assertEquals(":only-child", evaluator.toString());

		evaluator = parseQuery(":only-of-type");
		assertEquals(Evaluator.IsOnlyOfType.class, evaluator.getClass());
		assertEquals(":only-of-type", evaluator.toString());

		evaluator = parseQuery(":first-child");
		assertEquals(Evaluator.IsFirstChild.class, evaluator.getClass());
		assertEquals(":first-child", evaluator.toString());

		evaluator = parseQuery(":first-of-type");
		assertEquals(Evaluator.IsFirstOfType.class, evaluator.getClass());
		assertEquals(":first-of-type", evaluator.toString());

		evaluator = parseQuery(":last-of-type");
		assertEquals(Evaluator.IsLastOfType.class, evaluator.getClass());
		assertEquals(":last-of-type", evaluator.toString());
	}

	@Test
	public void testNthChildSelectors() {
		Evaluator evaluator = parseQuery(":nth-child(2)");
		assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
		assertEquals(":nth-child(2)", evaluator.toString());

		evaluator = parseQuery(":nth-child(2n)");
		assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
		assertEquals(":nth-child(2n)", evaluator.toString());

		evaluator = parseQuery(":nth-child(even)");
		assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
		assertEquals(":nth-child(even)", evaluator.toString());

		evaluator = parseQuery(":nth-child(odd)");
		assertEquals(Evaluator.IsNthChild.class, evaluator.getClass());
		assertEquals(":nth-child(odd)", evaluator.toString());

		evaluator = parseQuery(":nth-last-child(2)");
		assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
		assertEquals(":nth-last-child(2)", evaluator.toString());

		evaluator = parseQuery(":nth-last-child(2n)");
		assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
		assertEquals(":nth-last-child(2n)", evaluator.toString());

		evaluator = parseQuery(":nth-last-child(even)");
		assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
		assertEquals(":nth-last-child(even)", evaluator.toString());

		evaluator = parseQuery(":nth-last-child(odd)");
		assertEquals(Evaluator.IsNthLastChild.class, evaluator.getClass());
		assertEquals(":nth-last-child(odd)", evaluator.toString());

		evaluator = parseQuery(":nth-of-type(2)");
		assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
		assertEquals(":nth-of-type(2)", evaluator.toString());

		evaluator = parseQuery(":nth-of-type(2n)");
		assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
		assertEquals(":nth-of-type(2n)", evaluator.toString());

		evaluator = parseQuery(":nth-of-type(even)");
		assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
		assertEquals(":nth-of-type(even)", evaluator.toString());

		evaluator = parseQuery(":nth-of-type(odd)");
		assertEquals(Evaluator.IsNthOfType.class, evaluator.getClass());
		assertEquals(":nth-of-type(odd)", evaluator.toString());

		evaluator = parseQuery(":nth-last-of-type(2)");
		assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
		assertEquals(":nth-last-of-type(2)", evaluator.toString());

		evaluator = parseQuery(":nth-last-of-type(2n)");
		assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
		assertEquals(":nth-last-of-type(2n)", evaluator.toString());

		evaluator = parseQuery(":nth-last-of-type(even)");
		assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
		assertEquals(":nth-last-of-type(even)", evaluator.toString());

		evaluator = parseQuery(":nth-last-of-type(odd)");
		assertEquals(Evaluator.IsNthLastOfType.class, evaluator.getClass());
		assertEquals(":nth-last-of-type(odd)", evaluator.toString());
	}

	@Test
	public void testHasSelector() {
		Evaluator childEvaluator = parseQuery("div > p");
		Evaluator parentEvaluator = parseQuery("p:has(> div)");

		Evaluator evaluator = parseQuery("p:has(> div > span)");
		assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
		assertEquals("p:has(> div > span)", evaluator.toString());
		assertEquals(childEvaluator, evaluator.getChildEvaluator());
		assertEquals(parentEvaluator, evaluator.getParentEvaluator());
	}

	@Test
	public void testContainsSelector() {
		String text = "The quick brown fox jumps over the lazy dog.";

		Evaluator evaluator = parseQuery(":contains(lazy)");
		assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
		assertEquals(":contains(lazy)", evaluator.toString());

		evaluator = parseQuery(":contains(lazy):not(:contains(dog))");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals(":contains(lazy):not(:contains(dog))", evaluator.toString());

		evaluator = parseQuery("p:contains(lazy)");
		assertEquals(Evaluator.ContainsText.class, evaluator.getClass());
		assertEquals("p:contains(lazy)", evaluator.toString());

		evaluator = parseQuery("p:containsOwn(lazy)");
		assertEquals(Evaluator.ContainsOwnText.class, evaluator.getClass());
		assertEquals("p:containsOwn(lazy)", evaluator.toString());

		evaluator = parseQuery("p:containsData(dog)");
		assertEquals(Evaluator.ContainsData.class, evaluator.getClass());
		assertEquals("p:containsData(dog)", evaluator.toString());

		evaluator = parseQuery("p:containsData(lazy)");
		assertEquals(Evaluator.ContainsData.class, evaluator.getClass());
		assertEquals("p:containsData(lazy)", evaluator.toString());

		evaluator = parseQuery("p:matches(lazy|dog)");
		assertEquals(Evaluator.Matches.class, evaluator.getClass());
		assertEquals("p:matches(lazy|dog)", evaluator.toString());

		evaluator = parseQuery("p:matchesOwn(lazy|dog)");
		assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
		assertEquals("p:matchesOwn(lazy|dog)", evaluator.toString());

		evaluator = parseQuery(":matches(lazy|dog)");
		assertEquals(Evaluator.Matches.class, evaluator.getClass());
		assertEquals(":matches(lazy|dog)", evaluator.toString());

		evaluator = parseQuery(":matchesOwn(lazy|dog)");
		assertEquals(Evaluator.MatchesOwn.class, evaluator.getClass());
		assertEquals(":matchesOwn(lazy|dog)", evaluator.toString());

		evaluator = parseQuery("p:contains(fox) > span:contains(jumps)");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("p:contains(fox) > span:contains(jumps)", evaluator.toString());

		evaluator = parseQuery("p:contains(fox) > span:containsOwn(jumps)");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("p:contains(fox) > span:containsOwn(jumps)", evaluator.toString());

		evaluator = parseQuery("p:contains(fox) > span:has(> a:contains(jumps))");
		assertEquals(StructuralEvaluator.Has.class, evaluator.getClass());
		assertEquals("p:contains(fox) > span:has(> a:contains(jumps))", evaluator.toString());
	}

	@Test
	public void testAttributeWithRegexSelector() {
		Evaluator evaluator = parseQuery("[title~='JSoup']");
		assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
		assertEquals("[title~='JSoup']", evaluator.toString());

		evaluator = parseQuery("[title~='JSoup' i]");
		assertEquals(Evaluator.AttributeWithValueMatching.class, evaluator.getClass());
		assertEquals("[title~='JSoup' i]", evaluator.toString());
	}

	@Test
	public void testComplexQuery() {
		Evaluator evaluator = parseQuery("div#element > p:contains(test) > span:matches(^[A-Z]\\w+$)");
		assertEquals(CombiningEvaluator.And.class, evaluator.getClass());
		assertEquals("div#element > p:contains(test) > span:matches(^[A-Z]\\w+$)", evaluator.toString());
	}
}