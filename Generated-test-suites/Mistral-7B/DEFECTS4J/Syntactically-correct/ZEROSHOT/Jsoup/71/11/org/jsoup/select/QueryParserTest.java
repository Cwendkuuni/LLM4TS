package org.jsoup.select;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.jsoup.select.QueryParser;

public class QueryParserTest {

	@Test
	public void testParseEmptyQuery() {
		QueryParser parser = new QueryParser("");
		assertNull(parser.parse());
	}

	@Test
	public void testParseSimpleQuery() {
		String query = "div";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.Tag);
		assertEquals(evaluator.toString(), "tag:div");
	}

	@Test
	public void testParseQueryWithId() {
		String query = "#myId";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.Id);
		assertEquals(evaluator.toString(), "id:myId");
	}

	@Test
	public void testParseQueryWithClass() {
		String query = ".myClass";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.Class);
		assertEquals(evaluator.toString(), "class:myClass");
	}

	@Test
	public void testParseQueryWithCombinators() {
		String query = "div > p";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertFalse(evaluator instanceof Evaluator.Tag);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		Evaluator childEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(0);
		assertTrue(childEvaluator instanceof StructuralEvaluator.ImmediateParent);
		Evaluator parentEvaluator = ((CombiningEvaluator.And) evaluator).getEvaluators().get(1);
		assertTrue(parentEvaluator instanceof Evaluator.Tag);
		assertEquals(parentEvaluator.toString(), "tag:div");
		assertEquals(childEvaluator.toString(), "isParentOf tag:p");
	}

	@Test
	public void testParseQueryWithAttribute() {
		String query = "[href]";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.Attribute);
		assertEquals(evaluator.toString(), "attribute:href");
	}

	@Test
	public void testParseQueryWithAttributeValue() {
		String query = "[href='https://www.example.com']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
		assertEquals(evaluator.toString(), "attribute:href withValue 'https://www.example.com'");
	}

	@Test
	public void testParseQueryWithMultipleCombinators() {
		String query = "div > p > span.highlight";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertFalse(evaluator instanceof Evaluator.Tag);
		assertTrue(evaluator instanceof CombiningEvaluator.And);
		List<Evaluator> evaluators = Arrays.asList(((CombiningEvaluator.And) evaluator).getEvaluators());
		Evaluator lastEvaluator = evaluators.get(evaluators.size() - 1);
		assertTrue(lastEvaluator instanceof Evaluator.Class);
		assertEquals(lastEvaluator.toString(), "class:highlight");
		Evaluator penultimateEvaluator = evaluators.get(evaluators.size() - 2);
		assertTrue(penultimateEvaluator instanceof StructuralEvaluator.ImmediateChild);
		Evaluator previousEvaluator = evaluators.get(evaluators.size() - 3);
		assertTrue(previousEvaluator instanceof Evaluator.Tag);
		assertEquals(previousEvaluator.toString(), "tag:p");
		Evaluator firstEvaluator = evaluators.get(0);
		assertTrue(firstEvaluator instanceof StructuralEvaluator.ImmediateParent);
		Evaluator parentEvaluator = ((StructuralEvaluator.ImmediateParent) firstEvaluator).getChildEvaluator();
		assertTrue(parentEvaluator instanceof Evaluator.Tag);
		assertEquals(parentEvaluator.toString(), "tag:div");
	}

	@Test
	public void testParseQueryWithPseudoClass() {
		String query = "input:checked";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.PseudoClass);
		assertEquals(evaluator.toString(), "pseudoClass:checked");
	}

	@Test
	public void testParseQueryWithIndex() {
		String query = "li:nth-child(2)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.IsNthChild);
		Evaluator.IsNthChild isNthChildEvaluator = (Evaluator.IsNthChild) evaluator;
		assertTrue(isNthChildEvaluator.getIndex() == 2);
		assertTrue(isNthChildEvaluator.isForward());
	}

	@Test
	public void testParseQueryWithNegativeIndex() {
		String query = "li:nth-last-child(-2)";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.IsNthLastChild);
		Evaluator.IsNthLastChild isNthLastChildEvaluator = (Evaluator.IsNthLastChild) evaluator;
		assertTrue(isNthLastChildEvaluator.getIndex() == 2);
		assertFalse(isNthLastChildEvaluator.isForward());
	}

	@Test
	public void testParseQueryWithAttributeStarting() {
		String query = "[href^='https://www.example.com']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeStarting);
		assertEquals(evaluator.toString(), "attributeStarting:href withValue 'https://www.example.com'");
	}

	@Test
	public void testParseQueryWithAttributeEnding() {
		String query = "[href$='.pdf']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeEnding);
		assertEquals(evaluator.toString(), "attributeEnding:href withValue '.pdf'");
	}

	@Test
	public void testParseQueryWithAttributeContaining() {
		String query = "[href*='example']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueContaining);
		assertEquals(evaluator.toString(), "attributeWithValueContaining:href withValue 'example'");
	}

	@Test
	public void testParseQueryWithAttributeMatching() {
		String query = "[title~='example']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueMatching);
		assertEquals(evaluator.toString(), "attributeWithValueMatching:title withRegex /example/");
	}

	@Test
	public void testParseQueryWithAttributeWithValue() {
		String query = "[data-type='text']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeWithValue);
		assertEquals(evaluator.toString(), "attributeWithValue:data-type withValue 'text'");
	}

	@Test
	public void testParseQueryWithAttributeWithValueNot() {
		String query = "[data-type!='text']";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.AttributeWithValueNot);
		assertEquals(evaluator.toString(), "attributeWithValueNot:data-type withValue 'text'");
	}

	@Test
	public void testParseQueryWithDescendantSelector() {
		String query = "p > a";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertFalse(evaluator instanceof Evaluator.Tag);
		assertTrue(evaluator instanceof CombiningEvaluator.Descendant);
		Evaluator childEvaluator = ((CombiningEvaluator.Descendant) evaluator).getDescendantEvaluator();
		assertTrue(childEvaluator instanceof Evaluator.Tag);
		assertEquals(childEvaluator.toString(), "tag:a");
		Evaluator parentEvaluator = ((CombiningEvaluator.Descendant) evaluator).getParentEvaluator();
		assertTrue(parentEvaluator instanceof Evaluator.Tag);
		assertEquals(parentEvaluator.toString(), "tag:p");
	}

	@Test
	public void testParseQueryWithAdjacentSiblingSelector() {
		String query = "p + a";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertFalse(evaluator instanceof Evaluator.Tag);
		assertTrue(evaluator instanceof CombiningEvaluator.AdjacentSibling);
		Evaluator siblingEvaluator = ((CombiningEvaluator.AdjacentSibling) evaluator).getSiblingEvaluator();
		assertTrue(siblingEvaluator instanceof Evaluator.Tag);
		assertEquals(siblingEvaluator.toString(), "tag:a");
		Evaluator previousSiblingEvaluator = ((CombiningEvaluator.AdjacentSibling) evaluator).getPreviousSiblingEvaluator();
		assertTrue(previousSiblingEvaluator instanceof Evaluator.Tag);
		assertEquals(previousSiblingEvaluator.toString(), "tag:p");
	}

	@Test
	public void testParseQueryWithGeneralSiblingSelector() {
		String query = "p ~ a";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertFalse(evaluator instanceof Evaluator.Tag);
		assertTrue(evaluator instanceof CombiningEvaluator.GeneralSibling);
		Evaluator siblingEvaluator = ((CombiningEvaluator.GeneralSibling) evaluator).getSiblingEvaluator();
		assertTrue(siblingEvaluator instanceof Evaluator.Tag);
		assertEquals(siblingEvaluator.toString(), "tag:a");
		Evaluator previousSiblingEvaluator = ((CombiningEvaluator.GeneralSibling) evaluator).getPreviousSiblingEvaluator();
		assertTrue(previousSiblingEvaluator instanceof Evaluator.Tag);
		assertEquals(previousSiblingEvaluator.toString(), "tag:p");
	}

	@Test
	public void testParseQueryWithPseudoElement() {
		String query = "ul > li:before";
		QueryParser parser = new QueryParser(query);
		Evaluator evaluator = parser.parse();
		assertTrue(evaluator instanceof Evaluator.PseudoElement);
		assertEquals(evaluator.toString(), "pseudoElement:before");
	}

	@Test
	public void testParseQueryWithCombinatorError() {
		String query = "div > p > + a";
		try {
			QueryParser parser = new QueryParser(query);
			parser.parse();
			fail("Expected Selector.SelectorParseException");
		} catch (Selector.SelectorParseException e) {
			assertEquals(e.getMessage(), "Unknown combinator: +");
		}
	}
}